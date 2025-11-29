/* ADSK  | Product Accesss SZ Q2 Goal (C)2025 */
package com.saidworks.vault;

import org.apache.logging.log4j.*;

public record Vault(int password) {
  public boolean isCorrectPassword(int guess) {
    Logger log = LogManager.getFormatterLogger(Vault.class.getName());
    try {
      Thread.sleep(5);
    } catch (InterruptedException e) {
      log.error("Vault thread has been interrupted because of exception {}", e.getMessage());
    }
    return password == guess;
  }
}
