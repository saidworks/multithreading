/* Said Zitouni (C)2025 */
package com.saidworks.examples;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.*;

/**
 * This class demonstrates the use of Atomic variables in Java.
 *
 * Atomic variables provide thread-safe operations on single variables without using locks.
 * They use low-level atomic hardware features for better performance in concurrent applications.
 *
 * Key concepts demonstrated:
 * 1. Basic atomic types (AtomicInteger, AtomicLong, AtomicBoolean, AtomicReference)
 * 2. Compare-and-swap (CAS) operations
 * 3. Atomic arrays
 * 4. Atomic field updaters
 * 5. Atomic accumulators and adders
 */
public class AtomicVariablesExample {

    // Regular counter without thread safety
    static class UnsafeCounter {
        private int count = 0;

        public void increment() {
            count++;
        }

        public int getCount() {
            return count;
        }
    }

    // Thread-safe counter using AtomicInteger
    static class AtomicCounter {
        private AtomicInteger count = new AtomicInteger(0);

        public void increment() {
            // Atomically increments by one and returns the previous value
            count.getAndIncrement();

            // Alternative ways to increment:
            // count.incrementAndGet(); // Atomically increments by one and returns the updated value
            // count.addAndGet(1);      // Atomically adds the given value and returns the updated value
            // count.getAndAdd(1);      // Atomically adds the given value and returns the previous value
        }

        public int getCount() {
            return count.get();
        }
    }

    /**
     * Demonstrates basic atomic types and operations.
     */
    private static void demonstrateBasicAtomicTypes() {
        System.out.println("\n--- Basic Atomic Types Example ---");

        // AtomicInteger example
        AtomicInteger atomicInt = new AtomicInteger(10);
        System.out.println("AtomicInteger initial value: " + atomicInt.get());

        // Increment and get the new value
        int newValue = atomicInt.incrementAndGet();
        System.out.println("After incrementAndGet(): " + newValue);

        // Add a value and get the old value
        int oldValue = atomicInt.getAndAdd(5);
        System.out.println("getAndAdd(5) returned old value: " + oldValue);
        System.out.println("New value after adding 5: " + atomicInt.get());

        // Set to a new value and get the old value
        oldValue = atomicInt.getAndSet(20);
        System.out.println("getAndSet(20) returned old value: " + oldValue);
        System.out.println("New value after set: " + atomicInt.get());

        // AtomicBoolean example
        AtomicBoolean atomicBool = new AtomicBoolean(false);
        System.out.println("\nAtomicBoolean initial value: " + atomicBool.get());

        // Set to true if current value is false (returns true if changed)
        boolean changed = atomicBool.compareAndSet(false, true);
        System.out.println("Value was changed: " + changed);
        System.out.println("New value: " + atomicBool.get());

        // Try to set again with wrong expected value (won't change)
        changed = atomicBool.compareAndSet(false, true);
        System.out.println("Value was changed: " + changed + " (expected false, but was true)");

        // AtomicReference example
        AtomicReference<String> atomicRef = new AtomicReference<>("initial");
        System.out.println("\nAtomicReference initial value: " + atomicRef.get());

        // Update the reference
        atomicRef.set("updated");
        System.out.println("After set(): " + atomicRef.get());

        // Compare and set
        boolean success = atomicRef.compareAndSet("updated", "final");
        System.out.println("compareAndSet succeeded: " + success);
        System.out.println("Final value: " + atomicRef.get());
    }

    /**
     * Demonstrates the power of Compare-And-Swap (CAS) operations.
     */
    private static void demonstrateCAS() {
        System.out.println("\n--- Compare-And-Swap (CAS) Example ---");

        AtomicInteger value = new AtomicInteger(100);

        // Simulate a thread that wants to update the value only if it hasn't changed
        boolean updated = value.compareAndSet(100, 200);
        System.out.println("First update succeeded: " + updated);
        System.out.println("Value is now: " + value.get());

        // Another thread tries to do the same, but the value has already changed
        updated = value.compareAndSet(100, 300);
        System.out.println("Second update succeeded: " + updated + " (expected 100, but was 200)");
        System.out.println("Value remains: " + value.get());

        // Using updateAndGet for atomic updates with a function
        int result = value.updateAndGet(x -> x * 2);
        System.out.println("After doubling: " + result);

        // Using accumulateAndGet for atomic updates with a binary operator
        result = value.accumulateAndGet(50, (x, y) -> x + y);
        System.out.println("After adding 50: " + result);
    }

    /**
     * Demonstrates atomic arrays for thread-safe operations on array elements.
     */
    private static void demonstrateAtomicArrays() {
        System.out.println("\n--- Atomic Arrays Example ---");

        // Create an atomic integer array with 5 elements
        AtomicIntegerArray atomicArray = new AtomicIntegerArray(5);

        // Set initial values
        for (int i = 0; i < atomicArray.length(); i++) {
            atomicArray.set(i, i * 10);
        }

        // Print initial values
        System.out.println("Initial array values:");
        for (int i = 0; i < atomicArray.length(); i++) {
            System.out.println("atomicArray[" + i + "] = " + atomicArray.get(i));
        }

        // Perform atomic operations on array elements
        atomicArray.getAndIncrement(1); // Increment index 1
        atomicArray.getAndAdd(2, 5); // Add 5 to index 2
        atomicArray.getAndSet(3, 100); // Set index 3 to 100

        // Perform a CAS operation on index 4
        boolean success = atomicArray.compareAndSet(4, 40, 400);

        // Print updated values
        System.out.println("\nUpdated array values:");
        for (int i = 0; i < atomicArray.length(); i++) {
            System.out.println("atomicArray[" + i + "] = " + atomicArray.get(i));
        }
    }

