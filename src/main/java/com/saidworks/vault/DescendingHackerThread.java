/* ADSK  | Product Accesss SZ Q2 Goal (C)2025 */
package com.saidworks.vault;

public class DescendingHackerThread extends HackerThread {
  public DescendingHackerThread(Vault vault) {
    super(vault);
  }

  @Override
  public void run() {
    log.info("The {} thread has started", this.getName());
    for (int guess = MAX_PASSWORD; guess > 0; guess--) {
      if (vault.isCorrectPassword(guess)) {
        log.info("Thread {} has found the correct password and it is {}", this.getName(), guess);
        System.exit(0);
      }
    }
  }
}
