package org.art.threadsafequeue.lifo;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ConcurrentLinkedStackTest extends Assert {
    private static final Logger LOG = LoggerFactory.getLogger(ConcurrentLinkedStackTest.class);

    @Test
    public void testPush_whenSingleThread() throws Exception {
        Stack<Integer> stack = new ConcurrentLinkedStack<>();
        assertEquals(stack.size(), 0);

        stack.push(11);
        stack.push(22);
        stack.push(33);
        stack.push(44);
        stack.push(55);
        stack.push(44);

        assertEquals(stack.size(), 6);
    }

    @Test
    public void testPop_whenSingleThread() throws Exception {
        Stack<Integer> stack = new ConcurrentLinkedStack<>();
        assertEquals(stack.pop(), null);

        stack.push(22);
        stack.push(33);
        stack.push(44);

        assertEquals(stack.pop(), Integer.valueOf(44));
        assertEquals(stack.pop(), Integer.valueOf(33));
        assertEquals(stack.pop(), Integer.valueOf(22));

        assertEquals(stack.pop(), null);
    }

    @Test
    public void testPeek_whenSingleThread() throws Exception {
        Stack<Integer> stack = new ConcurrentLinkedStack<>();
        assertEquals(stack.peek(), null);

        stack.push(11);
        stack.push(5);

        assertEquals(stack.peek(), Integer.valueOf(5));
        assertEquals(stack.peek(), Integer.valueOf(5));

        stack.pop();

        assertEquals(stack.peek(), Integer.valueOf(11));

        stack.pop();

        assertEquals(stack.peek(), null);
    }

    @Test
    public void testPush_whenMultipleThreads() throws Exception {

        final Stack<Integer> stack = new ConcurrentLinkedStack<>();
        final Random random = new Random();
        final int numberOfThreads = 10;
        final int iterations = 10_000;

        assertEquals(stack.peek(), null);
        assertEquals(stack.size(), 0);

        List<Thread> threads = Stream
                .generate(() -> new Thread(() -> {
                    for (int i = 0; i < iterations; i++) {
                        stack.push(random.nextInt(10000));
                    }
                }))
                .limit(numberOfThreads)
                .collect(Collectors.toList());

        for (Thread t : threads) t.start();
        for (Thread t : threads) t.join();

        assertEquals(stack.size(), iterations * numberOfThreads);
    }

    @Test
    public void testPop_whenMultipleThreads() throws Exception {

        final Stack<Integer> stack = new ConcurrentLinkedStack<>();
        final Random random = new Random();

        final int W_THREADS = 10;
        final int R_THREADS = 8;
        final int W_ITER = 10_000;
        final int R_ITER = 10_000;

        final AtomicInteger nullCounter = new AtomicInteger(0);

        List<Thread> writerThreads = Stream
                .generate(() -> new Thread(() -> {
                    for (int i = 0; i < W_ITER; i++) {
                        stack.push(random.nextInt(10000));
                    }
                }))
                .limit(W_THREADS)
                .collect(Collectors.toList());

        List<Thread> readerThreads = Stream
                .generate(() -> new Thread(() -> {
                    for (int i = 0; i < R_ITER; i++) {
                        Integer val = stack.pop();
                        if (val == null) {
                            nullCounter.incrementAndGet();
                        }
                    }
                }))
                .limit(R_THREADS)
                .collect(Collectors.toList());

        List<Thread> threads = Stream.concat(
                readerThreads.stream(),
                writerThreads.stream())
                .collect(Collectors.toList());

        Collections.shuffle(threads);

        for (Thread t : threads) t.start();
        for (Thread t : threads) t.join();

        // the expected stack size is calculated as
        // total number of push - total number of pop + count of empty pop (excess),
        // because size can't be negative (R * N - W * M + E = 0)
        int expected = W_THREADS * W_ITER - R_THREADS * R_ITER + nullCounter.get();
        LOG.info("actual stack size {} expected stack size {}", stack.size(), expected);

        assertEquals(stack.size(), expected);
    }

}