/* Said Zitouni (C)2025 */
package com.saidworks.examples;

/**
 * This class demonstrates thread synchronization in Java.
 *
 * Synchronization is necessary when multiple threads access shared resources
 * to prevent race conditions and ensure data consistency.
 *
 * Key concepts demonstrated:
 * 1. Race conditions without synchronization
 * 2. Synchronized methods
 * 3. Synchronized blocks
 * 4. Object locks and class locks
 */
public class SynchronizationExample {

    /**
     * This class demonstrates a counter without proper synchronization,
     * which can lead to race conditions.
     */
    static class UnsafeCounter {
        private int count = 0;

        // This method is not synchronized, so multiple threads can enter it simultaneously
        public void increment() {
            // This is not an atomic operation! It involves:
            // 1. Reading the current value
            // 2. Adding 1 to it
            // 3. Storing the new value
            count++;
        }

        public int getCount() {
            return count;
        }
    }

    /**
     * This class demonstrates a thread-safe counter using synchronized methods.
     */
    static class SafeCounter {
        private int count = 0;

        // The synchronized keyword ensures only one thread can execute this method at a time
        public synchronized void increment() {
            count++;
        }

        // This method is also synchronized to ensure visibility of the latest count
        public synchronized int getCount() {
            return count;
        }
    }

    /**
     * This class demonstrates using synchronized blocks instead of methods.
     */
    static class BlockSynchronizedCounter {
        private int count = 0;
        private final Object lock = new Object(); // dedicated lock object

        public void increment() {
            // Only synchronize the critical section
            synchronized (lock) {
                count++;
            }
            // Other non-critical code can run without synchronization
        }

        public int getCount() {
            synchronized (lock) {
                return count;
            }
        }
    }

    /**
     * Demonstrates the effects of synchronization on thread safety.
     */
    public static void runExample() {
        System.out.println("\n=== Synchronization Example ===");

        // First, demonstrate the problem with unsynchronized access
        demonstrateRaceCondition();

        // Then show how synchronization solves the problem
        demonstrateSynchronizedCounter();

        // Finally, show synchronized blocks
        demonstrateBlockSynchronization();

        System.out.println("Synchronization Example completed");
    }

    private static void demonstrateRaceCondition() {
        System.out.println("\n--- Demonstrating Race Condition ---");
        final UnsafeCounter unsafeCounter = new UnsafeCounter();

        // Create two threads that both increment the counter many times
        Thread thread1 = new Thread(() -> {
            for (int i = 0; i < 10000; i++) {
                unsafeCounter.increment();
            }
        });

        Thread thread2 = new Thread(() -> {
            for (int i = 0; i < 10000; i++) {
                unsafeCounter.increment();
            }
        });

        thread1.start();
        thread2.start();

        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // The expected count is 20000, but due to race conditions, it will likely be less
        System.out.println("Unsafe counter final count: " + unsafeCounter.getCount());
        System.out.println("Expected count: 20000");
        System.out.println("Race condition occurred: " + (unsafeCounter.getCount() != 20000));
    }

    private static void demonstrateSynchronizedCounter() {
        System.out.println("\n--- Demonstrating Synchronized Methods ---");
        final SafeCounter safeCounter = new SafeCounter();

        Thread thread1 = new Thread(() -> {
            for (int i = 0; i < 10000; i++) {
                safeCounter.increment();
            }
        });

        Thread thread2 = new Thread(() -> {
            for (int i = 0; i < 10000; i++) {
                safeCounter.increment();
            }
        });

        thread1.start();
        thread2.start();

        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // With proper synchronization, the count should be exactly 20000
        System.out.println("Safe counter final count: " + safeCounter.getCount());
        System.out.println("Expected count: 20000");
        System.out.println("Count is correct: " + (safeCounter.getCount() == 20000));
    }

    private static void demonstrateBlockSynchronization() {
        System.out.println("\n--- Demonstrating Synchronized Blocks ---");
        final BlockSynchronizedCounter blockCounter = new BlockSynchronizedCounter();

        Thread thread1 = new Thread(() -> {
            for (int i = 0; i < 10000; i++) {
                blockCounter.increment();
            }
        });

        Thread thread2 = new Thread(() -> {
            for (int i = 0; i < 10000; i++) {
                blockCounter.increment();
            }
        });

        thread1.start();
        thread2.start();

        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // With synchronized blocks, the count should also be exactly 20000
        System.out.println("Block synchronized counter final count: " + blockCounter.getCount());
        System.out.println("Expected count: 20000");
        System.out.println("Count is correct: " + (blockCounter.getCount() == 20000));
    }
}
