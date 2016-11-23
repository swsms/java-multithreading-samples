package org.art.racecondition.counter;

public interface Counter<T> {

    T incrementAndGet();

    T get();
}
