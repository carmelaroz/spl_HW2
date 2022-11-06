package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;
import bgu.spl.mics.Future;
import bgu.spl.mics.application.objects.Model;

public class TestModelEvent implements Event<Model> {
    private int id;
    private Future<Model> future;
    private Model model;

    public TestModelEvent(int id, Model model) {
        this.id = id;
        this.model = model;
        future = new Future<Model>();
    }

    public String getType(){
        return "Model";
    }

    public Future<Model> getFuture() {
        return this.future;
    }

    public int getId() {
        return this.id;
    }

    public Model getModel() {
        return this.model;
    }

    public void setFuture(Future<Model> future) {
        this.future = future;
    }

    public void setModel(Model model) {
        this.model = model;
    }
}
