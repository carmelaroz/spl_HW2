package bgu.spl.mics;


import bgu.spl.mics.example.messages.ExampleBroadcast;
import bgu.spl.mics.example.messages.ExampleEvent;
import static org.junit.jupiter.api.Assertions.*;

public class MessageBusImplTest {
    private static MessageBus messageBus;
    private static MicroService microService;
    private static ExampleBroadcast broadcast;
    private ExampleEvent event;

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        messageBus = new MessageBusImpl();
        broadcast = new ExampleBroadcast("exampleBroadcast");
        event = new ExampleEvent("exampleEvent");
    }

    @org.junit.jupiter.api.Test
    void testSubscribeEvent() {
        messageBus.register(microService);
        assertThrows(Exception.class, () -> messageBus.subscribeEvent(ExampleEvent.class, microService), "register failed");
        messageBus.subscribeEvent(ExampleEvent.class, microService);
        assertThrows(Exception.class, () -> messageBus.subscribeEvent(ExampleEvent.class, microService), "subscribe event failed");
        messageBus.unregister(microService);
    }

    @org.junit.jupiter.api.Test
    void subscribeBroadcast() {
        messageBus.register(microService);
        assertThrows(Exception.class, () -> messageBus.subscribeBroadcast(Broadcast.class, microService), "register failed");
        messageBus.subscribeBroadcast(Broadcast.class, microService);
        assertThrows(Exception.class, () -> messageBus.subscribeBroadcast(Broadcast.class, microService), "subscribe broadcast failed");
        messageBus.unregister(microService);
    }

    @org.junit.jupiter.api.Test
    void complete() {
        String s = "some result";
        messageBus.register(microService);
        assertThrows(Exception.class, () -> messageBus.complete(event, s), "register failed");
        messageBus.subscribeEvent(ExampleEvent.class, microService);
        assertThrows(Exception.class, () -> messageBus.complete(event, s), "subscribe event failed");
        messageBus.sendEvent(event);
        assertThrows(Exception.class, () -> messageBus.complete(event, s), "send event failed");
        messageBus.complete(event, s);
        assertThrows(Exception.class, () -> messageBus.complete(event, s), "complete has failed");
        messageBus.unregister(microService);
    }

    @org.junit.jupiter.api.Test
    void sendBroadcast() {
        messageBus.register(microService);
        assertThrows(Exception.class, () -> messageBus.sendBroadcast(broadcast), "register failed");
        messageBus.subscribeBroadcast(broadcast.getClass(), microService);
        assertThrows(Exception.class, () -> messageBus.sendBroadcast(broadcast), "subscribe event failed");
        messageBus.sendBroadcast(broadcast);
        assertThrows(Exception.class, () -> messageBus.sendBroadcast(broadcast), "sent broadcast failed");
        messageBus.unregister(microService);
    }

    @org.junit.jupiter.api.Test
    void sendEvent() {
        messageBus.register(microService);
        assertThrows(Exception.class, () -> messageBus.sendEvent(event), "register failed");
        messageBus.subscribeEvent(ExampleEvent.class, microService);
        assertThrows(Exception.class, () -> messageBus.sendEvent(event), "subscribe event failed");
        messageBus.sendEvent(event);
        assertThrows(Exception.class, () -> messageBus.sendEvent(event), "send event failed");
        messageBus.unregister(microService);
    }

    @org.junit.jupiter.api.Test
    void register() {
        messageBus.register(microService);
        assertThrows(Exception.class, () -> messageBus.register(microService), "register failed");
        messageBus.unregister(microService);
    }

    @org.junit.jupiter.api.Test
    void unregister() {
        messageBus.register(microService);
        assertThrows(Exception.class, () -> messageBus.unregister(microService), "register failed");
        messageBus.unregister(microService);
        assertThrows(Exception.class, () -> messageBus.unregister(microService), "unregister failed");
    }

    @org.junit.jupiter.api.Test
    void awaitMessage() throws InterruptedException {
        assertThrows(Exception.class, () -> messageBus.awaitMessage(microService), "microservice's message is empty");
        messageBus.register(microService);
        assertThrows(Exception.class, () -> messageBus.awaitMessage(microService), "register failed");
        messageBus.subscribeEvent(ExampleEvent.class, microService);
        assertThrows(Exception.class, () -> messageBus.awaitMessage(microService), "subscribe event failed");
        messageBus.sendEvent(event);
        assertThrows(Exception.class, () -> messageBus.awaitMessage(microService), "send event failed");
        assertEquals(messageBus.awaitMessage(microService), event);
    }
}