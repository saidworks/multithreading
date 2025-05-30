/* Said Zitouni (C)2025 */
package com.saidworks.examples;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * This class demonstrates the use of ThreadLocal in Java.
 *
 * ThreadLocal provides thread-local variables, which are variables that are
 * local to each thread. Each thread has its own, independently initialized
 * copy of the variable, invisible to other threads.
 *
 * Key concepts demonstrated:
 * 1. Basic ThreadLocal usage
 * 2. ThreadLocal with initial values
 * 3. InheritableThreadLocal for passing values to child threads
 * 4. ThreadLocal memory considerations and proper cleanup
 * 5. Java 8 ThreadLocal.withInitial() factory method
 */
public class ThreadLocalExample {

    /**
     * Demonstrates basic ThreadLocal usage.
     */
    private static void demonstrateBasicThreadLocal() {
        System.out.println("\n--- Basic ThreadLocal Example ---");

        // Create a ThreadLocal variable
        ThreadLocal<String> threadLocal = new ThreadLocal<>();

        // Create two threads that use the same ThreadLocal variable
        Thread thread1 = new Thread(() -> {
            // Set a value in the ThreadLocal
            threadLocal.set("Thread 1's value");

            // Get the value (should be the one we just set)
            System.out.println("Thread 1 gets: " + threadLocal.get());

            // Clean up the ThreadLocal value
            threadLocal.remove();

            // After removal, get returns null
            System.out.println("Thread 1 after remove: " + threadLocal.get());
        });

        Thread thread2 = new Thread(() -> {
            // Initially, the value is null for this thread
            System.out.println("Thread 2 initially gets: " + threadLocal.get());

            // Set a different value
            threadLocal.set("Thread 2's value");

            // Get the value (should be the one we just set)
            System.out.println("Thread 2 gets: " + threadLocal.get());

            // Clean up
            threadLocal.remove();
        });

        // Start the threads
        thread1.start();
        thread2.start();

        // Wait for both threads to complete
        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Main thread has its own value (or null if not set)
        System.out.println("Main thread gets: " + threadLocal.get());
    }

    /**
     * Demonstrates ThreadLocal with initial values.
     */
    private static void demonstrateThreadLocalWithInitialValue() {
        System.out.println("\n--- ThreadLocal with Initial Value Example ---");

        // Create a ThreadLocal with an initial value
        ThreadLocal<Integer> threadLocalCounter = ThreadLocal.withInitial(() -> 0);

        // Create multiple threads that increment the counter
        Runnable task = () -> {
            String threadName = Thread.currentThread().getName();

            // Get the initial value
            int counter = threadLocalCounter.get();
            System.out.println(threadName + " initial counter: " + counter);

            // Increment a few times
            for (int i = 0; i < 3; i++) {
                counter++;
                threadLocalCounter.set(counter);
                System.out.println(threadName + " incremented counter to: " + counter);
            }

            // Clean up
            threadLocalCounter.remove();
        };

        // Create and start multiple threads
        Thread[] threads = new Thread[3];
        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(task, "Thread-" + i);
            threads[i].start();
        }

