/* Said Zitouni (C)2025 */
package com.saidworks.practice.coordination.resourcesharing;

public class DecrementingThread extends Thread {
    private InventoryCounter inventoryCounter;

    public DecrementingThread(InventoryCounter inventoryCounter) {
        this.inventoryCounter = inventoryCounter;
    }

    @Override
    public void run() {
        for (int i = 0; i < 10000; i++) {
            inventoryCounter.decrement();
        }
    }
}
