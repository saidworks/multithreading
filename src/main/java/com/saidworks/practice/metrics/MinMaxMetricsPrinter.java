package com.saidworks.practice.metrics;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MinMaxMetricsPrinter extends Thread{
    Logger logger = LogManager.getLogger(MinMaxMetricsPrinter.class);
    private MinMaxMetrics metrics;

    public MinMaxMetricsPrinter(MinMaxMetrics metrics) {
        this.metrics = metrics;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                logger.error(e);
            }
            this.setName("Metrics_REF_" + metrics.getInstanceReference());
            long min = metrics.getMin();
            long max = metrics.getMax();
            logger.info("The current min in the thread ref {} is: {}", this.getName(), min);
            logger.info("The current max in the thread ref {} is: {}", this.getName(), max);
        }
    }
}
