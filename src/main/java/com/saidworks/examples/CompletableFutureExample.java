/* Said Zitouni (C)2025 */
package com.saidworks.examples;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;
import java.util.stream.Collectors;

/**
 * This class demonstrates the use of CompletableFuture in Java.
 *
 * CompletableFuture was introduced in Java 8 and provides a more powerful way
 * to work with asynchronous computations compared to the basic Future interface.
 *
 * Key concepts demonstrated:
 * 1. Creating and completing CompletableFuture
 * 2. Running asynchronous tasks
 * 3. Chaining and composing asynchronous operations
 * 4. Handling exceptions in asynchronous code
 * 5. Combining multiple CompletableFutures
 */
public class CompletableFutureExample {

    /**
     * Simulates a remote service call with a delay.
     */
    private static String fetchData(String source) {
        System.out.println("Fetching data from " + source + "...");
        try {
            // Simulate network delay
            Thread.sleep(new Random().nextInt(2000) + 1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Fetch interrupted", e);
        }
        return "Data from " + source;
    }

    /**
     * Simulates processing data with a delay.
     */
    private static String processData(String data) {
        System.out.println("Processing " + data + "...");
        try {
            // Simulate processing time
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Processing interrupted", e);
        }
        return "Processed: " + data;
    }

    /**
     * Demonstrates creating and completing CompletableFuture manually.
     */
    private static void demonstrateManualCompletion() {
        System.out.println("\n--- Manual Completion Example ---");

        // Create a CompletableFuture that will be completed manually
        CompletableFuture<String> future = new CompletableFuture<>();

        // In a separate thread, complete the future after a delay
        CompletableFuture.runAsync(() -> {
            try {
                System.out.println("Working on the result...");
                Thread.sleep(2000);
                // Complete the future with a result
                future.complete("Result is ready!");

                // This won't have any effect since the future is already completed
                future.complete("Trying to complete again");
            } catch (InterruptedException e) {
                // Complete the future exceptionally if something goes wrong
                future.completeExceptionally(e);
                Thread.currentThread().interrupt();
            }
        });

        // Wait for the result
        try {
            System.out.println("Waiting for the result...");
            String result = future.get(); // Blocks until the future is completed
            System.out.println("Got result: " + result);
        } catch (InterruptedException | ExecutionException e) {
            System.out.println("Error getting result: " + e.getMessage());
        }
    }

    /**
     * Demonstrates running asynchronous tasks with CompletableFuture.
     */
    private static void demonstrateAsyncExecution() {
        System.out.println("\n--- Asynchronous Execution Example ---");

        // Run a task asynchronously without returning a value
        CompletableFuture<Void> runFuture = CompletableFuture.runAsync(() -> {
            System.out.println("Running asynchronously on thread: "
                    + Thread.currentThread().getName());
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            System.out.println("Async task completed");
        });

        // Supply a value asynchronously
        CompletableFuture<String> supplyFuture = CompletableFuture.supplyAsync(() -> {
            System.out.println("Supplying asynchronously on thread: "
                    + Thread.currentThread().getName());
            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            return "Supplied value";
        });

        // Wait for both futures to complete
        try {
            runFuture.get();
            String result = supplyFuture.get();
            System.out.println("Supplied result: " + result);
        } catch (InterruptedException | ExecutionException e) {
            System.out.println("Error: " + e.getMessage());
        }

        // Using a custom executor
        ExecutorService executor = Executors.newFixedThreadPool(2);

        CompletableFuture<String> customFuture = CompletableFuture.supplyAsync(
                () -> {
                    System.out.println("Running with custom executor on thread: "
                            + Thread.currentThread().getName());
                    return "Custom executor result";
                },
                executor);

        try {
            System.out.println(customFuture.get());
        } catch (InterruptedException | ExecutionException e) {
            System.out.println("Error: " + e.getMessage());
        } finally {
            executor.shutdown();
        }
    }

    /**
     * Demonstrates chaining asynchronous operations.
     */
    private static void demonstrateChaining() {
        System.out.println("\n--- Chaining Operations Example ---");

        // Start with an asynchronous operation
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> fetchData("service1"));

        // Chain a transformation (thenApply) - runs when the previous stage completes
        CompletableFuture<String> processedFuture = future.thenApply(data -> {
            System.out.println("Applying transformation on thread: "
                    + Thread.currentThread().getName());
            return processData(data);
        });

        // Chain an action (thenAccept) - consumes the result but doesn't return a new value
        CompletableFuture<Void> actionFuture = processedFuture.thenAccept(result -> {
            System.out.println("Final result: " + result);
        });

        // Chain a task that doesn't need the previous result (thenRun)
        CompletableFuture<Void> runFuture = actionFuture.thenRun(() -> {
            System.out.println("All processing completed");
        });

        // Wait for the entire chain to complete
        try {
            runFuture.get();
        } catch (InterruptedException | ExecutionException e) {
            System.out.println("Error in chain: " + e.getMessage());
        }

        // Asynchronous chaining
        System.out.println("\nAsynchronous chaining:");
        CompletableFuture.supplyAsync(() -> fetchData("service2"))
                .thenApplyAsync(data -> processData(data))
                .thenAcceptAsync(result -> System.out.println("Async chain result: " + result))
                .join(); // Wait for completion
    }

