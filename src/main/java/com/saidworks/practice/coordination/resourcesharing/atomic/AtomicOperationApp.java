/* Said Zitouni (C)2025 */
package com.saidworks.practice.coordination.resourcesharing.atomic;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AtomicOperationApp {
    private static final Logger logger = LogManager.getLogger(AtomicOperationApp.class);

    public static void main(String[] args) throws InterruptedException {
        InventoryAtomicCounter inventoryCounter = new InventoryAtomicCounter();
        DecrementingAtomicThread incrementingThread = new DecrementingAtomicThread(inventoryCounter);
        IncrementingAtomicThread decrementingThread = new IncrementingAtomicThread(inventoryCounter);

        // start threads
        incrementingThread.start();
        decrementingThread.start();
        // join the threads after start here lead to
        incrementingThread.join();
        decrementingThread.join();

        logger.info("Inventory counter of items return : {}", inventoryCounter.getItems());
    }
}
