/* Said Zitouni (C)2025 */
package com.saidworks.examples;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

/**
 * This class demonstrates the use of Parallel Streams in Java.
 *
 * Parallel Streams were introduced in Java 8 as part of the Stream API.
 * They allow for easy parallelization of stream operations, leveraging
 * multiple processor cores for improved performance in certain scenarios.
 *
 * Key concepts demonstrated:
 * 1. Creating parallel streams
 * 2. Performance comparison with sequential streams
 * 3. Common parallel stream operations
 * 4. Pitfalls and considerations when using parallel streams
 * 5. Controlling parallelism level
 */
public class ParallelStreamsExample {

    /**
     * Demonstrates different ways to create parallel streams.
     */
    private static void demonstrateCreatingParallelStreams() {
        System.out.println("\n--- Creating Parallel Streams ---");

        // Method 1: From a collection using parallelStream()
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        System.out.println("Method 1: Using Collection.parallelStream()");
        numbers.parallelStream().forEach(n -> System.out.print(n + " "));
        System.out.println();

        // Method 2: From a stream using parallel()
        System.out.println("\nMethod 2: Using Stream.parallel()");
        numbers.stream().parallel().forEach(n -> System.out.print(n + " "));
        System.out.println();

        // Method 3: From IntStream, LongStream, or DoubleStream
        System.out.println("\nMethod 3: Using IntStream.range().parallel()");
        IntStream.range(1, 11).parallel().forEach(n -> System.out.print(n + " "));
        System.out.println();

        // Check if a stream is parallel
        boolean isParallel = numbers.parallelStream().isParallel();
        System.out.println("\nIs the stream parallel? " + isParallel);

        // Convert back to sequential
        System.out.println("\nConverting back to sequential:");
        numbers.parallelStream().sequential().forEach(n -> System.out.print(n + " "));
        System.out.println();
    }

    /**
     * Demonstrates a performance comparison between sequential and parallel streams.
     */
    private static void demonstratePerformanceComparison() {
        System.out.println("\n--- Performance Comparison ---");

        // Create a large dataset
        int size = 50_000_000;
        System.out.println("Computing sum of " + size + " numbers...");

        // Sequential sum
        long startTime = System.currentTimeMillis();
        long sequentialSum = LongStream.rangeClosed(1, size).sum();
        long sequentialTime = System.currentTimeMillis() - startTime;

        // Parallel sum
        startTime = System.currentTimeMillis();
        long parallelSum = LongStream.rangeClosed(1, size).parallel().sum();
        long parallelTime = System.currentTimeMillis() - startTime;

        System.out.println("Sequential sum: " + sequentialSum + " in " + sequentialTime + " ms");
        System.out.println("Parallel sum: " + parallelSum + " in " + parallelTime + " ms");
        System.out.println("Speedup: " + (double) sequentialTime / parallelTime + "x");

        // Another example with a more complex operation
        System.out.println("\nComputing prime numbers up to 100,000...");

        // Sequential count of primes
        startTime = System.currentTimeMillis();
        long sequentialCount = IntStream.rangeClosed(2, 100_000)
                .filter(ParallelStreamsExample::isPrime)
                .count();
        sequentialTime = System.currentTimeMillis() - startTime;

        // Parallel count of primes
        startTime = System.currentTimeMillis();
        long parallelCount = IntStream.rangeClosed(2, 100_000)
                .parallel()
                .filter(ParallelStreamsExample::isPrime)
                .count();
        parallelTime = System.currentTimeMillis() - startTime;

        System.out.println("Sequential prime count: " + sequentialCount + " in " + sequentialTime + " ms");
        System.out.println("Parallel prime count: " + parallelCount + " in " + parallelTime + " ms");
        System.out.println("Speedup: " + (double) sequentialTime / parallelTime + "x");
    }

    /**
     * Helper method to check if a number is prime.
     */
    private static boolean isPrime(int n) {
        if (n <= 1) return false;
        if (n <= 3) return true;
        if (n % 2 == 0 || n % 3 == 0) return false;

        for (int i = 5; i * i <= n; i += 6) {
            if (n % i == 0 || n % (i + 2) == 0) return false;
        }
        return true;
    }

    /**
     * Demonstrates common parallel stream operations.
     */
    private static void demonstrateCommonOperations() {
        System.out.println("\n--- Common Parallel Stream Operations ---");

        List<String> words = Arrays.asList(
                "apple", "banana", "cherry", "date", "elderberry", "fig", "grape", "honeydew", "kiwi", "lemon");

        // Filtering
        System.out.println("Filtering words starting with 'a' to 'f':");
        List<String> filteredWords = words.parallelStream()
                .filter(w -> w.charAt(0) >= 'a' && w.charAt(0) <= 'f')
                .collect(Collectors.toList());
        System.out.println(filteredWords);

        // Mapping
        System.out.println("\nConverting to uppercase:");
        List<String> upperCaseWords =
                words.parallelStream().map(String::toUpperCase).collect(Collectors.toList());
        System.out.println(upperCaseWords);

        // Reduction
        System.out.println("\nConcatenating all words:");
        String concatenated = words.parallelStream().reduce("", (s1, s2) -> s1 + s2);
        System.out.println(concatenated);

        // Collecting
        System.out.println("\nGrouping by first letter:");
        var groupedByFirstLetter = words.parallelStream().collect(Collectors.groupingByConcurrent(w -> w.charAt(0)));
        System.out.println(groupedByFirstLetter);

        // ForEach
        System.out.println("\nProcessing each element (order not guaranteed):");
        words.parallelStream().forEach(w -> System.out.print(w + " "));
        System.out.println();

        // ForEachOrdered (maintains encounter order)
        System.out.println("\nProcessing each element in order:");
        words.parallelStream().forEachOrdered(w -> System.out.print(w + " "));
        System.out.println();
    }

