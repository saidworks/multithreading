/* Said Zitouni (C)2025 */
package com.saidworks.examples;

/**
 * This class demonstrates creating threads using the Runnable interface.
 *
 * Implementing Runnable is generally preferred over extending Thread because:
 * 1. It doesn't waste your single inheritance opportunity
 * 2. It better separates the task (what to do) from the thread (how to run)
 * 3. The same Runnable can be passed to multiple threads
 * 4. It's more compatible with executor frameworks
 */
public class RunnableExample {

    /**
     * This class implements the Runnable interface to define a task.
     * The run() method contains the code that will be executed in the thread.
     */
    static class MyRunnable implements Runnable {
        private final String name;

        public MyRunnable(String name) {
            this.name = name;
        }

        @Override
        public void run() {
            // This code will run in a separate thread
            System.out.println("Runnable task started: " + name);

            try {
                // Simulate some work by sleeping
                for (int i = 1; i <= 5; i++) {
                    Thread.sleep(500); // Pause for 500 milliseconds
                    System.out.println(name + " is working: step " + i);
                }
            } catch (InterruptedException e) {
                System.out.println(name + " was interrupted!");
                // Restore the interrupted status
                Thread.currentThread().interrupt();
            }

            System.out.println("Runnable task finished: " + name);
        }
    }

    /**
     * Demonstrates how to create and start threads using Runnable.
     */
    public static void runExample() {
        System.out.println("\n=== Runnable Example ===");

        // Create Runnable instances
        Runnable task1 = new MyRunnable("Task-1");
        Runnable task2 = new MyRunnable("Task-2");

        // Create threads with the Runnable tasks
        Thread thread1 = new Thread(task1);
        Thread thread2 = new Thread(task2);

        // Start the threads
        System.out.println("Starting threads with Runnable tasks...");
        thread1.start();
        thread2.start();

        // Using lambda expression (Java 8+) to create a Runnable
        Thread thread3 = new Thread(() -> {
            System.out.println("Lambda Runnable started");
            try {
                Thread.sleep(1000);
                System.out.println("Lambda Runnable working...");
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            System.out.println("Lambda Runnable finished");
        });

        System.out.println("Starting lambda thread...");
        thread3.start();

        try {
            // Wait for all threads to complete
            thread1.join();
            thread2.join();
            thread3.join();
            System.out.println("All Runnable threads have finished execution");
        } catch (InterruptedException e) {
            System.out.println("Main thread was interrupted!");
            Thread.currentThread().interrupt();
        }

        System.out.println("Runnable Example completed");
    }
}
