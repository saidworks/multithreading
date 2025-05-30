/* Said Zitouni (C)2025 */
package com.saidworks;

import com.saidworks.examples.*;
import java.util.Scanner;

/**
 * Main class for the Multithreading Lab.
 * This program provides a menu-driven interface to explore various multithreading concepts in Java.
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("=================================================");
        System.out.println("       JAVA MULTITHREADING EXAMPLES LAB          ");
        System.out.println("=================================================");
        System.out.println("This program demonstrates various multithreading concepts in Java.");
        System.out.println("Each example includes detailed explanations for beginners.");

        Scanner scanner = new Scanner(System.in);
        boolean exit = false;

        while (!exit) {
            displayMenu();
            System.out.print("\nEnter your choice (0-12): ");

            try {
                int choice = scanner.nextInt();

                switch (choice) {
                    case 0:
                        exit = true;
                        System.out.println("Exiting the program. Goodbye!");
                        break;
                    case 1:
                        runExample("Basic Thread Creation", BasicThreadExample::runExample);
                        break;
                    case 2:
                        runExample("Runnable Interface", RunnableExample::runExample);
                        break;
                    case 3:
                        runExample("Thread Synchronization", SynchronizationExample::runExample);
                        break;
                    case 4:
                        runExample("Wait/Notify Mechanism", WaitNotifyExample::runExample);
                        break;
                    case 5:
                        runExample("Thread Pools", ThreadPoolExample::runExample);
                        break;
                    case 6:
                        runExample("Callable and Future", CallableFutureExample::runExample);
                        break;
                    case 7:
                        runExample("CompletableFuture", CompletableFutureExample::runExample);
                        break;
                    case 8:
                        runExample("Atomic Variables", AtomicVariablesExample::runExample);
                        break;
                    case 9:
                        runExample("Locks and Conditions", LocksExample::runExample);
                        break;
                    case 10:
                        runExample("ThreadLocal", ThreadLocalExample::runExample);
                        break;
                    case 11:
                        runExample("Parallel Streams", ParallelStreamsExample::runExample);
                        break;
                    case 12:
                        runAllExamples();
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
                scanner.nextLine(); // Clear the scanner buffer
            }

            if (!exit) {
                System.out.println("\nPress Enter to continue...");
                scanner.nextLine(); // Consume the newline
                if (scanner.hasNextLine()) {
                    scanner.nextLine(); // Wait for user input
                }
            }
        }

        scanner.close();
    }

    /**
     * Displays the main menu of available examples.
     */
    private static void displayMenu() {
        System.out.println("\n=================================================");
        System.out.println("                  MAIN MENU                      ");
        System.out.println("=================================================");
        System.out.println(" 1. Basic Thread Creation and Execution");
        System.out.println(" 2. Runnable Interface Implementation");
        System.out.println(" 3. Thread Synchronization");
        System.out.println(" 4. Thread Communication (wait/notify)");
        System.out.println(" 5. Thread Pools and Executor Framework");
        System.out.println(" 6. Callable and Future");
        System.out.println(" 7. CompletableFuture for Asynchronous Programming");
        System.out.println(" 8. Atomic Variables");
        System.out.println(" 9. Locks and Conditions");
        System.out.println("10. ThreadLocal");
        System.out.println("11. Parallel Streams");
        System.out.println("12. Run All Examples");
        System.out.println(" 0. Exit");
    }

    /**
     * Runs a specific example with a header and footer.
     *
     * @param title The title of the example
     * @param example A Runnable that executes the example
     */
    private static void runExample(String title, Runnable example) {
        System.out.println("\n=================================================");
        System.out.println("          RUNNING: " + title.toUpperCase());
        System.out.println("=================================================");

        try {
            example.run();
        } catch (Exception e) {
            System.out.println("Error running example: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("\n=================================================");
        System.out.println("          COMPLETED: " + title.toUpperCase());
        System.out.println("=================================================");
    }

    /**
     * Runs all examples sequentially.
     */
    private static void runAllExamples() {
        System.out.println("\n=================================================");
        System.out.println("          RUNNING ALL EXAMPLES");
        System.out.println("=================================================");

        runExample("Basic Thread Creation", BasicThreadExample::runExample);
        runExample("Runnable Interface", RunnableExample::runExample);
        runExample("Thread Synchronization", SynchronizationExample::runExample);
        runExample("Wait/Notify Mechanism", WaitNotifyExample::runExample);
        runExample("Thread Pools", ThreadPoolExample::runExample);
        runExample("Callable and Future", CallableFutureExample::runExample);
        runExample("CompletableFuture", CompletableFutureExample::runExample);
        runExample("Atomic Variables", AtomicVariablesExample::runExample);
        runExample("Locks and Conditions", LocksExample::runExample);
        runExample("ThreadLocal", ThreadLocalExample::runExample);
        runExample("Parallel Streams", ParallelStreamsExample::runExample);

        System.out.println("\n=================================================");
        System.out.println("          ALL EXAMPLES COMPLETED");
        System.out.println("=================================================");
    }
}
