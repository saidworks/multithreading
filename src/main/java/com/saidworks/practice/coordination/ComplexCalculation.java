package com.saidworks.practice;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigInteger;

public class ComplexCalculation {
    private static final Logger logger = LogManager.getLogger(ComplexCalculation.class);

    public static void main(String[] args) throws InterruptedException {
        long start = System.currentTimeMillis();
        calculateResult(new BigInteger("50"),new BigInteger("50"),new BigInteger("60"),new BigInteger("60"));
        long end = System.currentTimeMillis();
        logger.info("Execution {}ms",end - start);
    }
    private static BigInteger calculateResult(BigInteger base1, BigInteger power1, BigInteger base2, BigInteger power2)  {
        BigInteger result;
        PowerCalculatingThread t1 = new PowerCalculatingThread(base1, power1);
        PowerCalculatingThread t2 = new PowerCalculatingThread(base2, power2);
        t1.setName("First Thread");
        t2.setName("Second Thread");
        t1.start();
        t2.start();
        try {
            t1.join();
            t2.join();
            }
        catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        t1.setDaemon(true);
        t2.setDaemon(true);
        var result1 = t1.getResult();
        var result2 = t2.getResult();

        result = result1.add(result2);
        logger.info("total result: {}", result);

        return result;
    }

    private static class PowerCalculatingThread extends Thread {
        Logger logger = LogManager.getLogger(PowerCalculatingThread.class);
        private BigInteger result = BigInteger.ONE;
        private BigInteger base;
        private BigInteger power;

        public PowerCalculatingThread(BigInteger base, BigInteger power) {
            this.base = base;
            this.power = power;
        }

        @Override
        public void run() {
            this.result = calculateResult();
            logger.info("{result is {}}", this.result);
        }

        private BigInteger calculateResult() {
            BigInteger tmp = BigInteger.ONE;
            for (BigInteger i = BigInteger.ZERO; i.compareTo(power) != 0; i=i.add(BigInteger.ONE)) {
                tmp = tmp.multiply(base);
            }
            return tmp;
        }

        public BigInteger getResult() { return result; }
    }
}