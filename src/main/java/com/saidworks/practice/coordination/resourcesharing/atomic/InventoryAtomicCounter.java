/* Said Zitouni (C)2025 */
package com.saidworks.practice.coordination.resourcesharing.atomic;

import java.util.concurrent.atomic.AtomicInteger;

public class InventoryAtomicCounter {
    private final AtomicInteger items = new AtomicInteger(0);

    public void increment() {
        items.incrementAndGet();
    }

    public void decrement() {
        items.decrementAndGet();
    }

    public int getItems() {
        return items.get();
    }
}
