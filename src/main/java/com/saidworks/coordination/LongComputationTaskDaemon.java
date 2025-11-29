/* ADSK  | Product Accesss SZ Q2 Goal (C)2025 */
package com.saidworks.coordination;

import java.math.BigDecimal;
import java.math.BigInteger;

public class LongComputationTaskDaemon extends LongComputationTask {
  private BigDecimal base;
  private BigInteger power;

  public LongComputationTaskDaemon(BigDecimal base, BigInteger power) {
    super(base, power);
    this.base = base;
    this.power = power;
  }

  @Override
  public void run() {
    log.trace("Running LongComputationTask");
    log.info("{}^{}={}", base, power, pow(base, power));
  }

  @Override
  protected BigDecimal pow(BigDecimal base, BigInteger exponent) {
    BigDecimal result = BigDecimal.ONE;
    for (BigInteger i = BigInteger.ONE; i.compareTo(exponent) != 0; i.add(BigInteger.ONE)) {
      log.trace("{}^{}={}", base, power, i);
      result = result.multiply(base);
    }
    return result;
  }
}
