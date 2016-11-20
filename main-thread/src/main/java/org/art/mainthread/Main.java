package org.art.mainthread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {
    private static final Logger LOG = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        LOG.info("Displaying main thread info");

        ThreadInfo mainThreadInfo = new ThreadInfo(
                Thread.currentThread().getId(),
                Thread.currentThread().getName(),
                Thread.currentThread().getState()
        );

        LOG.info("Main thread info: {}", mainThreadInfo.toString());
    }
}
