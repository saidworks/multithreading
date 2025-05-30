/* Said Zitouni (C)2025 */
package com.saidworks.examples;

/**
 * This class demonstrates thread communication using wait() and notify() methods.
 *
 * The wait/notify mechanism allows threads to communicate with each other and
 * coordinate their actions. This is useful for producer-consumer scenarios and
 * other situations where threads need to wait for certain conditions.
 *
 * Key concepts demonstrated:
 * 1. Object.wait() - causes the current thread to wait until another thread calls notify()
 * 2. Object.notify() - wakes up a single thread that is waiting on this object
 * 3. Object.notifyAll() - wakes up all threads that are waiting on this object
 * 4. The importance of using these methods inside synchronized blocks
 */
public class WaitNotifyExample {

    /**
     * A simple message class that will be shared between threads.
     */
    static class Message {
        private String content;
        private boolean empty = true;

        // Producer calls this method
        public synchronized void put(String message) {
            // Wait until the message has been consumed
            while (!empty) {
                try {
                    // Release the lock and wait to be notified
                    wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    System.out.println("Producer was interrupted");
                    return;
                }
            }

            // Set the new message
            this.content = message;
            this.empty = false;

            // Notify consumer that a message is available
            notify();

            System.out.println("Producer: Message '" + message + "' sent");
        }

        // Consumer calls this method
        public synchronized String take() {
            // Wait until a message is available
            while (empty) {
                try {
                    // Release the lock and wait to be notified
                    wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    System.out.println("Consumer was interrupted");
                    return null;
                }
            }

            // Get the message
            String message = this.content;
            this.empty = true;

            // Notify producer that the message has been consumed
            notify();

            System.out.println("Consumer: Message '" + message + "' received");
            return message;
        }
    }

    /**
     * Demonstrates the wait/notify mechanism with a producer-consumer example.
     */
    public static void runExample() {
        System.out.println("\n=== Wait/Notify Example ===");

        // Create a shared message object
        final Message message = new Message();

        // Create producer thread
        Thread producerThread = new Thread(() -> {
            String[] messages = {"Hello", "How are you?", "I'm doing great!", "Wait/notify is powerful", "Goodbye!"};

            // Send each message
            for (String msg : messages) {
                message.put(msg);

                // Sleep a bit between messages
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        });

        // Create consumer thread
        Thread consumerThread = new Thread(() -> {
            // Receive 5 messages
            for (int i = 0; i < 5; i++) {
                message.take();

                // Sleep a bit between receiving messages
                try {
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        });

        // Start both threads
        System.out.println("Starting producer and consumer threads...");
        producerThread.start();
        consumerThread.start();

        try {
            // Wait for both threads to complete
            producerThread.join();
            consumerThread.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Main thread was interrupted");
        }

        System.out.println("Wait/Notify Example completed");
    }

    /**
     * Additional example showing notifyAll() with multiple waiting threads.
     * This is not called by default but demonstrates an important concept.
     */
    public static void demonstrateNotifyAll() {
        System.out.println("\n=== NotifyAll Example ===");

        final Object lock = new Object();

        // Create multiple waiting threads
        for (int i = 1; i <= 3; i++) {
            final int threadId = i;
            Thread waiter = new Thread(() -> {
                synchronized (lock) {
                    System.out.println("Thread " + threadId + " is waiting");
                    try {
                        lock.wait();
                        System.out.println("Thread " + threadId + " was notified and is running again");
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            });
            waiter.start();
        }

        // Give time for all threads to start waiting
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Notify all waiting threads
        synchronized (lock) {
            System.out.println("Main thread is notifying all waiting threads");
            lock.notifyAll();
        }
    }
}
