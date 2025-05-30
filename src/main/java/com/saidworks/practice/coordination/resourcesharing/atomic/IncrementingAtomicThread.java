/* Said Zitouni (C)2025 */
package com.saidworks.practice.coordination.resourcesharing.atomic;

public class IncrementingAtomicThread extends Thread {
    private InventoryAtomicCounter inventoryCounter;

    public IncrementingAtomicThread(InventoryAtomicCounter inventoryAtomicCounter) {
        this.inventoryCounter = inventoryAtomicCounter;
    }

    @Override
    public void run() {
        for (int i = 0; i < 10000; i++) {
            inventoryCounter.increment();
        }
    }
}
