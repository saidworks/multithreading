/* ADSK  | Product Accesss SZ Q2 Goal (C)2025 */
package com.saidworks.deadlock;

import java.util.Random;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * DeadlocksApp demonstrates a scenario where two threads (Train A and Train B) attempt to acquire
 * locks on shared resources (roadA and roadB) in a way that can lead to a deadlock.
 */
public class DeadlocksApp {
  public static void main(String[] args) {
    // Create an Intersection object to manage shared resources
    Intersection intersection = new Intersection();

    // Create and start threads for Train A and Train B
    Thread trainAThread = new Thread(new TrainA(intersection));
    Thread trainBThread = new Thread(new TrainB(intersection));

    trainAThread.start();
    trainAThread.setName("Train A");
    trainBThread.start();
    trainBThread.setName("Train B");
  }

  /** TrainB represents a train attempting to pass through road B. */
  public static class TrainB implements Runnable {
    Logger log = LogManager.getLogger(TrainB.class);
    private final Intersection intersection;
    private final Random random = new Random();

    /**
     * Constructor for TrainB.
     *
     * @param intersection The shared Intersection object.
     */
    public TrainB(Intersection intersection) {
      this.intersection = intersection;
    }

    /** Continuously attempts to pass through road B, simulating random delays. */
    @Override
    public void run() {
      while (true) {
        long sleepingTime = random.nextInt(5); // Random delay before attempting to pass
        log.info("Train B deparated");
        try {
          Thread.sleep(sleepingTime);
        } catch (InterruptedException e) {
          // Handle interruption
        }

        intersection.takeRoadB(); // Attempt to take road B
      }
    }
  }

  /** TrainA represents a train attempting to pass through road A. */
  public static class TrainA implements Runnable {
    Logger log = LogManager.getLogger(TrainA.class);
    private Intersection intersection;
    private Random random = new Random();

    /**
     * Constructor for TrainA.
     *
     * @param intersection The shared Intersection object.
     */
    public TrainA(Intersection intersection) {
      this.intersection = intersection;
    }

    /** Continuously attempts to pass through road A, simulating random delays. */
    @Override
    public void run() {
      while (true) {
        long sleepingTime = random.nextInt(5); // Random delay before attempting to pass
        log.info("Train A deparated");
        try {
          Thread.sleep(sleepingTime);
        } catch (InterruptedException e) {
          // Handle interruption
        }

        intersection.takeRoadA(); // Attempt to take road A
      }
    }
  }

  /**
   * Intersection manages the shared resources (roadA and roadB) and provides synchronized methods
   * to ensure thread-safe access.
   */
  public static class Intersection {
    Logger log = LogManager.getLogger(Intersection.class);
    private final Object roadA = new Object(); // Lock for road A
    private final Object roadB = new Object(); // Lock for road B

    /**
     * Allows a thread to take road A by acquiring locks on roadA and roadB. Logs the thread's
     * actions.
     */
    public void takeRoadA() {
      synchronized (roadB) {
        log.info("Road A is locked by thread {}", Thread.currentThread().getName());

        synchronized (roadA) {
          log.info("Train is passing through road A");
          try {
            Thread.sleep(1); // Simulate train passing
          } catch (InterruptedException e) {
            // Handle interruption
          }
        }
      }
    }

    /**
     * Allows a thread to take road B by acquiring locks on roadB and roadA. Logs the thread's
     * actions.
     */
    public void takeRoadB() {
      synchronized (roadB) {
        log.info("Road B is locked by thread {}", Thread.currentThread().getName());

        synchronized (roadA) {
          log.info("Train is passing through road B");
          try {
            Thread.sleep(1); // Simulate train passing
          } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Restore interrupted status
          }
        }
      }
    }
  }
}
