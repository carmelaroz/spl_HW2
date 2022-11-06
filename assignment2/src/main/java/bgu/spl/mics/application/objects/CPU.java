package bgu.spl.mics.application.objects;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Passive object representing a single CPU.
 * Add all the fields described in the assignment as private fields.
 * Add fields and methods to this class as you see fit (including public methods and constructors).
 */
public class CPU {
    private int cores;
    private Queue<Pair<DataBatch,Integer>> dataBatch;
    private int currTick;
    private DataBatch currDataBatch;
    private int currGPUId;
    private Cluster cluster;
    private int timeCpu = 0;
    private AtomicInteger count = new AtomicInteger(0); // need to delete

    public CPU(int cores, Cluster cluster) {
        this.cores = cores;
        this.dataBatch =new LinkedList<Pair<DataBatch,Integer>>();
        this.currDataBatch = null;
        this.cluster = cluster;
    }

    public int getCores() {
        return cores;
    }

    public Queue<Pair<DataBatch,Integer>> getDataBatch() {
        return dataBatch;
    }

    /**
     * @inv: none
     * @pre: none
     * @post: dataBatch.size() == @PRE(data.size() + 1)
     */
//    public boolean receiveData() {
//        if(!cluster.getUnprocessed().isEmpty()) {
//            synchronized (cluster.getUnprocessed()) {
//                try {
//                    dataBatch.add(cluster.getUnprocessed().take());
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                return true;
//            }
//        }
//        return false;
//    }

    /**
     * @inv: none
     * @pre: data.size() > 0
     * @post: @PRE(data.size) == @AFTER(data.size)
     */
    public void process() {
        // enqueue element from the list
        // processing the data batch and then send to cluster
        // make StatusDB to process
        if(currDataBatch == null) {
            extractData();
        }
        if(currDataBatch != null) {
            cluster.getStatistics().CPUUsed();  // count the time of cpu
            if(currDataBatch.data.getType() == Data.Type.Images) {
                if (currTick >= (32 / cores) * 4) {
                    currTick = 0;
                    currDataBatch.setStatus(StatusDB.processed);
                    cluster.getFromCPU(currDataBatch, currGPUId);
                    currDataBatch = null;
                    extractData();
                }
            }
            else if(currDataBatch.data.getType() == Data.Type.Text) {
                if (currTick >= (32 / cores) * 2) {
                    currTick = 0;
                    currDataBatch.setStatus(StatusDB.processed);
                    cluster.getFromCPU(currDataBatch, currGPUId);
                    currDataBatch = null;
                    extractData();
                }
            }
            else if(currDataBatch.data.getType() == Data.Type.Tabular){
                if (currTick >= (32 / cores)) {
                    count.getAndIncrement();
                    currTick = 0;
                    currDataBatch.setStatus(StatusDB.processed);
                    cluster.getFromCPU(currDataBatch, currGPUId);
                    currDataBatch = null;
                    extractData();
                }
            }
        }
    }

    public void updateTime() {
        currTick++;
        process();
    }

    public void extractData() {
        if(!cluster.getUnprocessed().isEmpty()) {
            synchronized (cluster.getUnprocessed()) {
                try {
                    currDataBatch = cluster.getUnprocessed().take().getFirstValue();
                    currGPUId = cluster.getUnprocessed().take().getSecondValue();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
//        if(receiveData()) {
//            Pair<DataBatch,Integer> d = dataBatch.remove();
//            currDataBatch = d.getFirstValue();
//            currGPUId = d.getSecondValue();
//        }
    }

    public int getTimeCpu() {
        return timeCpu;
    }
}
