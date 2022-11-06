package bgu.spl.mics.application.services;

import bgu.spl.mics.MessageBusImpl;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.PublishConferenceBroadcast;
import bgu.spl.mics.application.messages.PublishResultsEvent;
import bgu.spl.mics.application.messages.TickBroadCast;
import bgu.spl.mics.application.objects.ConfrenceInformation;
import bgu.spl.mics.application.objects.Pair;

/**
 * Conference service is in charge of
 * aggregating good results and publishing them via the {@link PublishConferenceBroadcast},
 * after publishing results the conference will unregister from the system.
 * This class may not hold references for objects which it is not responsible for.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class ConferenceService extends MicroService {
    private ConfrenceInformation conference;
    private int ticks;

    public ConferenceService(String name, ConfrenceInformation conference) {
        super(name);
        this.conference = conference;
        this.ticks = 0;
  }

    @Override
    protected void initialize() {
        subscribeEvent(PublishResultsEvent.class, (p) -> {
            conference.addSuccess(p.getModel());
        });

        subscribeBroadcast(TickBroadCast.class, (t) -> {
            if(ticks >= conference.getDate()) {
                PublishConferenceBroadcast publish = new PublishConferenceBroadcast(conference);
                sendBroadcast(publish);
                System.out.println("hereeeeeeeeeeeeeeee");
                this.terminate();
//                MessageBusImpl.getInstance().unregister(this);
            }
        });

    }
}
