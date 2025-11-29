/* ADSK  | Product Accesss SZ Q2 Goal (C)2025 */
package com.saidworks.coordination;

import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class JoiningThreads {
  static final Logger logger = LogManager.getLogger(JoiningThreads.class.getName());

  public static void main(String[] args) throws InterruptedException {
    List<Long> nbrs = new ArrayList<>(List.of(342L, 23L, 45L, 75L, 465L, 4937L));
    List<FactorialThread> factorialThreads = new ArrayList<>();
    for (int i = 0; i < nbrs.size(); i++) {
      factorialThreads.add(new FactorialThread(nbrs.get(i)));
    }
    for (FactorialThread factorialThread : factorialThreads) {
      factorialThread.setDaemon(true);
      factorialThread.start();
      factorialThread.join(2000);
    }

    for (FactorialThread factorialThread : factorialThreads) {
      if (factorialThread.isFinished()) {
        logger.info(
            "Factorial thread finished number is {} result is {}",
            factorialThread.getNumber(),
            factorialThread.getResult());
      } else {
        logger.info("Factorial for number is still working {}", factorialThread.getNumber());
      }
    }
  }
}
