package org.art.runningthreads;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {
    private static final Logger LOG = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {

        final int numberOfThreads = 10;
        LOG.info("Running {} threads", numberOfThreads);

        List<Thread> threads = Stream
                .generate(() -> new Thread(() -> {
                    int time = 1000 + (new Random()).nextInt(1000);
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
                LOG.error("Can't join to thread {}", e, t.getName());
            }
        });
    }
}
