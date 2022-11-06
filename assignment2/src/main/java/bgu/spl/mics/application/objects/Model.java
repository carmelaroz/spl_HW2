package bgu.spl.mics.application.objects;
/**
 * Passive object representing a Deep Learning model.
 * Add all the fields described in the assignment as private fields.
 * Add fields and methods to this class as you see fit (including public methods and constructors).
 */


public class Model {
    public enum Status {preTrained, Training, Trained, Tested}
    public enum Results {None, Good, Bad}
    private String name;
    private Data data;
    private Student student;
    private Status status;
    private Results results;
    private boolean isPublished;
    private double P;


    public Model(String name, Data data, Student student) {
        this.name = name;
        this.data = data;
        this.student = student;
        this.status = Status.preTrained;
        this.results = Results.None;
    }

    public String getName() {
        return name;
    }

    public Data getData() {
        return data;
    }

    public Status getStatus() {
        return this.status;
    }

    public Results getResult() {
        return this.results;
    }

    public Student getStudent(){
        return this.student;
    }

    public double getProbability() {
        return P;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setResults(Results result) {
        this.results = result;
    }

    public void setPublished(boolean published) {
        this.isPublished = published;
    }

    public boolean isPublished() {
        return isPublished;
    }

    @Override
    public String toString() {
        return "Model : " + '\'' + "name = '" + name + "," + '\'' + "data = " + data + "," + "status = " + status + ".";
    }
}


