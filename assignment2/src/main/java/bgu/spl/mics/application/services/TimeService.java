package bgu.spl.mics.application.services;

import bgu.spl.mics.MessageBusImpl;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.CRMSRunner;
import bgu.spl.mics.application.messages.CloseAll;
import bgu.spl.mics.application.messages.TerminateBroadcast;
import bgu.spl.mics.application.messages.TickBroadCast;

import java.util.Timer;
import java.util.TimerTask;

/**
 * TimeService is the global system timer There is only one instance of this micro-service.
 * It keeps track of the amount of ticks passed since initialization and notifies
 * all other micro-services about the current time tick using {@link TickBroadCast}.
 * This class may not hold references for objects which it is not responsible for.
 * 
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class TimeService extends MicroService{
	private Timer clock;
	private int speed;
	private int duration;

	public TimeService(int speed, int duration) {
		super("TimeService");
		this.speed = speed;
		this.duration = duration;
		this.clock = new Timer();
	}

	@Override
	protected void initialize() throws InterruptedException {
		TickBroadCast tick = new TickBroadCast();
		TimerTask t = new TimerTask() {
			@Override
			public void run() {
				sendBroadcast(tick);
				duration--;
			}
		};
		TimerTask end = new TimerTask() {
			@Override
			public void run() {
				clock.cancel();
				sendBroadcast(new CloseAll());
				sendBroadcast(new TerminateBroadcast());
				CRMSRunner.printStatistics();
			}
		};
		clock.scheduleAtFixedRate(t, 0, speed);
		clock.schedule(end,duration*speed-speed);

	}

//	public static class CPUTick implements Callback{
//		private CPUService c;
//		public CPUTick(CPUService c,)
//	}

}
