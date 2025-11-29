/* ADSK  | Product Accesss SZ Q2 Goal (C)2025 */
package com.saidworks.threadpool;

import java.util.concurrent.Callable;

public class StringTaskCallable extends Thread implements Callable<String> {
  private final String input;

  public StringTaskCallable(String input) {
    this.input = input;
  }

  @Override
  public String call() {
    return this.input.replace("?", ".");
  }
}
