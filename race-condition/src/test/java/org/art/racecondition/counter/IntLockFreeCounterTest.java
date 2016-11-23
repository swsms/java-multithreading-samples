package org.art.racecondition.counter;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class IntLockFreeCounterTest extends Assert {

    @Test
    public void testIncrementAndGet_whenSingleThread() throws Exception {
        Counter<Integer> counter = new IntLockFreeCounter();
        assertEquals(counter.get().intValue(), 0);

        final int n = 10_000_000;
        for (int i = 0; i < n; i++) {
            counter.incrementAndGet();
        }

        assertEquals(counter.get().intValue(), n);
    }


    @Test
    public void testIncrementAndGet_whenMultipleThreads() throws Exception {
        Counter<Integer> counter = new IntLockFreeCounter();
        assertEquals(counter.get().intValue(), 0);

        final int iterations = 1_000_000;
        final int threads = 10;

        List<Thread> threadList = Stream
                .generate(() -> new Thread(() -> {
                    for (int i = 0; i < iterations; i++) {
                        counter.incrementAndGet();
                    }
                }))
                .limit(threads)
                .collect(Collectors.toList());

        for (Thread t : threadList) t.start();
        for (Thread t : threadList) t.join();

        assertEquals(counter.get().intValue(), iterations * threads);
    }
}