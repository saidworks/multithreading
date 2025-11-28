/* Said Zitouni (C)2025 */
package com.saidworks.practice.coordination.resourcesharing.locking;

import com.saidworks.practice.coordination.resourcesharing.InventoryCounter;

public class InventoryCounterSyncWithLocked extends InventoryCounter {
    private int items = 0;
    final Object lock = new Object();

    @Override
    public void increment() {
        synchronized (this.lock) {
            items++;
        }
    }

    @Override
    public void decrement() {
        synchronized (this.lock) {
            items--;
        }
    }

    @Override
    public int getItems() {
        synchronized (this.lock) {
            return items;
        }
    }
}
