package bgu.spl.mics.application.objects;

import bgu.spl.mics.application.messages.PublishConferenceBroadcast;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Passive object representing information on a conference.
 * Add fields and methods to this class as you see fit (including public methods and constructors).
 */
public class ConfrenceInformation {
    private LinkedList<Model> success;
    private String name;
    private int date;
    private ArrayList<Model> models;
    public ConfrenceInformation() {
        models = new ArrayList<>();
    }

    public ConfrenceInformation(String name, int date) {
        this.success = new LinkedList<>();
        this.name = name;
        this.date = date;
    }

    public int getDate() {
        return date;
    }

    public String getName() {
        return name;
    }

    public LinkedList<Model> getSuccess() {
        return success;
    }

    @Override
    public String toString() {
        return "ConferenceInformation : " + '\'' + "name = " + name + '\'' + "," + "date = " + date + " .";
    }

    public void addSuccess(Model success) {
        this.models.add(success);
    }

    public ArrayList<Model> getModels() {
        return models;
    }

}
