package bgu.spl.mics.application.services;
import bgu.spl.mics.Future;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.*;
import bgu.spl.mics.application.objects.Model;
import bgu.spl.mics.application.objects.Pair;
import bgu.spl.mics.application.objects.Student;

import java.util.ArrayList;

/**
 * Student is responsible for sending the {@link TrainModelEvent},
 * {@link TestModelEvent} and {@link PublishResultsEvent}.
 * In addition, it must sign up for the conference publication broadcasts.
 * This class may not hold references for objects which it is not responsible for.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class StudentService extends MicroService {

    private Student student;
    private boolean terminated;
    private Thread send;

    public StudentService(Student student) {
        super("Student " + student.getId() + " Service");
        this.student = student;
        this.terminated = false;
    }

    @Override
    protected void initialize() throws InterruptedException {
        subscribeBroadcast(TerminateBroadcast.class, (c) -> {
            terminated = true;
            send.interrupt();
        });
        subscribeBroadcast(PublishConferenceBroadcast.class, (PCB) -> {
            ArrayList<Model> publishModels = PCB.getConferenceInformation().getModels();
            System.out.println("GOT HERE!!!!!");
            if(publishModels != null) {
                for(Model m : publishModels) {
                    if(m.getStudent().getId() == this.student.getId()) {
                        System.out.println("added publication");
                        student.setPublications(student.getPublications() + 1);
                    }
                    else
                        System.out.println("added paper");
                        student.setPapersRead(student.getPapersRead() + 1);
                }
            }
//           Model[] models = PCB.getModels();
//            for (Model p : models) {
//                p.setPublished(true);
//                if (p.getStudent().getId() == student.getId())
//                    student.setPublications(student.getPublications() + 1);
//                else
//                    student.setPapersRead(student.getPapersRead() + 1);
//            }
        });

        Thread send = new Thread(() -> {
            while (!student.getStudentModels().isEmpty() && !terminated) {
                Model m = student.getStudentModels().remove();
//                student.getTrainedModels().add(m);
                TrainModelEvent trainEvent = new TrainModelEvent(m);
                Future f = sendEvent(trainEvent);
                    try {
                        synchronized (f) {
                            while (f.isDone() && !terminated) {
                                f.wait();
                            }
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                TestModelEvent testModel = new TestModelEvent(this.student.getId(), m);
                    Future<Model> f1 = sendEvent(testModel);
                student.getTrainedModels().add(testModel.getModel());
                if(testModel.getModel().getStatus() == Model.Status.Tested) {
//                    student.getTrainedModels().add(m);
                    student.getTrainedModels().add(testModel.getModel());
                    System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
                    System.out.println("@@@@@@@@@@" + student.getTrainedModels().peek().getResult());
                }
//                    try {
//                        synchronized (f) {
//                            while (m.getResult() == Model.Results.None && !terminated) {
//                                f.wait();
//                            }
//                        }
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
                // print the trained models:

                if (m.getResult() == Model.Results.Good && !terminated) {
                    PublishResultsEvent publishEvent = new PublishResultsEvent(this.student.getId(), m);
                    sendEvent(publishEvent);
                }
            }
//            for(Model mo : student.getTrainedModels()) {
//                System.out.println(mo.getName() + " result: " + mo.getResult());
//            }
        });
        send.start();
    }
}
