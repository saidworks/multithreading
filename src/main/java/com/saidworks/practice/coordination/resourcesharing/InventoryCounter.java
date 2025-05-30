/* Said Zitouni (C)2025 */
package com.saidworks.practice.coordination.resourcesharing;

public class InventoryCounter {
    private int items = 0;

    public void increment() {
        items++;
    }

    public void decrement() {
        items--;
    }

    public int getItems() {
        return items;
    }
}
