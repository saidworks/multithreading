/* Said Zitouni (C)2025 */
package com.saidworks.practice.coordination.resourcesharing;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SharedResourcesProblemApp {
    private static final Logger logger = LogManager.getLogger(SharedResourcesProblemApp.class);

    public static void main(String[] args) throws InterruptedException {
        InventoryCounter inventoryCounter = new InventoryCounter();
        IncrementingThread incrementingThread = new IncrementingThread(inventoryCounter);
        DecrementingThread decrementingThread = new DecrementingThread(inventoryCounter);

        // start threads
        incrementingThread.start();
        decrementingThread.start();
        // join the threads after start here lead to
        incrementingThread.join();
        decrementingThread.join();

        logger.info("Inventory counter of items return : {}", inventoryCounter.getItems());
    }
}
