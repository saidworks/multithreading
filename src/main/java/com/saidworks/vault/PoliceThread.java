/* ADSK  | Product Accesss SZ Q2 Goal (C)2025 */
package com.saidworks.vault;

import org.apache.logging.log4j.*;

public class PoliceThread extends Thread {
  Logger logger = LogManager.getLogger(PoliceThread.class);

  @Override
  public void run() {
    for (int i = 10; i > 0; i--) {
      Thread.startVirtualThread(
          () -> {
            try {
              Thread.sleep(1000);
            } catch (InterruptedException e) {
              Thread.currentThread().interrupt();
            }
          });
      logger.info(i);
    }
    logger.info("we caught all hackers checkmate!");
    System.exit(0);
  }
}
