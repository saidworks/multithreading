/* Said Zitouni (C)2025 */
package com.saidworks.examples;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * This class demonstrates the use of thread pools and the Executor framework.
 *
 * Thread pools manage a pool of worker threads, reducing the overhead of thread creation.
 * The Executor framework provides a higher-level abstraction for working with threads.
 *
 * Key concepts demonstrated:
 * 1. Different types of thread pools (fixed, cached, scheduled)
 * 2. Submitting tasks to thread pools
 * 3. Shutting down thread pools properly
 * 4. Creating custom thread factories
 */
public class ThreadPoolExample {

    /**
     * Demonstrates the use of a fixed thread pool.
     */
    private static void demonstrateFixedThreadPool() {
        System.out.println("\n--- Fixed Thread Pool Example ---");

        // Create a fixed thread pool with 3 threads
        ExecutorService executor = Executors.newFixedThreadPool(3);

        System.out.println("Submitting 5 tasks to a pool of 3 threads");

        // Submit 5 tasks to the thread pool
        for (int i = 1; i <= 5; i++) {
            final int taskId = i;
            executor.submit(() -> {
                String threadName = Thread.currentThread().getName();
                System.out.println("Task " + taskId + " is running on " + threadName);

                // Simulate work
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }

                System.out.println("Task " + taskId + " completed");
                return taskId; // Return value (not used in this example)
            });
        }

        // Shutdown the executor - no new tasks will be accepted
        executor.shutdown();

        try {
            // Wait for all tasks to complete or timeout after 10 seconds
            boolean completed = executor.awaitTermination(10, TimeUnit.SECONDS);
            System.out.println("All tasks completed: " + completed);
        } catch (InterruptedException e) {
            System.out.println("Awaiting termination was interrupted");
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Demonstrates the use of a cached thread pool.
     */
    private static void demonstrateCachedThreadPool() {
        System.out.println("\n--- Cached Thread Pool Example ---");

        // Create a cached thread pool that creates new threads as needed and reuses idle ones
        ExecutorService executor = Executors.newCachedThreadPool();

        System.out.println("Submitting 10 quick tasks to a cached thread pool");

        // Submit 10 quick tasks
        for (int i = 1; i <= 10; i++) {
            final int taskId = i;
            executor.submit(() -> {
                String threadName = Thread.currentThread().getName();
                System.out.println("Quick task " + taskId + " is running on " + threadName);

                // Very short task
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }

                return null;
            });
        }

        executor.shutdown();

        try {
            executor.awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Demonstrates the use of a scheduled thread pool.
     */
    private static void demonstrateScheduledThreadPool() {
        System.out.println("\n--- Scheduled Thread Pool Example ---");

        // Create a scheduled thread pool with 2 threads
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2);

        // Schedule a task to run after a 2-second delay
        System.out.println("Scheduling a task with 2-second delay");
        scheduler.schedule(
                () -> {
                    System.out.println("Delayed task executed after 2 seconds");
                    return null;
                },
                2,
                TimeUnit.SECONDS);

        // Schedule a task to run repeatedly every 1 second, starting after 0 seconds
        System.out.println("Scheduling a task to run every 1 second");
        final AtomicInteger counter = new AtomicInteger(0);
        ScheduledFuture<?> scheduledFuture = scheduler.scheduleAtFixedRate(
                () -> {
                    int count = counter.incrementAndGet();
                    System.out.println("Periodic task executed, count: " + count);

                    // Run only 3 times for this example
                    if (count >= 3) {
                        System.out.println("Cancelling periodic task");
                        // We can't call scheduler.shutdown() from inside a task, so we cancel this specific task
                        throw new RuntimeException("Cancelling task");
                    }
                },
                0,
                1,
                TimeUnit.SECONDS);

        // Let the scheduled tasks run for a while
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Shutdown the scheduler
        scheduler.shutdown();

        try {
            scheduler.awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Demonstrates the use of a custom thread factory.
     */
    private static void demonstrateCustomThreadFactory() {
        System.out.println("\n--- Custom Thread Factory Example ---");

        // Create a custom thread factory
        ThreadFactory customThreadFactory = new ThreadFactory() {
            private final AtomicInteger threadNumber = new AtomicInteger(1);

            @Override
            public Thread newThread(Runnable r) {
                Thread thread = new Thread(r, "CustomThread-" + threadNumber.getAndIncrement());
                // Set as daemon threads
                thread.setDaemon(true);
                // Set higher priority
                thread.setPriority(Thread.MAX_PRIORITY);
                System.out.println("Created " + thread.getName() + " with priority " + thread.getPriority());
                return thread;
            }
        };

        // Create a thread pool with our custom thread factory
        ExecutorService executor = Executors.newFixedThreadPool(3, customThreadFactory);

        // Submit some tasks
        for (int i = 1; i <= 3; i++) {
            final int taskId = i;
            executor.submit(() -> {
                System.out.println("Task " + taskId + " running on "
                        + Thread.currentThread().getName());
                return null;
            });
        }

        executor.shutdown();

        try {
            executor.awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Runs all thread pool examples.
     */
    public static void runExample() {
        System.out.println("\n=== Thread Pool Example ===");

        demonstrateFixedThreadPool();
        demonstrateCachedThreadPool();
        demonstrateScheduledThreadPool();
        demonstrateCustomThreadFactory();

        System.out.println("Thread Pool Example completed");
    }
}