    /**
     * Demonstrates exception handling in CompletableFuture.
     */
    private static void demonstrateExceptionHandling() {
        System.out.println("\n--- Exception Handling Example ---");

        // Create a CompletableFuture that will throw an exception
        CompletableFuture<String> failedFuture = CompletableFuture.supplyAsync(() -> {
            if (true) { // Always fail for this example
                throw new RuntimeException("Simulated error");
            }
            return "This will never be returned";
        });

        // Handle the exception with exceptionally
        CompletableFuture<String> recoveredFuture = failedFuture.exceptionally(ex -> {
            System.out.println("Recovered from error: " + ex.getMessage());
            return "Recovery value";
        });

        // Handle both success and failure with handle
        CompletableFuture<String> handledFuture = failedFuture.handle((result, ex) -> {
            if (ex != null) {
                System.out.println("Handled error: " + ex.getMessage());
                return "Fallback value";
            } else {
                return "Got result: " + result;
            }
        });

        try {
            System.out.println("Recovered result: " + recoveredFuture.get());
            System.out.println("Handled result: " + handledFuture.get());
        } catch (InterruptedException | ExecutionException e) {
            System.out.println("This shouldn't happen as we handled the exceptions: " + e.getMessage());
        }
    }

    /**
     * Demonstrates combining multiple CompletableFutures.
     */
    private static void demonstrateCombining() {
        System.out.println("\n--- Combining CompletableFutures Example ---");

        // Create two independent futures
        CompletableFuture<String> future1 = CompletableFuture.supplyAsync(() -> fetchData("service3"));
        CompletableFuture<String> future2 = CompletableFuture.supplyAsync(() -> fetchData("service4"));

        // Combine results when both complete (thenCombine)
        CompletableFuture<String> combinedFuture = future1.thenCombine(future2, (result1, result2) -> {
            return "Combined: " + result1 + " and " + result2;
        });

        // Run an action when both complete (thenAcceptBoth)
        future1.thenAcceptBoth(future2, (result1, result2) -> {
            System.out.println("Both completed with: " + result1 + ", " + result2);
        });

        // Run an action when either completes (runAfterEither)
        future1.runAfterEither(future2, () -> {
            System.out.println("At least one of the futures completed");
        });

        // Get the result of the first to complete (applyToEither)
        CompletableFuture<String> fastestFuture = future1.applyToEither(future2, result -> {
            return "Fastest result: " + result;
        });

        try {
            System.out.println("Combined result: " + combinedFuture.get());
            System.out.println("Fastest result: " + fastestFuture.get());
        } catch (InterruptedException | ExecutionException e) {
            System.out.println("Error: " + e.getMessage());
        }

        // Wait for all futures to complete
        System.out.println("\nWaiting for all futures:");
        List<CompletableFuture<String>> futures = Arrays.asList(
                CompletableFuture.supplyAsync(() -> fetchData("service5")),
                CompletableFuture.supplyAsync(() -> fetchData("service6")),
                CompletableFuture.supplyAsync(() -> fetchData("service7")));

        // Convert List<CompletableFuture<String>> to CompletableFuture<List<String>>
        CompletableFuture<Void> allFutures = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));

        // When all complete, collect all results
        CompletableFuture<List<String>> allResults = allFutures.thenApply(v -> futures.stream()
                .map(CompletableFuture::join) // Safe to use join here as we know all are done
                .collect(Collectors.toList()));

        try {
            List<String> results = allResults.get();
            System.out.println("All results: " + results);
        } catch (InterruptedException | ExecutionException e) {
            System.out.println("Error getting all results: " + e.getMessage());
        }
    }

    /**
     * Runs all CompletableFuture examples.
     */
    public static void runExample() {
        System.out.println("\n=== CompletableFuture Example ===");

        demonstrateManualCompletion();
        demonstrateAsyncExecution();
        demonstrateChaining();
        demonstrateExceptionHandling();
        demonstrateCombining();

        System.out.println("CompletableFuture Example completed");
    }
}
