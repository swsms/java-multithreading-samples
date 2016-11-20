package org.art.threadsafequeue.lifo;

import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentLinkedDeque;

public class ConcurrentLinkedStack<T> implements Stack<T> {
    private LinkedList<T> list;

    public ConcurrentLinkedStack() {
        this.list = new LinkedList<>();
    }

    @Override
    public synchronized void push(T elem) {
        list.addFirst(elem);
    }

    @Override
    public synchronized T pop() {
        try {
            return list.removeFirst();
        } catch (NoSuchElementException e) {
            return null;
        }
    }

    @Override
    public synchronized T peek() {
        try {
            return list.peekFirst();
        } catch (NoSuchElementException e) {
            return null;
        }
    }

    @Override
    public synchronized int size() {
        return list.size();
    }
}
