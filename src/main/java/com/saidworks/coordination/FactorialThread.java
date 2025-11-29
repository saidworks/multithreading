/* ADSK  | Product Accesss SZ Q2 Goal (C)2025 */
package com.saidworks.coordination;

import java.math.BigInteger;

public class FactorialThread extends Thread {
  private long number;
  private BigInteger result;
  private boolean isFinished;

  public FactorialThread(long number) {
    this.number = number;
  }

  @Override
  public void run() {
    this.result = factorial(number);
    isFinished = true;
  }

  public BigInteger factorial(long number) {
    BigInteger tmp = BigInteger.ONE;
    for (long i = number; i > 0; i--) {
      tmp = tmp.multiply(BigInteger.valueOf(i));
    }
    return tmp;
  }

  boolean isFinished() {
    return isFinished;
  }

  BigInteger getResult() {
    return result;
  }

  public long getNumber() {
    return number;
  }
}
