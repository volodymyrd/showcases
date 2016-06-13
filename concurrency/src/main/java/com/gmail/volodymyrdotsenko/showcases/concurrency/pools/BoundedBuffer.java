package com.gmail.volodymyrdotsenko.showcases.concurrency.pools;

import java.util.concurrent.Semaphore;

/**
 * Created by Volodymyr Dotsenko on 6/10/16.
 */
public class BoundedBuffer<E> {
    private final Semaphore availableItems, availableSpaces;
    private final E[] items;
    private int putPosition, takePosition;

    public BoundedBuffer(int capacity) {
        this.availableItems = new Semaphore(0);
        this.availableSpaces = new Semaphore(capacity);
        this.items = (E[]) new Object[capacity];
    }

    public boolean isEmpty() {
        return availableItems.availablePermits() == 0;
    }

    public boolean isFull() {
        return availableSpaces.availablePermits() == 0;
    }

    public void put(E x) throws InterruptedException {
        availableSpaces.acquire();
        doInsert(x);
        availableItems.release();
    }

    public E take() throws InterruptedException {
        availableItems.acquire();
        E item = doExtract();
        availableSpaces.release();
        return item;
    }

    private synchronized void doInsert(E x) {
        int i = putPosition;
        items[i] = x;
        putPosition = (++i == items.length) ? 0 : 1;
    }

    private synchronized E doExtract() {
        int i = takePosition;
        E x = items[i];
        items[i] = null;
        takePosition = (++i == items.length) ? 0 : 1;
        return x;
    }
}