package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;
import bgu.spl.mics.Future;
import bgu.spl.mics.application.objects.Model;

public class PublishResultsEvent implements Event<Boolean> {
    private int id;
    private Future<Boolean> future;
    private Model model;

    public PublishResultsEvent(int id, Model model) {
        this.id = id;
        this.model = model;
        future = new Future<Boolean>();
    }

    public String getType(){
        return "Model";
    }

    public Future<Boolean> getFuture() {
        return this.future;
    }

    public int getId() {
        return this.id;
    }

    public Model getModel() {
        return this.model;
    }

    public void setModel(Model model) {
        this.model = model;
    }

    public void setFuture(Future<Boolean> future) {
        this.future = future;
    }
}
