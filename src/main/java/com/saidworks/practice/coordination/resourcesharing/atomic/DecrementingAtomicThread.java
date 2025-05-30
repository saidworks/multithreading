/* Said Zitouni (C)2025 */
package com.saidworks.practice.coordination.resourcesharing.atomic;

public class DecrementingAtomicThread extends Thread {
    private InventoryAtomicCounter inventoryAtomicCounter;

    public DecrementingAtomicThread(InventoryAtomicCounter inventoryAtomicCounter) {
        this.inventoryAtomicCounter = inventoryAtomicCounter;
    }

    @Override
    public void run() {
        for (int i = 0; i < 10000; i++) {
            inventoryAtomicCounter.decrement();
        }
    }
}