        // Wait for all threads to complete
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    /**
     * Demonstrates a common use case: thread-safe date formatting.
     */
    private static void demonstrateDateFormatterUseCase() {
        System.out.println("\n--- Thread-safe Date Formatter Example ---");

        // SimpleDateFormat is not thread-safe, so we use ThreadLocal
        ThreadLocal<SimpleDateFormat> dateFormatThreadLocal =
                ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));

        // Function to format a date using the thread-local formatter
        Runnable formatDates = () -> {
            String threadName = Thread.currentThread().getName();

            // Get the formatter for this thread
            SimpleDateFormat formatter = dateFormatThreadLocal.get();

            // Format some dates
            for (int i = 0; i < 3; i++) {
                Date now = new Date(System.currentTimeMillis() + i * 1000); // Add some seconds
                String formattedDate = formatter.format(now);
                System.out.println(threadName + " formatted date: " + formattedDate);

                // Simulate some work
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }

            // Clean up
            dateFormatThreadLocal.remove();
        };

        // Create a thread pool
        ExecutorService executor = Executors.newFixedThreadPool(3);

        // Submit multiple tasks
        for (int i = 0; i < 3; i++) {
            executor.submit(formatDates);
        }

        // Shutdown the executor
        executor.shutdown();
        try {
            executor.awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Demonstrates InheritableThreadLocal which passes values to child threads.
     */
    private static void demonstrateInheritableThreadLocal() {
        System.out.println("\n--- InheritableThreadLocal Example ---");

        // Create an InheritableThreadLocal
        InheritableThreadLocal<String> inheritableThreadLocal = new InheritableThreadLocal<>();

        // Set a value in the main thread
        inheritableThreadLocal.set("Value set in main thread");

        // Create a child thread
        Thread childThread = new Thread(() -> {
            // Child thread inherits the value from the parent thread
            System.out.println("Child thread inherited value: " + inheritableThreadLocal.get());

            // Child can modify its own copy
            inheritableThreadLocal.set("Value modified in child thread");
            System.out.println("Child thread modified value: " + inheritableThreadLocal.get());

            // Create a grandchild thread
            Thread grandchildThread = new Thread(() -> {
                // Grandchild inherits from child
                System.out.println("Grandchild thread inherited value: " + inheritableThreadLocal.get());

                // Clean up
                inheritableThreadLocal.remove();
            });

            grandchildThread.start();
            try {
                grandchildThread.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            // Clean up
            inheritableThreadLocal.remove();
        });

        // Start the child thread
        childThread.start();

        try {
            childThread.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Main thread's value is unchanged
        System.out.println("Main thread value after child execution: " + inheritableThreadLocal.get());

        // Clean up
        inheritableThreadLocal.remove();
    }

    /**
     * Demonstrates memory considerations with ThreadLocal.
     */
    private static void demonstrateMemoryConsiderations() {
        System.out.println("\n--- ThreadLocal Memory Considerations ---");

        System.out.println("Important notes about ThreadLocal and memory:");
        System.out.println("1. ThreadLocal values are stored in the Thread object itself");
        System.out.println("2. If you don't remove() ThreadLocal values when done, they can cause memory leaks");
        System.out.println("3. This is especially problematic in thread pools where threads are reused");
        System.out.println("4. Always call ThreadLocal.remove() when you're done with the value");
        System.out.println("5. Consider using try-finally blocks to ensure cleanup");

        // Example of proper cleanup with try-finally
        ThreadLocal<byte[]> threadLocalLargeObject = ThreadLocal.withInitial(() -> new byte[1024 * 1024]); // 1MB

        Runnable properCleanupTask = () -> {
            try {
                // Use the ThreadLocal value
                byte[] data = threadLocalLargeObject.get();
                System.out.println(
                        Thread.currentThread().getName() + " using large object of size: " + data.length + " bytes");

                // Simulate some work
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                // Always clean up in finally block
                threadLocalLargeObject.remove();
                System.out.println(Thread.currentThread().getName() + " cleaned up ThreadLocal");
            }
        };

        // Execute in a thread pool to demonstrate importance in pooled environments
        ExecutorService executor = Executors.newFixedThreadPool(2);

        for (int i = 0; i < 5; i++) {
            executor.submit(properCleanupTask);
        }

        // Shutdown the executor
        executor.shutdown();
        try {
            executor.awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Runs all ThreadLocal examples.
     */
    public static void runExample() {
        System.out.println("\n=== ThreadLocal Example ===");

        demonstrateBasicThreadLocal();
        demonstrateThreadLocalWithInitialValue();
        demonstrateDateFormatterUseCase();
        demonstrateInheritableThreadLocal();
        demonstrateMemoryConsiderations();

        System.out.println("ThreadLocal Example completed");
    }
}
