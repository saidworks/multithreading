/* ADSK  | Product Accesss SZ Q2 Goal (C)2025 */
package com.saidworks;

import java.util.*;
import org.springframework.scheduling.config.Task;

/*
   Thread Creation - MultiExecutor

   In this exercise, we are going to implement a  MultiExecutor .

   The client of this class will create a list of Runnable tasks and provide that list into MultiExecutor's constructor.

   When the client runs the  executeAll(),  the MultiExecutor,  will execute all the given tasks.

   To take full advantage of our multicore CPU, we would like the MultiExecutor to execute all the tasks in parallel by passing each task
    to a different thread.



*/
public class MultiExecutor {

  private List<Runnable> tasks = new ArrayList<>();
  private Task task;

  /*
   * @param tasks to executed concurrently
   */
  public MultiExecutor(List<Runnable> tasks) {
    this.tasks = tasks;
  }

  /** Starts and executes all the tasks concurrently */
  public void executeAll() {
    List<Thread> threads = new ArrayList<>();
    for (Runnable task : tasks) {
      threads.add(new Thread(task));
    }
    for (Thread thread : threads) {
      thread.start();
    }
  }
}
