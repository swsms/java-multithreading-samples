package org.art.runthreads;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {
    private static final Logger LOG = LoggerFactory.getLogger(Main.class);

    private static final int N_THREADS = 10;
    private static final int DEFAULT_WAITING = 1000;

    public static void main(String[] args) {

        final int numberOfThreads = N_THREADS;
        LOG.info("Running {} threads", numberOfThreads);

        List<Thread> threads = Stream
                .generate(() -> new Thread(() -> {
                    int time = DEFAULT_WAITING + (new Random()).nextInt(DEFAULT_WAITING);
                    LOG.info("Thread {} will work {} ms", Thread.currentThread().getName(), time);
                    try {
                        Thread.sleep(time);
                        LOG.info("Thread {} successfully finished the work", Thread.currentThread().getName());
                    } catch (InterruptedException e) {
                        LOG.error("{} interrupted", e, Thread.currentThread().getName());
                    }
                }))
                .limit(numberOfThreads)
                .collect(Collectors.toList());

        threads.forEach(Thread::start);

        threads.forEach((t) -> {
            try {
                t.join();
            } catch (InterruptedException e) {
                LOG.error("Can't join the thread {}", e, t.getName());
            }
        });
    }
}
