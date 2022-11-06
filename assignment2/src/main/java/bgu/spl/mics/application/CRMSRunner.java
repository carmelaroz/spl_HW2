package bgu.spl.mics.application;

import bgu.spl.mics.MessageBusImpl;
import bgu.spl.mics.application.objects.*;
import bgu.spl.mics.application.services.*;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.*;

/** This is the Main class of Compute Resources Management System application. You should parse the input file,
 * create the different instances of the objects, and run the system.
 * In the end, you should output a text file.
 */
public class CRMSRunner {
    public static void main(String[] args) throws IOException, InterruptedException {
        FileReader jsonReader = null;
        try{
            jsonReader = new FileReader(args[0]);
        } catch (FileNotFoundException e) {
            System.out.println("FileNotFoundException");
            System.exit(0);
        }
        JsonObject json = JsonParser.parseReader(jsonReader).getAsJsonObject();
        JsonArray JsonStudents = json.getAsJsonArray("Students");
        JsonArray JsonGPU = json.getAsJsonArray("GPUS");
        JsonArray JsonCPU = json.getAsJsonArray("CPUS");
        JsonArray Jconference = json.getAsJsonArray("Conferences");

        Student[] students = createStudents(JsonStudents);
        Model[] models = createModels(JsonStudents,students);
        GPU[] gpus = createGPU(JsonGPU);
        CPU[] cpus = createCPU(JsonCPU);
        ConfrenceInformation[] conferenceInformation = createConferences(Jconference);

        int TickTime = json.get("TickTime").getAsInt();
        int Duration = json.get("Duration").getAsInt();

        /* Create all objects from JSON */

        Cluster cluster = Cluster.getInstance();
        MessageBusImpl messageBus = MessageBusImpl.getInstance();

        for(Student s : students) {
            StudentService b = new StudentService(s);
            Thread t = new Thread(b);
            t.start();
        }

        int GPUindex = 1;
        for(GPU gpu : gpus) {
            cluster.addGPU(gpu);
            GPUService g = new GPUService("GPUService " + GPUindex, gpu);
            GPUindex++;
            Thread t = new Thread(g);
            t.start();
        }

        int CPUindex = 1;
        for(CPU cpu : cpus) {
            cluster.addCPU(cpu);
            CPUService c = new CPUService("CPUService " + CPUindex, cpu);
            CPUindex++;
            Thread t = new Thread(c);
            t.start();
        }

        int confIndex = 1;
        for(ConfrenceInformation conf : conferenceInformation) {
            ConferenceService c = new ConferenceService("conferenceService " + confIndex, conf);
            confIndex++;
            Thread t = new Thread(c);
            t.start();
        }

        TimeService timeService = new TimeService(TickTime, Duration);
        Thread timeServiceThread = new Thread(timeService);
        timeServiceThread.start();
        timeServiceThread.join();


        File file = new File("./output.txt");
        FileWriter w = new FileWriter(file);
        PrintWriter p = new PrintWriter(w);

        p.println("{");
        p.println("    \"students\": [");
        for(Student s : students) {
            p.println("        {");
            p.println("            \"name\": " + "\"" + s.getName() + "\",");
            p.println("            \"department\": " + "\"" + s.getDepartment() + "\",");
            p.println("            \"status\": " + "\"" + s.getStatus() + "\",");
            p.println("            \"publications\": " + s.getPublications() + ",");
            p.println("            \"papersRead\": " + s.getPapersRead() + ",");
            if(s.getTrainedModels().isEmpty())
                p.println("            \"trainedModels\": []");
            else {
                p.println("            \"trainedModels\": [");
                for (Model m : s.getTrainedModels()) {
                    p.println("                {");
                    p.println("                    \"name\": " + m.getName());
                    p.println("                    \"data\": {");
                    p.println("                         \"type\": " + m.getData().getType());
                    p.println("                         \"size\": " + m.getData().getSize());
                    p.println("                    },");
                    p.println("                    \"status\": " + m.getStatus());
                    p.println("                    \"result\": " + m.getResult());


                }
            }
            p.println("        },");
        }
        p.println("    ],");
        p.println("    \"conferences\": [");
        for(ConfrenceInformation c : conferenceInformation) {
            p.println("        {");
            p.println("            \"name\": " + c.getName());
            p.println("            \"date\": " + c.getDate());
            if(c.getSuccess().isEmpty()) {
                p.println("            \"publications\": [],");
            }
            else {

            }
            p.println("        },");
        }
        p.println("    ],");
        p.println("    \"cpuTimeUsed\": " + cluster.getStatistics().getCPUTime());
        p.println("    \"gpuTimeUsed\": " + cluster.getStatistics().getGPUTime());
        p.println("    \"batchesProcessed\": " + cluster.getStatistics().getNumOfFinishedModels());
        p.close();
    }
    private static Student[] createStudents(JsonArray array){
        Student[] students = new Student[array.size()];
        for (int i = 0; i < array.size(); i++) {
            JsonObject o = array.get(i).getAsJsonObject();
            students[i] = new Student(o.get("name").getAsString(),o.get("department").getAsString(),o.get("status").getAsString(),i);
        }
        return students;
    }

    private static Model[] createModels(JsonArray array,Student[] students) {
        int length = array.size();
        Model[] models = new Model[length];
        for (int i = 0; i < array.size(); i++) {
            JsonObject o = array.get(i).getAsJsonObject();
            JsonArray StudentsModel = o.getAsJsonArray("models");
            for(int j = 0; j < StudentsModel.size(); j++){
                JsonObject Model = StudentsModel.get(j).getAsJsonObject();
                Data d = new Data(Model.get("type").getAsString(),Model.get("size").getAsInt());
                models[j] = new Model(Model.get("name").getAsString(),d,students[i]);
                students[i].addModel(models[j]);   // add models to students
            }
        }
        return models;
    }

    private static GPU[] createGPU(JsonArray array){
        GPU[] gpu = new GPU[array.size()];
        for (int i = 0; i < array.size(); i++) {
            gpu[i] = new GPU(array.get(i).getAsString(), i,Cluster.getInstance());
        }
        return gpu;
    }

    private static CPU[] createCPU(JsonArray array){
        CPU[] cpu = new CPU[array.size()];
        for (int i = 0; i < array.size(); i++) {
            cpu[i] = new CPU(array.get(i).getAsInt(),Cluster.getInstance());
        }
        return cpu;
    }

    private static ConfrenceInformation[] createConferences(JsonArray array){
        ConfrenceInformation[] conferenceInformations = new ConfrenceInformation[array.size()];
        for (int i = 0; i < array.size(); i++) {
            JsonObject temp = array.get(i).getAsJsonObject();
            conferenceInformations[i] = new ConfrenceInformation(temp.get("name").getAsString(),temp.get("date").getAsInt());
        }
        return conferenceInformations;
    }

    public static void printStatistics() {
        Statistics myStatistics = Cluster.getInstance().getStatistics();
//        while(!myStatistics.getModels().isEmpty()) {
//            System.out.println("model: " + myStatistics.getModels().pop());
//        }
        System.out.println("total num of databatch processed: " + myStatistics.getNumOfFinishedModels());
        System.out.println("cpu time: " + myStatistics.getCPUTime());
        System.out.println("gpu time: " + myStatistics.getGPUTime());
    }
}