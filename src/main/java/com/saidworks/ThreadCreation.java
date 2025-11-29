/* ADSK  | Product Accesss SZ Q2 Goal (C)2025 */
package com.saidworks;

import org.apache.logging.log4j.*;

public class ThreadCreation {
  private static final Logger logger = LogManager.getLogger(ThreadCreation.class);

  public static void main(String[] args) throws InterruptedException {
    Thread thread1 =
        new Thread(
            new Runnable() {
              @Override
              public void run() {
                logger.info(
                    "we are now in the thread number 1 {}", Thread.currentThread().getName());
                logger.info("The priority for thread 1 {}", Thread.currentThread().getPriority());
              }
            });
    thread1.setPriority(Thread.MAX_PRIORITY);
    logger.info(
        "We are in the main thread {} before starting thread 1", Thread.currentThread().getName());
    thread1.start();
    logger.info(
        "We are in the main thread {} after starting thread 1", Thread.currentThread().getName());
  }
}
