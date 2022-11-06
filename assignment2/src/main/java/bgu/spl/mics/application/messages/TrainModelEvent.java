package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;
import bgu.spl.mics.Future;
import bgu.spl.mics.application.objects.Model;
import bgu.spl.mics.application.services.GPUService;

public class TrainModelEvent implements Event<Model> {
    private Future<Model> future;
    private Model model;

    public TrainModelEvent(Model model) {
        this.model = model;
        future = new Future<Model>();
    }

    public String getType(){
        return "Model";
    }

    public Future<Model> getFuture() {
        return future;
    }

    public Model getModel() {
        return model;
    }

    public void setFuture(Future<Model> future) {
        this.future = future;
    }

    public void setModel(Model model) {
        this.model = model;
    }

    public String toString() {
        return "event " + model.getName() + ", " + model.getResult();
    }
}
