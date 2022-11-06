package bgu.spl.mics.application.objects;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Passive object representing single student.
 * Add fields and methods to this class as you see fit (including public methods and constructors).
 */
public class Student {
    /**
     * Enum representing the Degree the student is studying for.
     */
    enum Degree {
        MSc, PhD
    }

    private String name;
    private String department;
    private Degree status;
    private int publications;
    private int papersRead;
    private Queue<Model> trainedModels;
    private final int id;
    private LinkedList<Model> studentModels;

    public Student(String name, String department, String degree, int id){
        this.id = id;
        this.name = name;
        this.department = department;
        if(degree == "MSc")
            this.status = Degree.MSc;
        else
            this.status = Degree.PhD;
        publications = 0;
        papersRead = 0;
        this.trainedModels = new LinkedList<>();
        this.studentModels = new LinkedList<>();
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDepartment() {
        return department;
    }

    public String getStatus() {
        return status.toString();
    }

    public int getPublications() {
        return publications;
    }

    public int getPapersRead() {
        return papersRead;
    }

    public Queue<Model> getTrainedModels() {
        return trainedModels;
    }
    public Queue<Model> getStudentModels() {
        return studentModels;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public void setStatus(Degree status) {
        this.status = status;
    }

    public void setPublications(int publications) {
        this.publications = publications;
    }

    public void setPapersRead(int papersRead) {
        this.papersRead = papersRead;
    }

//    public void AddModel(Model m){
//        this.untrainedModels.add(m);
//    }

    @Override
    public String toString() {
        return "Student : " + '\'' + "name = '" + name + '\'' +", department = '" + department + '\'' +", status = " + status +", publications = " + publications +", papersRead = " + papersRead + " .";
    }

    public void addModel(Model m) {
        studentModels.add(m);
    }

}
