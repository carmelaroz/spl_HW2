package bgu.spl.mics.application.objects;

import bgu.spl.mics.Event;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GPUTest {
    private static GPU gpu;

//    @BeforeEach
//    public void setUp() {
//        Student s = new Student();
//        Data d = new Data(Data.Type.Images, 0, 8000);
//        Model model = new Model("model1", d, s, Status.preTrained, Results.None);
//        gpu = new GPU(GPU.Type.GTX1080, model);
//    }
//
//    @Test
//    public void divideData() {
//        gpu.divideData(gpu.getModel().getData());
//        assertTrue(gpu.getUnprocessed().size() == 8);
//        for(DataBatch b : gpu.getUnprocessed()) {
//            assertTrue(b.getNumOfSamples() == 1000);
//        }
//    }
//
//    @Test
//    public void sendToCluster() {
//        DataBatch d = new DataBatch();
//        gpu.getUnprocessed().add(d);
//        int size = gpu.getUnprocessed().size();
//        gpu.sendToCluster(d);
//        assertEquals(size - 1, gpu.getUnprocessed().size());
//    }
//
//    @Test
//    public void receiveProcessData() {
//        int size = gpu.getProcessed().size();
//        gpu.receiveProcessData();
//        assertEquals(size + 1, gpu.getProcessed().size());
//    }
//
//    @Test
//    public void trainModel() {
//        DataBatch d = new DataBatch();
//        gpu.trainModel(d);
//        assertTrue(gpu.getTrained().remove().getStatus() == StatusDB.trained);
//    }
}