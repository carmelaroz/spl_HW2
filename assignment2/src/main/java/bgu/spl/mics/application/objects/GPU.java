package bgu.spl.mics.application.objects;

import bgu.spl.mics.Event;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Passive object representing a single GPU.
 * Add all the fields described in the assignment as private fields.
 * Add fields and methods to this class as you see fit (including public methods and constructors).
 */
public class GPU {
    /**
     * Enum representing the type of the GPU.
     */
    enum Type {RTX3090, RTX2080, GTX1080}

    private int id;
    private Type type;
    private Model model;
    private int capacity;
    private Cluster cluster;
    private int ticks;
    private int currTick;
    private DataBatch currTrainedDataBatch;
    private Queue<DataBatch> processed;
    private Queue<Pair<DataBatch,Integer>> unprocessed;
    private Queue<DataBatch> trained;

    public GPU(String type,int id, Cluster cluster) {
        this.id = id;
        this.type = Type.valueOf(type);
        this.model = null;
        processed = new LinkedList<DataBatch>();
        unprocessed = new LinkedList<Pair<DataBatch,Integer>>();
        trained = new LinkedList<DataBatch>();
        if (Type.valueOf(type) == Type.RTX3090) {
            capacity = 32;
            ticks = 1;
        }

        if (Type.valueOf(type) == Type.RTX2080) {
            capacity = 16;
            ticks = 2;
        }
        else {
            capacity = 8;
            ticks = 4;
        }
        this.currTick = 0;
        this.currTrainedDataBatch = null;
        this.cluster = cluster;
    }

    public Queue<Pair<DataBatch,Integer>> getUnprocessed() {
        return unprocessed;
    }

    public Queue<DataBatch> getProcessed() {
        return processed;
    }

    public Queue<DataBatch> getTrained() {
        return trained;
    }

    public Type getType() {
        return type;
    }

    public Model getModel() {
        return model;
    }

    public int getCapacity() {
        return capacity;
    }

    public int getId() {
        return id;
    }

    public void setModel(Model m) {
        this.model = m;
    }

    /**
     * @inv: none
     * @pre: none
     * @after: unprocessed.size() > 0
     */
    public void divideData(Data d) throws InterruptedException {
        // receive data from GPUService
        // divide the data to data batches and insert the data to unprocessed queue
        int size = d.getSize();
        int index = 0;
        cluster.getStatistics().GPUUsed();

        for (int i = 1; i <= size; i++) {
            if (i % 1000 == 0) {
                Data divided_data = new Data(d.getType().toString(), 1000);
                DataBatch db = new DataBatch(divided_data, index);
                index++;
                unprocessed.add(new Pair<DataBatch,Integer>(db,id));
                size = size - 1000;
            }
            // if were left less than 1000 data:
            if (i == size) {
                Data divided_data = new Data(d.getType().toString(), size);
                DataBatch db = new DataBatch(divided_data, index);
                unprocessed.add(new Pair<DataBatch,Integer>(db,id));
            }
        }
//        sendToCluster();
        cluster.getFromGpu(this.unprocessed.remove());
    }

    /**
     * @inv: none
     * @pre: Unprocessed.size > 0
     * @after: Unprocessed.size() + 1 == @PRE(Unprocessed.size())
     */
//    public void sendToCluster() throws InterruptedException {
//        // send unprocessed data batch
//        for (int i = 0; i < capacity; i++) {
//            if(!unprocessed.isEmpty()) {
//                cluster.getFromGpu(this.unprocessed.remove());
//            }
//        }
//    }

    /**
     * @inv: none
     * @pre: none
     * @after: processed.size() + 1 == @PRE(processed.size())
     */
    public void receiveProcessData(DataBatch d) {
        // receive the processed data from cluster
        // add the data to processed queue
        // make sure the processed data has limitation !
        processed.add(d);
    }

    /**
     * @inv: none
     * @pre: none
     * @after: trained.remove() == StatusDB.trained
     */
    public void trainModel() throws InterruptedException {
        // train the model
        // add the trained dataBatch to trained queue and set them trained
        if (currTrainedDataBatch == null && !processed.isEmpty()) {
            extractData();
        }
        else if(this.model != null && currTrainedDataBatch != null) {
            cluster.getStatistics().GPUUsed();
            model.setStatus(Model.Status.Training);
            if (ticks == currTick) {
                model.getData().updateProcess();
                currTrainedDataBatch.setStatus(StatusDB.trained);
                model.setStatus(Model.Status.Trained);
                trained.add(currTrainedDataBatch);
//                if(unprocessed != null)
                cluster.getFromGpu(this.unprocessed.remove());
                cluster.getStatistics().AddModel(model);
                currTick = 0;
                currTrainedDataBatch = null;
            }
        }
    }

    public void updateTime() throws InterruptedException {
        currTick++;
        if(!unprocessed.isEmpty()) {
            cluster.getFromGpu(this.unprocessed.remove());
        }
        trainModel();

    }

    public void extractData() {
        if(!processed.isEmpty()) {
            currTrainedDataBatch = processed.remove();
        }
    }

    public void testModelEvent(Model m) {
        double probability = Math.random();
        if (m.getStudent().getStatus() == "MSc" && probability > 0.4) {
            m.setResults(Model.Results.Good);
            m.setStatus(Model.Status.Tested);
        } else if (m.getStudent().getStatus() == "PhD" && probability > 0.2) {
            m.setResults(Model.Results.Good);
            m.setStatus(Model.Status.Tested);

        } else {
            m.setResults(Model.Results.Bad);
            m.setStatus(Model.Status.Tested);
        }
    }
}
