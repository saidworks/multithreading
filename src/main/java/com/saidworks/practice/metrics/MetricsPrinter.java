/* Said Zitouni (C)2025 */
package com.saidworks.practice.metrics;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MetricsPrinter extends Thread {
    Logger logger = LogManager.getLogger(MetricsPrinter.class);
    private Metrics metrics;

    public MetricsPrinter(Metrics metrics) {
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
            double currentAverage = metrics.getAverage();
            this.setName("Metrics_REF_" + metrics.getInstanceReference());
            logger.info("The current average in the thread ref {} is: {}", this.getName(), currentAverage);
        }
    }
}
