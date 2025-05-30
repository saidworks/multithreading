/* Said Zitouni (C)2025 */
package com.saidworks.examples;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

/**
 * This class demonstrates the use of Callable and Future in Java.
 *
 * Unlike Runnable, Callable can return a result and throw checked exceptions.
 * Future represents the result of an asynchronous computation.
 *
 * Key concepts demonstrated:
 * 1. Creating and submitting Callable tasks
 * 2. Getting results from Future objects
 * 3. Handling timeouts and cancellation
 * 4. Using invokeAll and invokeAny methods
 */
public class CallableFutureExample {

    /**
     * A callable task that simulates a computation and returns a result.
     */
    static class ComputationTask implements Callable<Integer> {
        private final int taskId;
        private final int duration;

        public ComputationTask(int taskId, int duration) {
            this.taskId = taskId;
            this.duration = duration;
        }

        @Override
        public Integer call() throws Exception {
            System.out.println("Task " + taskId + " started, will take " + duration + " ms");

            try {
                // Simulate computation time
                Thread.sleep(duration);
            } catch (InterruptedException e) {
                System.out.println("Task " + taskId + " was interrupted!");
                Thread.currentThread().interrupt();
                throw e; // Re-throw to signal the task didn't complete
            }

            // Compute a result (in this case, just a random number)
            int result = new Random().nextInt(100) + 1;
            System.out.println("Task " + taskId + " completed with result: " + result);

            return result;
        }
    }

    /**
     * Demonstrates basic usage of Callable and Future.
     */
    private static void demonstrateBasicCallableFuture() {
        System.out.println("\n--- Basic Callable and Future Example ---");

        ExecutorService executor = Executors.newFixedThreadPool(2);

        // Create and submit a Callable task
        Callable<Integer> task = new ComputationTask(1, 2000);
        Future<Integer> future = executor.submit(task);

        System.out.println("Task submitted, doing other work while waiting for result...");

        // Check if the task is done (non-blocking)
        System.out.println("Is task done? " + future.isDone());

        try {
            // Get the result of the task (this will block until the task completes)
            Integer result = future.get();
            System.out.println("Task result: " + result);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Waiting for result was interrupted");
        } catch (ExecutionException e) {
            System.out.println("Task threw an exception: " + e.getCause());
        }

        executor.shutdown();
    }

    /**
     * Demonstrates handling timeouts and cancellation with Future.
     */
    private static void demonstrateTimeoutAndCancellation() {
        System.out.println("\n--- Timeout and Cancellation Example ---");

        ExecutorService executor = Executors.newSingleThreadExecutor();

        // Create a task that takes a long time
        Callable<Integer> longTask = new ComputationTask(2, 5000);
        Future<Integer> future = executor.submit(longTask);

        try {
            // Try to get the result with a timeout of 2 seconds
            System.out.println("Waiting for result with 2-second timeout...");
            Integer result = future.get(2, TimeUnit.SECONDS);
            System.out.println("Got result: " + result);
        } catch (TimeoutException e) {
            System.out.println("Timeout occurred! Task took too long.");

            // Cancel the task
            boolean cancelled = future.cancel(true); // true means attempt to interrupt if running
            System.out.println("Task cancelled: " + cancelled);
            System.out.println("Is task cancelled? " + future.isCancelled());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Waiting was interrupted");
        } catch (ExecutionException e) {
            System.out.println("Task threw an exception: " + e.getCause());
        }

        executor.shutdownNow(); // Attempt to stop all actively executing tasks
    }

    /**
     * Demonstrates executing multiple Callable tasks and getting all results.
     */
    private static void demonstrateInvokeAll() {
        System.out.println("\n--- InvokeAll Example ---");

        ExecutorService executor = Executors.newFixedThreadPool(3);

        // Create a list of Callable tasks
        List<Callable<Integer>> tasks = new ArrayList<>();
        tasks.add(new ComputationTask(3, 1000));
        tasks.add(new ComputationTask(4, 2000));
        tasks.add(new ComputationTask(5, 1500));

        try {
            // Execute all tasks and get a list of Futures
            System.out.println("Executing all tasks...");
            List<Future<Integer>> futures = executor.invokeAll(tasks);

            // Process all results
            System.out.println("All tasks completed, processing results:");
            int sum = 0;
            for (int i = 0; i < futures.size(); i++) {
                Future<Integer> future = futures.get(i);
                try {
                    Integer result = future.get();
                    System.out.println("Task " + (i + 3) + " result: " + result);
                    sum += result;
                } catch (ExecutionException e) {
                    System.out.println("Task " + (i + 3) + " threw an exception: " + e.getCause());
                }
            }
            System.out.println("Sum of all results: " + sum);

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Operation was interrupted");
        }

        executor.shutdown();
    }

    /**
     * Demonstrates executing multiple Callable tasks and getting the result of the first to complete.
     */
    private static void demonstrateInvokeAny() {
        System.out.println("\n--- InvokeAny Example ---");

        ExecutorService executor = Executors.newFixedThreadPool(3);

        // Create a list of Callable tasks with different durations
        List<Callable<Integer>> tasks = new ArrayList<>();
        tasks.add(new ComputationTask(6, 3000)); // Slowest
        tasks.add(new ComputationTask(7, 2000)); // Medium
        tasks.add(new ComputationTask(8, 1000)); // Fastest

        try {
            // Execute all tasks and get the result of the first to complete
            System.out.println("Executing tasks with invokeAny...");
            Integer result = executor.invokeAny(tasks);

            // This will be the result of the fastest task (unless it failed)
            System.out.println("First completed task result: " + result);

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Operation was interrupted");
        } catch (ExecutionException e) {
            System.out.println("All tasks failed: " + e.getCause());
        }

        executor.shutdown();
    }

    /**
     * Runs all Callable and Future examples.
     */
    public static void runExample() {
        System.out.println("\n=== Callable and Future Example ===");

        demonstrateBasicCallableFuture();
        demonstrateTimeoutAndCancellation();
        demonstrateInvokeAll();
        demonstrateInvokeAny();

        System.out.println("Callable and Future Example completed");
    }
}
