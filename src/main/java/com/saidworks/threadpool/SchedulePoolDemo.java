/* ADSK  | Product Accesss SZ Q2 Goal (C)2025 */
package com.saidworks.threadpool;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SchedulePoolDemo {
  private static final Logger logger = LogManager.getLogger();
  private static final Random random = new Random();
  private static final ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(3);
  private static AtomicReference<String> atomicReference = new AtomicReference<>("default");

  public static void main(String[] args) throws ExecutionException, InterruptedException {
    List<String> listOfStrings =
        RandomStringWithQuestionMark.generateListOfStrings(10, random.nextInt(10, 100));
    logger.info("List of Strings: {}", listOfStrings);
    // Create a list of StringTaskCallable instances
    List<StringTaskCallable> callables = new ArrayList<>();

    for (String s : listOfStrings) {
      callables.add(new StringTaskCallable(s));
    }
    for (StringTaskCallable callable : callables) {
      callable.setDaemon(true);
      ScheduledFuture<String> scheduledFuture =
          executor.schedule((Callable<String>) callable, random.nextInt(1, 2), TimeUnit.SECONDS);
      String result = scheduledFuture.get();

      if (atomicReference.compareAndSet("default", result)) {
        logger.info("Scheduled Future: {}", atomicReference);
        logger.info(
            "Scheduled Future is done: {}, thread pool size is {}",
            scheduledFuture.isDone(),
            executor.getActiveCount());
        logger.info("Scheduled future queue size: {}", executor.getQueue().size());
        logger.info(
            "Scheduled future remaining capacity: {}", executor.getQueue().remainingCapacity());
        logger.info("Scheduled future task count: {}", executor.getTaskCount());
        logger.info("Scheduled future completed task count: {}", executor.getCompletedTaskCount());
        logger.info("Scheduled future largest pool size: {}", executor.getLargestPoolSize());
        atomicReference.set("default");
      }
    }
  }
}
