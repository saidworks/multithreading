# Java Multithreading Lab

A comprehensive educational project demonstrating various multithreading concepts in Java with detailed explanations for beginners.

## Overview

This project serves as a practical guide to Java multithreading, providing working examples of all major multithreading concepts. Each example is thoroughly documented with explanations suitable for beginners to understand the underlying concepts.

## Features

The project includes examples of the following multithreading concepts:

1. **Basic Thread Creation and Execution**
   - Creating threads by extending the Thread class
   - Starting, joining, and managing threads

2. **Runnable Interface Implementation**
   - Creating threads using the Runnable interface
   - Benefits over extending Thread class
   - Using lambda expressions for concise thread creation

3. **Thread Synchronization**
   - Understanding race conditions
   - Using synchronized methods and blocks
   - Object locks and class locks

4. **Thread Communication (wait/notify)**
   - Producer-consumer pattern
   - Using wait(), notify(), and notifyAll()
   - Thread coordination and signaling

5. **Thread Pools and Executor Framework**
   - Fixed thread pools
   - Cached thread pools
   - Scheduled thread pools
   - Custom thread factories

6. **Callable and Future**
   - Returning results from threads
   - Handling exceptions in threads
   - Timeouts and cancellation
   - Executing multiple tasks with invokeAll and invokeAny

7. **CompletableFuture for Asynchronous Programming**
   - Creating and completing CompletableFuture
   - Chaining asynchronous operations
   - Handling exceptions in asynchronous code
   - Combining multiple CompletableFutures

8. **Atomic Variables**
   - Thread-safe counters
   - Compare-and-swap operations
   - Atomic arrays and field updaters
   - Performance comparison with synchronized alternatives

9. **Locks and Conditions**
   - ReentrantLock for flexible locking
   - ReadWriteLock for concurrent reads
   - Condition variables for thread coordination
   - StampedLock for optimistic reading

10. **ThreadLocal**
    - Thread-local variables
    - Initial values and inheritance
    - Memory considerations and proper cleanup
    - Practical use cases like thread-safe date formatting

11. **Parallel Streams**
    - Creating and using parallel streams
    - Performance comparison with sequential streams
    - Common operations and pitfalls
    - Controlling parallelism level

## How to Use

1. Clone the repository
2. Open the project in your favorite Java IDE
3. Run the `Main` class
4. Use the menu to select which multithreading concept you want to explore
5. Follow the console output to understand the concepts in action

## Requirements

- Java 8 or higher
- Maven (for dependency management)

## Learning Path

For beginners, it's recommended to explore the examples in the following order:

1. Basic Thread Creation
2. Runnable Interface
3. Thread Synchronization
4. Wait/Notify Mechanism
5. Thread Pools
6. Callable and Future
7. CompletableFuture
8. Atomic Variables
9. Locks and Conditions
10. ThreadLocal
11. Parallel Streams

Each example builds on concepts from previous examples, providing a structured learning path.

## Contributing

Contributions are welcome! If you'd like to add more examples, improve existing ones, or enhance the documentation, please feel free to submit a pull request.

## License

This project is licensed under the MIT License - see the LICENSE file for details.