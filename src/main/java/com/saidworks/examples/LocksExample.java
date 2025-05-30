/* Said Zitouni (C)2025 */
package com.saidworks.examples;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.*;

/**
 * This class demonstrates the use of Locks and Conditions in Java.
 *
 * The java.util.concurrent.locks package provides more flexible locking operations
 * than synchronized blocks and methods, with additional features like:
 * - Timed lock waits
 * - Interruptible lock acquisition
 * - Non-block-structured locking
 * - Multiple condition variables per lock
 *
 * Key concepts demonstrated:
 * 1. ReentrantLock - a mutual exclusion lock with the same behavior as synchronized blocks
 * 2. ReadWriteLock - allows multiple readers but only one writer
 * 3. Condition - provides a way for threads to wait for a specific condition
 * 4. StampedLock - a capability-based lock with optimistic reading
 */
public class LocksExample {

    /**
     * Demonstrates basic usage of ReentrantLock.
     */
    private static void demonstrateReentrantLock() {
        System.out.println("\n--- ReentrantLock Example ---");

        // Create a ReentrantLock
        ReentrantLock lock = new ReentrantLock();

        // Create a shared resource
        StringBuilder sharedResource = new StringBuilder();

        // Create and start multiple threads that access the shared resource
        Thread thread1 = new Thread(() -> {
            // Acquire the lock
            lock.lock();
            try {
                System.out.println("Thread 1 acquired the lock");

                // Access the shared resource
                sharedResource.append("Hello from Thread 1\n");

                // Simulate some work
                Thread.sleep(1000);

                sharedResource.append("Thread 1 is done\n");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                // Always release the lock in a finally block
                lock.unlock();
                System.out.println("Thread 1 released the lock");
            }
        });

        Thread thread2 = new Thread(() -> {
            // Try to acquire the lock with a timeout
            try {
                if (lock.tryLock(2, TimeUnit.SECONDS)) {
                    try {
                        System.out.println("Thread 2 acquired the lock");

                        // Access the shared resource
                        sharedResource.append("Hello from Thread 2\n");

                        // Simulate some work
                        Thread.sleep(500);

                        sharedResource.append("Thread 2 is done\n");
                    } finally {
                        lock.unlock();
                        System.out.println("Thread 2 released the lock");
                    }
                } else {
                    System.out.println("Thread 2 couldn't acquire the lock within the timeout");
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Thread 2 was interrupted while waiting for the lock");
            }
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

        // Print the final state of the shared resource
        System.out.println("Shared resource contents:");
        System.out.println(sharedResource.toString());

        // Demonstrate lock information
        System.out.println("Lock information:");
        System.out.println("- Is locked: " + lock.isLocked());
        System.out.println("- Is held by current thread: " + lock.isHeldByCurrentThread());
        System.out.println("- Queue length: " + lock.getQueueLength());
    }

    /**
     * Demonstrates the use of ReadWriteLock for scenarios with many readers and few writers.
     */
    private static void demonstrateReadWriteLock() {
        System.out.println("\n--- ReadWriteLock Example ---");

        // Create a ReadWriteLock
        ReadWriteLock rwLock = new ReentrantReadWriteLock();
        Lock readLock = rwLock.readLock();
        Lock writeLock = rwLock.writeLock();

        // Shared resource
        final int[] data = {0}; // Using array to make it effectively final and mutable

        // Create reader threads
        Runnable reader = () -> {
            for (int i = 0; i < 3; i++) {
                readLock.lock();
                try {
                    System.out.println(Thread.currentThread().getName() + " reads data: " + data[0]);
                    Thread.sleep(500); // Simulate reading time
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    readLock.unlock();
                }

                // Small delay between reads
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        };

        // Create writer thread
        Runnable writer = () -> {
            for (int i = 1; i <= 3; i++) {
                try {
                    Thread.sleep(300); // Simulate time before writing
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }

                writeLock.lock();
                try {
                    System.out.println(Thread.currentThread().getName() + " writes data: " + i);
                    data[0] = i;
                    Thread.sleep(500); // Simulate writing time
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    writeLock.unlock();
                }
            }
        };

        // Start multiple reader threads and one writer thread
        ExecutorService executor = Executors.newFixedThreadPool(5);
        executor.submit(writer);

        // Start readers
        for (int i = 0; i < 4; i++) {
            executor.submit(reader);
        }

        // Shutdown the executor
        executor.shutdown();
        try {
            executor.awaitTermination(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("Final data value: " + data[0]);
    }

    /**
     * Demonstrates the use of Condition for thread coordination.
     */
    private static void demonstrateCondition() {
        System.out.println("\n--- Condition Example ---");

        // Create a lock and a condition
        ReentrantLock lock = new ReentrantLock();
        Condition condition = lock.newCondition();

        // Shared state
        final boolean[] ready = {false};

        // Thread that waits for a condition
        Thread waiter = new Thread(() -> {
            lock.lock();
            try {
                System.out.println("Waiter: Waiting for signal...");

                while (!ready[0]) {
                    // Wait for the condition (releases the lock while waiting)
                    condition.await();
                }

                System.out.println("Waiter: Received signal, continuing execution");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Waiter: Interrupted while waiting");
            } finally {
                lock.unlock();
            }
        });

        // Thread that signals the condition
        Thread signaler = new Thread(() -> {
            try {
                // Wait a bit before signaling
                Thread.sleep(2000);

                lock.lock();
                try {
                    System.out.println("Signaler: Setting ready state and signaling");
                    ready[0] = true;
                    condition.signal();
                } finally {
                    lock.unlock();
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        // Start the threads
        waiter.start();
        signaler.start();

        // Wait for both threads to complete
        try {
            waiter.join();
            signaler.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Demonstrates the use of StampedLock, which supports optimistic reading.
     */
    private static void demonstrateStampedLock() {
        System.out.println("\n--- StampedLock Example ---");

        // Create a StampedLock
        StampedLock lock = new StampedLock();

        // Shared resource
        double[] point = {0.0, 0.0}; // x, y coordinates

        // Writer thread that updates the point
        Thread writer = new Thread(() -> {
            for (int i = 0; i < 3; i++) {
                // Acquire write lock
                long stamp = lock.writeLock();
                try {
                    System.out.println("Writer: Updating point");
                    point[0] += 1.0;
                    point[1] += 1.0;
                    System.out.println("Writer: Point updated to (" + point[0] + ", " + point[1] + ")");

                    // Simulate some work
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    // Release write lock
                    lock.unlockWrite(stamp);
                }

                // Small delay between writes
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });

        // Reader thread that uses pessimistic read lock
        Thread pessimisticReader = new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                // Acquire read lock
                long stamp = lock.readLock();
                try {
                    System.out.println("Pessimistic Reader: Point is (" + point[0] + ", " + point[1] + ")");

                    // Calculate distance from origin (read-only operation)
                    double distance = Math.sqrt(point[0] * point[0] + point[1] * point[1]);
                    System.out.println("Pessimistic Reader: Distance from origin: " + distance);

                    // Simulate some work
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    // Release read lock
                    lock.unlockRead(stamp);
                }

                // Small delay between reads
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });

        // Reader thread that uses optimistic read lock
        Thread optimisticReader = new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                // Try optimistic read
                long stamp = lock.tryOptimisticRead();
                double x = point[0];
                double y = point[1];

                // Check if the stamp is still valid (no writes occurred)
                if (lock.validate(stamp)) {
                    System.out.println(
                            "Optimistic Reader: Point is (" + x + ", " + y + ") [Optimistic read succeeded]");
                } else {
                    System.out.println("Optimistic Reader: Optimistic read failed, falling back to pessimistic read");

                    // Fall back to pessimistic read
                    stamp = lock.readLock();
                    try {
                        x = point[0];
                        y = point[1];
                        System.out.println("Optimistic Reader: Point is (" + x + ", " + y + ") [After fallback]");
                    } finally {
                        lock.unlockRead(stamp);
                    }
                }

                // Small delay between reads
                try {
                    Thread.sleep(150);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });

        // Start the threads
        writer.start();
        pessimisticReader.start();
        optimisticReader.start();

        // Wait for all threads to complete
        try {
            writer.join();
            pessimisticReader.join();
            optimisticReader.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Runs all locks examples.
     */
    public static void runExample() {
        System.out.println("\n=== Locks and Conditions Example ===");

        demonstrateReentrantLock();
        demonstrateReadWriteLock();
        demonstrateCondition();
        demonstrateStampedLock();

        System.out.println("Locks and Conditions Example completed");
    }
}
