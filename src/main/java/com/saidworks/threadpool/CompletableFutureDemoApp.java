/* ADSK  | Product Accesss SZ Q2 Goal (C)2025 */
package com.saidworks.threadpool;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CompletableFutureDemoApp {
  private static final Logger logger = LogManager.getLogger();
  private static final Random random = new Random();

  public static void main(String[] args) throws Exception {
    try (ThreadPoolExecutor executor =
        new ThreadPoolExecutor(5, 8, 60, TimeUnit.SECONDS, new ArrayBlockingQueue<>(10))) {
      List<String> listOfStrings =
          RandomStringWithQuestionMark.generateListOfStrings(100, random.nextInt(10, 100));
      List<StringTaskCallable> callables = new ArrayList<>();
      List<CompletableFuture<String>> completableFutures = new ArrayList<>();
      for (String s : listOfStrings) {
        callables.add(new StringTaskCallable(s));
        completableFutures.add(new CompletableFuture<>());
      }
      Future<String> future;
      executor.prestartAllCoreThreads();
      for (AtomicInteger i = new AtomicInteger(0); i.get() < callables.size(); i.set(i.get() + 1)) {

        completableFutures
            .get(i.get())
            .completeAsync(
                () -> {
                  try {
                    String valueResult = callables.get(i.get()).call();
                    logger.info("Future from CompletableFuture {}", valueResult);
                    return valueResult;
                  } catch (Exception e) {
                    Thread.currentThread().interrupt();
                    logger.error(e.getMessage(), e);
                    return String.format("failure {%s}", e.getMessage());
                  } finally {
                    logger.info("thread pool size is {}", executor.getActiveCount());
                  }
                },
                executor);
        future = completableFutures.get(i.get());
        executor.execute(() -> System.out.println(""));
        logger.info("Future from ThreadPoolExecutor {}", future.get());
        logger.info(
            "thread pool size is after completeble future block {}", executor.getActiveCount());
      }
    }
  }
}
