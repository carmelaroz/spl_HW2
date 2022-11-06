package bgu.spl.mics.application.services;

import bgu.spl.mics.Event;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.TestModelEvent;
import bgu.spl.mics.application.messages.TickBroadCast;
import bgu.spl.mics.application.messages.TrainModelEvent;
import bgu.spl.mics.application.objects.Cluster;
import bgu.spl.mics.application.objects.GPU;
import bgu.spl.mics.application.objects.Model;

import java.util.LinkedList;
import java.util.Queue;

/**
 * GPU service is responsible for handling the
 * {@link TrainModelEvent} and {@link TestModelEvent},
 * in addition to sending the {@link DataPreProcessEvent}.
 * This class may not hold references for objects which it is not responsible for.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class GPUService extends MicroService {
    private GPU gpu;
    private Model model;
    private Event event;
    private Queue<Event> events;

    public GPUService(String name, GPU gpu) {
        super(name);
        this.gpu = gpu;
        this.event = null;
        this.model = null;
        this.events = new LinkedList<Event>();
    }

    public Model getModel() {
        return model;
    }

    public Queue<Event> getEvents() {
        return events;
    }

    @Override
    protected void initialize() {
        Cluster.getInstance().addGPU(gpu);
        subscribeEvent(TrainModelEvent.class, (m) -> {
            event = m;
            gpu.setModel(m.getModel());
//            model = m.getModel();
            gpu.divideData(gpu.getModel().getData());
            for (int i = 0; i < gpu.getCapacity(); i++) {
                if(!gpu.getUnprocessed().isEmpty()) {
                    Cluster.getInstance().getFromGpu(gpu.getUnprocessed().remove());

                }
            }

        });
        subscribeBroadcast(TickBroadCast.class, (t) -> {
            gpu.updateTime();
            if(gpu.getModel() != null) {
                if(gpu.getModel().getStatus() == Model.Status.Trained) {
                    complete(event, gpu.getModel());
                } else{
                    gpu.trainModel();
                }
            }
        });
        subscribeEvent(TestModelEvent.class, (n) -> {
            gpu.testModelEvent(n.getModel());
            complete((Event)n, n.getModel());
        });
    }
}