package bgu.spl.mics;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

class FutureTest {
    private static Future<String> future;

    @BeforeEach
    public void setUp() {
        future = new Future<String>();
    }

    @Test
    public void get() {
        assertThrows(Exception.class, () -> future.get());
        String str = "someValue";
        future.resolve(str);
        try {
            assertNotNull(future.get(), "got null instead");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            assertEquals(future.get(),str);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void resolve() {
        String str = "SomeValue";
        future.resolve(str);
        assertTrue(future.isDone());
        try {
            assertTrue(str.equals(future.get()));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void isDone() {
        assertFalse(future.isDone());
        String str = "someValue";
        future.resolve(str);
        assertTrue(future.isDone());
    }

    @Test
    public void testGet() {
        long currentTime = System.currentTimeMillis();
        assertNull(future.get(1000, TimeUnit.MILLISECONDS));
        assertTrue(System.currentTimeMillis() - currentTime >= 1000);
        assertFalse(future.isDone());
        String str = "someValue";
        future.resolve(str);
        assertTrue(future.isDone());
        assertTrue(str.equals(future.get(1000, TimeUnit.MILLISECONDS)));
    }
}