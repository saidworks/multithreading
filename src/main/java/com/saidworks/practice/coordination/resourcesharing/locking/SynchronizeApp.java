/* Said Zitouni (C)2025 */
package com.saidworks.practice.coordination.resourcesharing.locking;

import com.saidworks.practice.coordination.resourcesharing.DecrementingThread;
import com.saidworks.practice.coordination.resourcesharing.IncrementingThread;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SynchronizeApp {
    private static final Logger logger = LogManager.getLogger(SynchronizeApp.class);

    public static void main(String[] args) throws InterruptedException {
        InventoryCounterSyncWithLocked inventoryCounterSynchronized = new InventoryCounterSyncWithLocked();
        IncrementingThread incrementingThread = new IncrementingThread(inventoryCounterSynchronized);
        DecrementingThread decrementingThread = new DecrementingThread(inventoryCounterSynchronized);

        // start threads
        incrementingThread.start();
        decrementingThread.start();
        // join the threads after start here lead to
        incrementingThread.join();
        decrementingThread.join();

        logger.info("Inventory counter of items return : {}", inventoryCounterSynchronized.getItems());
    }
}
