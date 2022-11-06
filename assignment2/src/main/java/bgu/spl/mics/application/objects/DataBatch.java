package bgu.spl.mics.application.objects;

/**
 * Passive object representing a data used by a model.
 * Add fields and methods to this class as you see fit (including public methods and constructors).
 */

enum StatusDB {unprocessed, processed, trained}

public class DataBatch {
    Data data;
    int start_index;
    private int numOfSamples;
    private StatusDB status;
    private Data.Type type;
    private int time;

    public DataBatch(Data d, int start_index) {
        this.data = d;
        this.start_index = start_index;
        this.numOfSamples = 0;
        this.status = StatusDB.unprocessed;
        this.time = 0;
    }

    public int getNumOfSamples() {
        return numOfSamples;
    }

    public StatusDB getStatus() {
        return status;
    }

    public void setStatus(StatusDB status) {
        this.status = status;
    }

}
