/* ADSK  | Product Accesss SZ Q2 Goal (C)2025 */
package com.saidworks.deadlock;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DeadlockExample {
  private static final Logger logger = LogManager.getLogger();
  private static final Object lock1 = new Object();
  private static final Object lock2 = new Object();

  public static void main(String[] args) {
    Thread thread1 =
        new Thread(
            () -> {
              synchronized (lock1) {
                logger.info("Thread 1: Holding lock 1...");
                try {
                  Thread.sleep(100);
                } catch (InterruptedException e) {
                }
                logger.info("Thread 1: Waiting for lock 2...");
                synchronized (lock2) {
                  logger.info("Thread 1: Holding lock 1 and lock 2...");
                }
              }
            });

    Thread thread2 =
        new Thread(
            () -> {
              synchronized (lock2) {
                logger.info("Thread 2: Holding lock 2...");
                try {
                  Thread.sleep(100);
                } catch (InterruptedException e) {
                }
                logger.info("Thread 2: Waiting for lock 1...");
                synchronized (lock1) {
                  logger.info("Thread 2: Holding lock 1 and lock 2...");
                }
              }
            });

    thread1.start();
    thread2.start();
  }
}
