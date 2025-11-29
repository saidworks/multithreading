/* ADSK  | Product Accesss SZ Q2 Goal (C)2025 */
package com.saidworks.coordination;

import java.math.BigInteger;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ComplexCalculation {
  Logger log = LogManager.getLogger(ComplexCalculation.class.getName());

  public BigInteger calculateResult(
      BigInteger base1, BigInteger power1, BigInteger base2, BigInteger power2)
      throws InterruptedException {
    BigInteger result;
    PowerCalculatingThread t1 = new PowerCalculatingThread(base1, power1);
    PowerCalculatingThread t2 = new PowerCalculatingThread(base2, power2);
    t1.setName("First Thread");
    t2.setName("Second Thread");
    t1.join();
    t2.join();
    t1.start();
    t2.start();
    result = t1.getResult().add(t2.getResult());
    /*
        Calculate result = ( base1 ^ power1 ) + (base2 ^ power2).
        Where each calculation in (..) is calculated on a different thread
    */
    return result;
  }

  private static class PowerCalculatingThread extends Thread {
    Logger log = LogManager.getLogger(PowerCalculatingThread.class.getName());

    private BigInteger result = BigInteger.ONE;
    private BigInteger base;
    private BigInteger power;

    public PowerCalculatingThread(BigInteger base, BigInteger power) {
      this.base = base;
      this.power = power;
    }

    @Override
    public void run() {
      log.info("Starting Power Calculation Thread {}", currentThread().getName());
      calculateResult();
      log.info("Finished Power Calculation Thread {}", currentThread().getName());
    }

    private BigInteger calculateResult() {
      result = BigInteger.ONE;
      for (int i = 1; i <= power.intValue(); i++) {
        result = result.multiply(base);
      }
      return result;
    }

    public BigInteger getResult() {
      return result;
    }
  }
}