    /**
     * Demonstrates pitfalls and considerations when using parallel streams.
     */
    private static void demonstratePitfallsAndConsiderations() {
        System.out.println("\n--- Pitfalls and Considerations ---");

        // 1. Non-thread-safe operations
        System.out.println("1. Non-thread-safe operations:");

        List<Integer> numbers = new ArrayList<>();

        // This is NOT thread-safe and may result in missing elements
        System.out.println("Incorrect way (may lose elements):");
        IntStream.range(0, 1000).parallel().forEach(numbers::add); // add is not thread-safe

        System.out.println("Size after parallel add: " + numbers.size() + " (expected 1000)");

        // Correct way using a thread-safe collector
        System.out.println("\nCorrect way:");
        List<Integer> safeNumbers = IntStream.range(0, 1000).parallel().boxed().collect(Collectors.toList());

        System.out.println("Size after parallel collect: " + safeNumbers.size());

        // 2. Operations with side effects
        System.out.println("\n2. Operations with side effects:");

        StringBuilder sb = new StringBuilder();

        // This is NOT thread-safe and will produce unpredictable results
        System.out.println("Incorrect way (unpredictable result):");
        IntStream.range(0, 100).parallel().forEach(i -> sb.append(i).append(" ")); // StringBuilder is not thread-safe

        System.out.println("Result length: " + sb.length() + " (unpredictable)");

        // 3. Order-dependent operations
        System.out.println("\n3. Order-dependent operations:");

        // This will produce different results in parallel vs sequential
        String sequentialResult =
                IntStream.range(0, 10).mapToObj(Integer::toString).reduce("", (s1, s2) -> s1 + s2);

        String parallelResult =
                IntStream.range(0, 10).parallel().mapToObj(Integer::toString).reduce("", (s1, s2) -> s1 + s2);

        System.out.println("Sequential result: " + sequentialResult);
        System.out.println("Parallel result: " + parallelResult);
        System.out.println("Are they equal? " + sequentialResult.equals(parallelResult));

        // 4. When parallel streams might not help
        System.out.println("\n4. When parallel streams might not help:");
        System.out.println("- Small data sets (overhead > benefit)");
        System.out.println("- Operations that cannot be parallelized efficiently");
        System.out.println("- When the source cannot be efficiently split (e.g., LinkedList)");
        System.out.println("- When the operations are very cheap (overhead > benefit)");
    }

    /**
     * Demonstrates controlling the parallelism level.
     */
    private static void demonstrateControllingParallelism() {
        System.out.println("\n--- Controlling Parallelism Level ---");

        // Get the common ForkJoinPool that parallel streams use
        ForkJoinPool commonPool = ForkJoinPool.commonPool();
        int parallelism = commonPool.getParallelism();

        System.out.println("Default parallelism level: " + parallelism);
        System.out.println("Available processors: " + Runtime.getRuntime().availableProcessors());

        // You can set the parallelism level using the system property:
        // -Djava.util.concurrent.ForkJoinPool.common.parallelism=4
        System.out.println("To change the parallelism level, use the JVM argument:");
        System.out.println("-Djava.util.concurrent.ForkJoinPool.common.parallelism=N");

        // Using a custom ForkJoinPool for specific tasks
        System.out.println("\nUsing a custom ForkJoinPool with parallelism=2:");

        try {
            ForkJoinPool customPool = new ForkJoinPool(2);

            long result = customPool
                    .submit(() ->
                            // This parallel stream will use the custom pool
                            IntStream.range(1, 1000)
                                    .parallel()
                                    .filter(ParallelStreamsExample::isPrime)
                                    .count())
                    .get();

            System.out.println("Number of primes found: " + result);

            customPool.shutdown();
            customPool.awaitTermination(10, TimeUnit.SECONDS);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    /**
     * Runs all parallel streams examples.
     */
    public static void runExample() {
        System.out.println("\n=== Parallel Streams Example ===");

        demonstrateCreatingParallelStreams();
        demonstrateCommonOperations();
        demonstratePitfallsAndConsiderations();
        demonstrateControllingParallelism();

        // Performance comparison can take longer, so run it last
        demonstratePerformanceComparison();

        System.out.println("Parallel Streams Example completed");
    }
}
