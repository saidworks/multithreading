/* ADSK  | Product Accesss SZ Q2 Goal (C)2025 */
package com.saidworks.vault;

public class AscendingHackerThread extends HackerThread {
  public AscendingHackerThread(Vault vault) {
    super(vault);
  }

  @Override
  public void run() {
    for (int i = 0; i < MAX_PASSWORD; i++) {
      if (vault.isCorrectPassword(i)) {
        log.info("The {} thread has found the correct password and it is {}", this.getName(), i);
        System.exit(0);
      }
    }
  }
}
