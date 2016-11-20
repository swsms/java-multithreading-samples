package org.art.threadsafequeue.lifo;

import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentLinkedDeque;

public class ConcurrentLinkedStack<T> implements Stack<T> {
    private ConcurrentLinkedDeque<T> deque;

    public ConcurrentLinkedStack() {
        this.deque = new ConcurrentLinkedDeque<>();
    }

    @Override
    public void push(T elem) {
        deque.addFirst(elem);
    }

    @Override
    public T pop() {
        try {
            return deque.removeFirst();
        } catch (NoSuchElementException e) {
            return null;
        }
    }

    @Override
    public T peek() {
        try {
            return deque.peekFirst();
        } catch (NoSuchElementException e) {
            return null;
        }
    }

    @Override
    public synchronized int size() {
        return deque.size();
    }
}
