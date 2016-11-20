package org.art.threadsafequeue.lifo;

public interface Stack<T> {

    void push(T elem);

    T pop();

    T peek();

    int size();
}