    /**
     * Demonstrates atomic field updaters that allow atomic operations on fields of existing classes.
     */
    private static void demonstrateFieldUpdaters() {
        System.out.println("\n--- Atomic Field Updaters Example ---");

        // Create a class with a volatile field
        class User {
            volatile int id;
            volatile String name;

            User(int id, String name) {
                this.id = id;
                this.name = name;
            }

            @Override
            public String toString() {
                return "User{id=" + id + ", name='" + name + "'}";
            }
        }

        // Create atomic updaters for the fields
        AtomicIntegerFieldUpdater<User> idUpdater = AtomicIntegerFieldUpdater.newUpdater(User.class, "id");

        AtomicReferenceFieldUpdater<User, String> nameUpdater =
                AtomicReferenceFieldUpdater.newUpdater(User.class, String.class, "name");

        // Create a user
        User user = new User(1, "Alice");
        System.out.println("Initial user: " + user);

        // Update the id field atomically
        idUpdater.getAndIncrement(user);
        System.out.println("After incrementing id: " + user);

        // Update the name field atomically
        nameUpdater.compareAndSet(user, "Alice", "Bob");
        System.out.println("After updating name: " + user);
    }

    /**
     * Demonstrates LongAdder and LongAccumulator for high-concurrency scenarios.
     */
    private static void demonstrateAdders() {
        System.out.println("\n--- Adders and Accumulators Example ---");

        // LongAdder is designed for high-concurrency counting
        LongAdder adder = new LongAdder();

        // Create a thread pool
        ExecutorService executor = Executors.newFixedThreadPool(4);

        // Submit 100 increment tasks
        for (int i = 0; i < 100; i++) {
            executor.submit(adder::increment);
        }

        // Shutdown and wait for completion
        executor.shutdown();
        try {
            executor.awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("Final LongAdder sum: " + adder.sum());

        // LongAccumulator with a custom accumulation function (max value)
        LongAccumulator maxAccumulator = new LongAccumulator(Long::max, 0);

        // Submit tasks that update with random values
        executor = Executors.newFixedThreadPool(4);
        for (int i = 0; i < 100; i++) {
            final int value = i;
            executor.submit(() -> maxAccumulator.accumulate(value));
        }

        // Shutdown and wait for completion
        executor.shutdown();
        try {
            executor.awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("Maximum value found: " + maxAccumulator.get());
    }

    /**
     * Compares performance of regular counter vs atomic counter.
     */
    private static void compareCounterPerformance() {
        System.out.println("\n--- Counter Performance Comparison ---");

        final int NUM_THREADS = 10;
        final int INCREMENTS_PER_THREAD = 100_000;

        // Test unsafe counter
        UnsafeCounter unsafeCounter = new UnsafeCounter();
        ExecutorService executor = Executors.newFixedThreadPool(NUM_THREADS);

        long startTime = System.nanoTime();

        for (int i = 0; i < NUM_THREADS; i++) {
            executor.submit(() -> {
                for (int j = 0; j < INCREMENTS_PER_THREAD; j++) {
                    unsafeCounter.increment();
                }
            });
        }

        executor.shutdown();
        try {
            executor.awaitTermination(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        long endTime = System.nanoTime();
        long unsafeDuration = (endTime - startTime) / 1_000_000; // Convert to milliseconds

        System.out.println("Unsafe counter final value: " + unsafeCounter.getCount());
        System.out.println("Expected value: " + (NUM_THREADS * INCREMENTS_PER_THREAD));
        System.out.println("Unsafe counter time: " + unsafeDuration + " ms");

        // Test atomic counter
        AtomicCounter atomicCounter = new AtomicCounter();
        executor = Executors.newFixedThreadPool(NUM_THREADS);

        startTime = System.nanoTime();

        for (int i = 0; i < NUM_THREADS; i++) {
            executor.submit(() -> {
                for (int j = 0; j < INCREMENTS_PER_THREAD; j++) {
                    atomicCounter.increment();
                }
            });
        }

        executor.shutdown();
        try {
            executor.awaitTermination(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        endTime = System.nanoTime();
        long atomicDuration = (endTime - startTime) / 1_000_000; // Convert to milliseconds

        System.out.println("\nAtomic counter final value: " + atomicCounter.getCount());
        System.out.println("Expected value: " + (NUM_THREADS * INCREMENTS_PER_THREAD));
        System.out.println("Atomic counter time: " + atomicDuration + " ms");

        System.out.println("\nConclusion:");
        System.out.println("- Unsafe counter is "
                + (unsafeCounter.getCount() == NUM_THREADS * INCREMENTS_PER_THREAD
                        ? "correct"
                        : "INCORRECT (race conditions)"));
        System.out.println("- Atomic counter is "
                + (atomicCounter.getCount() == NUM_THREADS * INCREMENTS_PER_THREAD ? "correct" : "incorrect"));
    }

    /**
     * Runs all atomic variables examples.
     */
    public static void runExample() {
        System.out.println("\n=== Atomic Variables Example ===");

        demonstrateBasicAtomicTypes();
        demonstrateCAS();
        demonstrateAtomicArrays();
        demonstrateFieldUpdaters();
        demonstrateAdders();
        compareCounterPerformance();

        System.out.println("Atomic Variables Example completed");
    }
}
