/* Said Zitouni (C)2025 */
package com.saidworks.practice.coordination.resourcesharing.synchronization;

import com.saidworks.practice.coordination.resourcesharing.InventoryCounter;

public class InventoryCounterSynchronized extends InventoryCounter {
    private int items = 0;

    @Override
    public synchronized void increment() {
        items++;
    }

    @Override
    public synchronized void decrement() {
        items--;
    }

    @Override
    public int getItems() {
        return items;
    }
}
