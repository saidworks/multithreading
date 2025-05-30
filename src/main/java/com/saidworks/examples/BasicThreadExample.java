/* Said Zitouni (C)2025 */
package com.saidworks.examples;

/**
 * This class demonstrates the most basic way to create and start threads in Java.
 *
 * In Java, there are two main ways to create a thread:
 * 1. Extending the Thread class (shown in this example)
 * 2. Implementing the Runnable interface (shown in RunnableExample)
 */
public class BasicThreadExample {

    /**
     * This inner class extends Thread class to create a custom thread.
     * The run() method contains the code that will be executed in the thread.
     */
    static class MyThread extends Thread {
        private final String name;

        public MyThread(String name) {
            this.name = name;
        }

        @Override
        public void run() {
            // This code will run in a separate thread
            System.out.println("Thread started: " + name);

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

            System.out.println("Thread finished: " + name);
        }
    }

    /**
     * Demonstrates how to create, start, and work with basic threads.
     */
    public static void runExample() {
        System.out.println("\n=== Basic Thread Example ===");

        // Create two thread instances
        MyThread thread1 = new MyThread("Thread-1");
        MyThread thread2 = new MyThread("Thread-2");

        // Start the threads - this calls the run() method in each thread
        System.out.println("Starting threads...");
        thread1.start();
        thread2.start();

        // Main thread continues execution
        System.out.println("Main thread continues...");

        try {
            // Wait for both threads to complete before continuing
            thread1.join();
            thread2.join();
            System.out.println("All threads have finished execution");
        } catch (InterruptedException e) {
            System.out.println("Main thread was interrupted!");
            Thread.currentThread().interrupt();
        }

        System.out.println("Basic Thread Example completed");
    }
}
