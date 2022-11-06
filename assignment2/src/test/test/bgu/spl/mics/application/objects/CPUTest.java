package bgu.spl.mics.application.objects;

import bgu.spl.mics.Future;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.*;

class CPUTest {
//    private static CPU c;
//
//    @BeforeEach
//    public void setUp() {
//        c = new CPU(1);
//    }
//
//    @Test
//    public void getCores() {
//        assertEquals(c.getCores(), 1);
//    }
//
//    @Test
//    public void receiveData() {
//        LinkedList<DataBatch> d = new LinkedList<>();
//        DataBatch db = new DataBatch();
//        d.add(db);
//        c.receiveData(d.getFirst());
//        assertNotNull(c.getDataBatch(), "error : list is null");
//        assertEquals(d.size(), 1);
//    }
//
//    @Test
//    public void process() {
//        assertThrows(Exception.class, () -> c.process(), "list is empty");
//        assertFalse(c.getDataBatch().isEmpty());
//        int size = c.getDataBatch().size();
//        DataBatch d = new DataBatch();
//        c.receiveData(d);
//        c.process();
//        assertEquals(size, c.getDataBatch().size());
//        assertTrue(d.getStatus() == StatusDB.processed);
//    }
}