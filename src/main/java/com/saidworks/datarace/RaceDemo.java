/* ADSK  | Product Accesss SZ Q2 Goal (C)2025 */
package com.saidworks.datarace;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RaceDemo {
  private static final Logger log = LogManager.getLogger(RaceDemo.class);

  public static void main(String[] args) {
    SharedClass sharedClass = new SharedClass();
    Thread thread1 =
        new Thread(
            () -> {
              for (int i = 0; i < Integer.MAX_VALUE; i++) {
                sharedClass.increment();
              }
            });

    Thread thread2 =
        new Thread(
            () -> {
              for (int i = 0; i < Integer.MAX_VALUE; i++) {
                sharedClass.checkForDataRace();
              }
            });

    thread1.start();
    thread2.start();
  }

  public static class SharedClass {
    // we can use volatile as well instead of lock
    private int x = 0;
    private int y = 0;
    private final Object lock = new Object();

    public void increment() {
      synchronized (lock) {
        x++;
        y++;
      }
    }

    public void checkForDataRace() {
      synchronized (lock) {
        if (y > x) {
          log.error("y > x - Data Race is detected");
        } else if (x > y) {
          log.error("x > y - Data Race is detected");
        }
      }
    }
  }
}
