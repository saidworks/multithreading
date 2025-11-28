/* Said Zitouni (C)2025 */
package com.saidworks.practice.metrics;

import java.util.Random;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class BusinessLogic extends Thread {
    private static final Logger log = LogManager.getLogger(BusinessLogic.class.getName());
    private Metrics metrics;
    private final Random random = new Random();

    public BusinessLogic(Metrics metrics) {
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
