/* ADSK  | Product Accesss SZ Q2 Goal (C)2025 */
package com.saidworks.vault;

import org.apache.logging.log4j.*;

public abstract class HackerThread extends Thread {
  public static final int MAX_PASSWORD = 9999;
  Logger log = LogManager.getFormatterLogger(HackerThread.class.getName());
  protected Vault vault;

  public HackerThread(Vault vault) {
    this.vault = vault;
    this.setName(this.getClass().getSimpleName());
    this.setPriority(Thread.MIN_PRIORITY);
  }

  @Override
  public void start() {
    log.info("The {} thread has started", this.getName());
    super.start();
  }
}
