package com.saidworks.practice.metrics;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Random;

public class BusinessLogicMinMax implements Runnable {
    private static final Logger log = LogManager.getLogger(BusinessLogicMinMax.class.getName());
    private MinMaxMetrics metrics;
    private final Random random = new Random();

    public BusinessLogicMinMax(MinMaxMetrics metrics) {
        this.metrics = metrics;
    }

    @Override
    public void run() {
        while (true) {
            long startTime = System.currentTimeMillis();
            try {
                Thread.sleep(random.nextInt(2));

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                log.error(e);
            }
            long endTime = System.currentTimeMillis();
            log.info("execution took : {}ms ", endTime - startTime);
            metrics.addSample(endTime - startTime);
        }
    }
}
