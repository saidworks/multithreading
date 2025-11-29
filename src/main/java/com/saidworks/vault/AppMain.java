/* ADSK  | Product Accesss SZ Q2 Goal (C)2025 */
package com.saidworks.vault;

import java.util.*;

public class AppMain {
  public static void main(String[] args) {
    Random rand = new Random();
    Vault vault = new Vault(rand.nextInt());
    List<Thread> threads = new ArrayList<>();
    threads.add(new AscendingHackerThread(vault));
    threads.add(new DescendingHackerThread(vault));
    threads.add(new PoliceThread());
    for (Thread t : threads) {
      t.start();
    }
  }
}
