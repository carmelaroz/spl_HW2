package bgu.spl.mics.application.objects;


import bgu.spl.mics.application.services.CPUService;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Passive object representing the cluster.
 * <p>
 * This class must be implemented safely as a thread-safe singleton.
 * Add all the fields described in the assignment as private fields.
 * Add fields and methods to this class as you see fit (including public methods and constructors).
 */
public class Cluster {
	private Statistics statistics = new Statistics();
	private BlockingQueue<GPU> GPUS = new LinkedBlockingQueue<>();
	private BlockingQueue<CPU> CPUS = new LinkedBlockingQueue<>();
//	private int gpuId;
	private BlockingQueue<Pair<DataBatch,Integer>> unprocessed = new LinkedBlockingQueue<>();

	private static class clusterHolder{
		// singleton
		private static Cluster clusterInstance = new Cluster();
	}

	public static Cluster getInstance() {
		return clusterHolder.clusterInstance;
	}

	public BlockingQueue<Pair<DataBatch,Integer>> getUnprocessed() {
		return unprocessed;
	}

//	public Pair<DataBatch,Integer> getDataBatch() {
//		try {
//			return unprocessed.take();
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
//	}

	public void addCPU(CPU cpu) {
		CPUS.add(cpu);
	}

	public void addGPU(GPU gpu) {
		GPUS.add(gpu);
	}

	/**
     * Retrieves the single instance of this class.
     */

	public Statistics getStatistics() {
		synchronized (statistics) {
			return statistics;
		}
	}

	public void getFromGpu(Pair<DataBatch,Integer> d) {
//		gpuId = d.getSecondValue();
		unprocessed.add(d);
	}

	public void getFromCPU(DataBatch d, int id) {
		statistics.addProcessedFromCPU();
		System.out.println(statistics.getNumOfFinishedModels());
		// find the suitable gpu
		for(GPU g : GPUS) {
			if(g.getId() == id) {
				g.receiveProcessData(d);
			}
		}
	}
}
