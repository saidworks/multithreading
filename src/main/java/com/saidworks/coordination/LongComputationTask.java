/* ADSK  | Product Accesss SZ Q2 Goal (C)2025 */
package com.saidworks.coordination;

import java.math.BigDecimal;
import java.math.BigInteger;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LongComputationTask implements Runnable {
  private BigDecimal base;
  private BigInteger power;

  Logger log = LogManager.getLogger(LongComputationTask.class.getName());

  public LongComputationTask(BigDecimal base, BigInteger power) {
    this.base = base;
    this.power = power;
  }

  @Override
  public void run() {
    log.trace("Running LongComputationTask");
    log.info("{}^{}={}", base, power, pow(base, power));
  }

  protected BigDecimal pow(BigDecimal base, BigInteger exponent) {
    BigDecimal result = BigDecimal.ONE;
    for (BigInteger i = BigInteger.ONE; i.compareTo(exponent) != 0; i.add(BigInteger.ONE)) {
      if (Thread.currentThread().isInterrupted()) {
        log.error("{}  Interrupted", LongComputationTask.class.getSimpleName());
        return BigDecimal.ZERO;
      }
      result = result.multiply(base);
    }
    return result;
  }
}
