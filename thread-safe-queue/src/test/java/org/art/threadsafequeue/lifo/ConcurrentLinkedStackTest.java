package org.art.threadsafequeue.lifo;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.*;
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
    public void testPushAndPop_whenMultipleThreads() throws Exception {

        final Stack<Integer> stack = new ConcurrentLinkedStack<>();
        final Random random = new Random();
        final int numberOfWriterThreads = 4;
        final int numberOfReaderThreads = 1;
        final int iterations = 1_000;

        List<Thread> writerThreads = Stream
                .generate(() -> new Thread(() -> {
                    for (int i = 0; i < iterations; i++) {
                        stack.push(random.nextInt(100));
                    }
                }))
                .limit(numberOfWriterThreads)
                .collect(Collectors.toList());

        List<Thread> readerThreads = Stream
                .generate(() -> new Thread(() -> {
                    for (int i = 0; i < iterations; i++) {
                        stack.pop();
                    }
                }))
                .limit(numberOfReaderThreads)
                .collect(Collectors.toList());

        List<Thread> threads = new ArrayList<>();
        threads.addAll(writerThreads);
        threads.addAll(readerThreads);

        Collections.shuffle(threads);

        for (Thread t : threads) t.start();
        for (Thread t : threads) t.join();

        int expectedSize = (numberOfWriterThreads - numberOfReaderThreads) * iterations;

        LOG.info("actual size {} expected size {}", stack.size(), expectedSize);
        assertEquals(stack.size(), expectedSize);
    }

}