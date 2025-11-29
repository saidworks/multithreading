/* ADSK  | Product Accesss SZ Q2 Goal (C)2025 */
package com.saidworks;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Inheritence {
  public static void main(String[] args) {
    Logger log = LogManager.getLogger(Inheritence.class.getName());
    // lambda declaration
    Thread t1 =
        new Thread(
            () -> {
              log.info("In thread 1");
            });
    // inheritence
    Thread t2 = new CoolThread();
    t1.setPriority(Thread.MAX_PRIORITY);
    t2.setPriority(Thread.MIN_PRIORITY);
    t1.start();
    t2.start();
  }

  public static class CoolThread extends Thread {
    Logger log = LogManager.getLogger(CoolThread.class.getName());

    @Override
    public void run() {
      log.info("A thread created by inheritance");
    }

    @Override
    public void start() {
      super.start();
      log.info("The cool thread has started");
    }
  }
}
