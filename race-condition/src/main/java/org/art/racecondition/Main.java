package org.art.racecondition;

import org.art.racecondition.counter.BadCounter;
import org.art.racecondition.counter.Counter;
import org.art.racecondition.counter.IntLockFreeCounter;
import org.art.racecondition.counter.IntLockingCounter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {
    private static final Logger LOG = LoggerFactory.getLogger(Main.class);

    private static final int N_THREADS = 10;
    private static final int N_ITERATIONS = 100_000;

    public static void main(String[] args) throws InterruptedException {
        final int expectedValue = N_THREADS * N_ITERATIONS;

        Counter<Integer> badCounter = new BadCounter();
        Counter<Integer> lockingCounter = new IntLockingCounter();
        Counter<Integer> lockFreeCounter = new IntLockFreeCounter();

        List<Thread> threads = Stream
                .generate(() -> new Thread(() -> {
                    for (int j = 0; j < N_ITERATIONS; j++) {
                        badCounter.incrementAndGet();
                        lockingCounter.incrementAndGet();
                        lockFreeCounter.incrementAndGet();
                    }
                }))
                .limit(N_THREADS)
                .collect(Collectors.toList());

        for (Thread thread : threads) thread.start();
        for (Thread thread : threads) thread.join();

        LOG.info("Bad Counter: expected " + expectedValue + ", actual " + badCounter.get());
        LOG.info("Locking counter: expected " + expectedValue + ", actual " + lockingCounter.get());
        LOG.info("Lock free counter: expected " + expectedValue + ", actual " + lockFreeCounter.get());
    }
}
