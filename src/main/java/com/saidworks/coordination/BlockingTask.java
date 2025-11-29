/* ADSK  | Product Accesss SZ Q2 Goal (C)2025 */
package com.saidworks.coordination;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class BlockingTask extends Thread {
  Logger log = LogManager.getLogger(BlockingTask.class.getName());

  @Override
  public void run() {

    Thread.ofPlatform()
        .start(
            () -> {
              var bt = Thread.currentThread();
              bt.setName("BlockingTask");
              try {
                synchronized (bt) {
                  bt.wait(10000);
                  log.error(
                      "current thread is interrupted {} otherwise it will run forever",
                      bt.getName());
                }
              } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
              }
            });
  }
}
