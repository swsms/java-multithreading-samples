package org.art.futures;

import java.util.concurrent.*;
import java.util.stream.LongStream;

public class FuturesExample {

    public static void main(String[] args) throws Exception {

        final ExecutorService service = Executors.newSingleThreadExecutor();

        final long start = 1;
        final long end = 100_000_000;
        final Callable<Long> sumCounter = () -> LongStream.rangeClosed(start, end).sum();

        final Future<Long> sumResult = service.submit(sumCounter);

        System.out.println(sumResult.isDone() ? sumResult.get() : "No any result yet!" );

        System.out.println(sumResult.get());

        service.shutdown();
    }
}
