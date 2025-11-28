/* Said Zitouni (C)2025 */
package com.saidworks.practice.metrics;

public class Metrics {
    private long count = 0;
    private volatile double average = 0.0;
    private static int instanceReference = 0;

    public synchronized void addSample(long sample) {
        double currentSum = average * count;
        count++;
        average = (currentSum + sample) / count;
    }

    public double getAverage() {
        return average;
    }

    public synchronized int getInstanceReference() {
        return instanceReference++;
    }
}
