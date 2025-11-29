/* ADSK  | Product Accesss SZ Q2 Goal (C)2025 */
package com.saidworks.datarace;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RaceDemo2 {
  public static void main(String[] args) throws InterruptedException {
    SharedClass sharedClass = new SharedClass();

    for (int i = 0; i < 10; i++) {
      Thread thread1 = new Thread(sharedClass::method1);
      Thread thread2 = new Thread(sharedClass::method2);

      thread1.start();
      thread2.start();
      thread1.join();
      thread2.join();
      Thread.sleep(2000);
    }
  }

  private static class SharedClass {
    Logger logger = LogManager.getLogger(SharedClass.class);
    int a = 0;
    int b = 0;

    public void method1() {
      int local1 = a;
      this.b = 1;
      logger.info("local1 = " + local1);
      logger.info("b = " + b);
    }

    public void method2() {
      int local2 = b;
      this.a = 2;
      logger.info("local2 = " + local2);
      logger.info("a = " + a);
    }
  }
}
