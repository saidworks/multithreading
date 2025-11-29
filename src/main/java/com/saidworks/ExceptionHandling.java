/* ADSK  | Product Accesss SZ Q2 Goal (C)2025 */
package com.saidworks;

import org.apache.logging.log4j.*;
import org.apache.logging.log4j.Logger;

public class ExceptionHandling {
  private static final Logger logger = LogManager.getLogger(ExceptionHandling.class.getName());

  public static void main(String[] args) {
    Thread t1 =
        new Thread(
            new Runnable() {
              @Override
              public void run() {
                throw new RuntimeException(
                    "Unexpected exception brought down " + Thread.currentThread().getName());
              }
            });
    t1.setName("bad thread");
    t1.setUncaughtExceptionHandler(
        new Thread.UncaughtExceptionHandler() {
          @Override
          public void uncaughtException(Thread t, Throwable e) {
            logger.info(
                "A critical error occured at {} and brought down the whole thread because of error {} caused by {}",
                t.getName(),
                e.getMessage(),
                e.getCause());
          }
        });
    t1.start();
  }
}
