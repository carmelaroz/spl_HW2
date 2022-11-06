package bgu.spl.mics.application.objects;

import java.util.Stack;
import java.util.concurrent.atomic.AtomicInteger;

public class Statistics {
    private Stack<String> models;
    private AtomicInteger GPUTime;
    private AtomicInteger CPUTime;
    private AtomicInteger numOfFinishedDataBatches;

public Statistics(){
    GPUTime = new AtomicInteger(0);
    CPUTime = new AtomicInteger(0);
    numOfFinishedDataBatches = new AtomicInteger(0);
    models = new Stack<String>();
}

    public void GPUUsed() {
       GPUTime.incrementAndGet();
    }

    public void CPUUsed(){
       CPUTime.incrementAndGet();
    }

    public Stack<String> getModels() {
        return models;
    }

    public AtomicInteger getGPUTime() {
        return this.GPUTime;
    }
    public AtomicInteger getCPUTime() {
        return this.CPUTime;
    }
    public AtomicInteger getNumOfFinishedModels() {
        return this.numOfFinishedDataBatches;
    }

    public void addProcessedFromCPU(){
       numOfFinishedDataBatches.incrementAndGet();
    }

    public void AddModel(Model model) {
        synchronized (model) {
            this.models.add(model.getName());
        }
    }
}