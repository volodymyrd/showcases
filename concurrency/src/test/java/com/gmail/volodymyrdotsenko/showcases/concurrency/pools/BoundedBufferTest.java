package com.gmail.volodymyrdotsenko.showcases.concurrency.pools;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Volodymyr Dotsenko on 6/10/16.
 */
public class BoundedBufferTest {

    @Test
    public void testIsEmptyWhenConstructed() {
        BoundedBuffer<Integer> boundedBuffer = new BoundedBuffer<>(10);
        assertTrue(boundedBuffer.isEmpty());
        assertFalse(boundedBuffer.isFull());
    }

    @Test
    public void testIsFullAfterPuts() throws InterruptedException {
        BoundedBuffer<Integer> boundedBuffer = new BoundedBuffer<>(10);
        for (int i = 0; i < 10; i++)
            boundedBuffer.put(i);

        assertTrue(boundedBuffer.isFull());
        assertFalse(boundedBuffer.isEmpty());
    }

    @Test
    public void testTakeBlocksWhenEmpty() {
        final BoundedBuffer<Integer> boundedBuffer = new BoundedBuffer<>(10);
        Thread taker = new Thread() {
            @Override
            public void run() {
                try {
                    int unused = boundedBuffer.take();
                    fail();
                } catch (InterruptedException succsess) {
                }
            }
        };
        try {
            taker.start();
            Thread.sleep(3000);
            taker.interrupt();
            taker.join(3000);
            assertFalse(taker.isAlive());
        } catch (Exception unexpected) {
            fail();
        }
    }
}