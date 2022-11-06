package bgu.spl.mics.application.messages;

import bgu.spl.mics.Broadcast;
import bgu.spl.mics.application.objects.ConfrenceInformation;
import bgu.spl.mics.application.objects.Model;
import bgu.spl.mics.application.objects.Pair;

import java.util.LinkedList;
import java.util.Queue;

public class PublishConferenceBroadcast implements Broadcast {
    private final ConfrenceInformation conferenceInfo;
//    private Model[] models;

//    public PublishConferenceBroadcast(LinkedList<Model> models) {
//        this.models = new Model[models.size()];
//        for (int i=0; i< models.size(); i++){
//            this.models[i] = models.poll();
//        }
//    }

    public PublishConferenceBroadcast(ConfrenceInformation confrenceInformation) {
        this.conferenceInfo = confrenceInformation;
    }

    public  ConfrenceInformation getConferenceInformation() {
        return conferenceInfo;
    }

//    public Model[] getModels() {
//        return this.models;
//    }
}
