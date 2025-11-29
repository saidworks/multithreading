/* ADSK  | Product Accesss SZ Q2 Goal (C)2025 */
package com.saidworks.coordination;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Scanner;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MainApp {
  private static final Logger log = LogManager.getLogger(MainApp.class.getName());

  public static void main(String[] args) {
    WaitingForUserInput waitingForUserInput = new WaitingForUserInput();
    Thread thread2 = new Thread(waitingForUserInput);
    thread2.setName("InputWaitingThread");
    thread2.setPriority(Thread.MAX_PRIORITY);
    thread2.start();

    blockingExemple();
    longComputations();
  }

  static void blockingExemple() {
    BlockingTask bt = new BlockingTask();
    bt.start();
    bt.interrupt();
  }

  static void longComputations() {
    LongComputationTask longComputationTask =
        new LongComputationTask(new BigDecimal("200000"), new BigInteger("4343445"));
    Thread thread = new Thread(longComputationTask);
    thread.setPriority(Thread.MIN_PRIORITY);
    thread.start();
    thread.interrupt();

    LongComputationTaskDaemon taskDaemon =
        new LongComputationTaskDaemon(
            new BigDecimal("1334445452323"), new BigInteger("245435345332424"));
    Thread thread1 = new Thread(taskDaemon);
    thread1.setDaemon(true);
    thread1.setPriority(Thread.MIN_PRIORITY);
    thread1.start();
  }

  private static class WaitingForUserInput implements Runnable {
    @Override
    public void run() {
      while (true) {
        log.info("waiting for user input");
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        if (input.equals("quit")) {
          return;
        }
      }
    }
  }
}
