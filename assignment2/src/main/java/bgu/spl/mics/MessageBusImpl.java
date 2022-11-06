package bgu.spl.mics;

import bgu.spl.mics.application.services.GPUService;

import java.util.Map;
import java.util.concurrent.*;

/**
 * The {@link MessageBusImpl class is the implementation of the MessageBus interface.
 * Write your implementation here!
 * Only private fields and methods can be added to this class.
 */
public class MessageBusImpl implements MessageBus {

	private ConcurrentHashMap<Class<? extends Message>, BlockingQueue<MicroService>> messageHashMap = new ConcurrentHashMap<>();
	private ConcurrentHashMap<MicroService, LinkedBlockingDeque<Message>> microServiceHashMap = new ConcurrentHashMap<>();
	private ConcurrentHashMap<Event, Future> futureHashMap = new ConcurrentHashMap<>();
	private Object sendEventLocker = new Object();

	private static class SingletonHolder {
		private static MessageBusImpl instance = new MessageBusImpl();
	}

	public synchronized static MessageBusImpl getInstance() {
		return SingletonHolder.instance;
	}

	@Override
	/**
	 * @inv: none
	 * @pre: none
	 * @post: m is subscribed to the event
	 */
	public <T> void subscribeEvent(Class<? extends Event<T>> type, MicroService m) {
		messageHashMap.putIfAbsent(type, new LinkedBlockingQueue<>());
		messageHashMap.get(type).add(m);
	}

	@Override
	/**
	 * @inv: none
	 * @pre: none
	 * @post: m is subscribed to broadcast
	 */
	public void subscribeBroadcast(Class<? extends Broadcast> type, MicroService m) {
		messageHashMap.putIfAbsent(type, new LinkedBlockingQueue<>());
		messageHashMap.get(type).add(m);
	}

	@Override
	/**
	 * @inv: none
	 * @pre: none
	 * @post: the result of future will not be null
	 */
	public <T> void complete(Event<T> e, T result) {
		futureHashMap.get(e).resolve(result);

	}

	@Override
	/**
	 * @inv: none
	 * @pre: none
	 * @post: m is subscribed to broadcast b
	 */
	public void sendBroadcast(Broadcast b) {
		if(messageHashMap.get(b.getClass()) != null)
		for (MicroService m : messageHashMap.get(b.getClass())) {
			if (microServiceHashMap.containsKey(m))
				microServiceHashMap.get(m).add(b);
		}
	}

	@Override
	/**
	 * @inv: none
	 * @pre: none
	 * @post: event e is added to one of the micro service's queue
	 */
	public <T> Future<T> sendEvent(Event<T> e) {
		synchronized (sendEventLocker) {
			if (messageHashMap.containsKey(e.getClass()) && !messageHashMap.get(e.getClass()).isEmpty()) {
				MicroService m = messageHashMap.get(e.getClass()).poll();
				messageHashMap.get(e.getClass()).add(m);
				if (microServiceHashMap.containsKey(m)) {
					microServiceHashMap.get(m).add(e);
					Future<T> f = new Future<>();
					futureHashMap.put(e, f);
					return f;
				}
			}
		}
		return null;
	}

	@Override
	/**
	 * @inv: none
	 * @pre: none
	 * @post: create a new queue for m in the message bus
	 */
	public void register(MicroService m) {
		microServiceHashMap.putIfAbsent(m, new LinkedBlockingDeque<>());
	}

	@Override
	/**
	 * @inv: none
	 * @pre: none
	 * @post: erase the queue of m from the message bus
	 */
	public void unregister(MicroService m) {
		microServiceHashMap.remove(m);
//		m.terminate();
		for (Map.Entry<Class<? extends Message>, BlockingQueue<MicroService>> mes : messageHashMap.entrySet()) {
			synchronized (m) {
				if (messageHashMap.containsKey(m))
					messageHashMap.get(mes).remove(m);
			}
		}


	}

	@Override
	/**
	 * @inv: none
	 * @pre: none
	 * @post: @PRE(m.queue.size()) == m.queue.size() + 1
	 */
	public Message awaitMessage(MicroService m) throws InterruptedException {
//		BlockingQueue<Message> events = microServiceHashMap.get(m);
		while (m.getClass() == GPUService.class && microServiceHashMap.get(m).peek() instanceof Event) {
			GPUService gpus = (GPUService) m;
			// if microservice is gpuservice we want to push the event to a queue
			gpus.getEvents().add((Event) microServiceHashMap.get(m).take());
			if(gpus.getModel() == null) {
				return gpus.getEvents().remove();
			}
		}
//		if(m.getClass()== GPUService.class){
//			GPUService gpus = (GPUService) m;
//			if (events.isEmpty() || ((gpus.getModel() != null) && (events.peek() instanceof Event))) {
//				synchronized (m) {
//					m.wait();
//				}
//			}
//
			Message ans;
			ans = microServiceHashMap.get(m).take();  // take() is waiting if the queue is empty
			return ans;
	}
}
