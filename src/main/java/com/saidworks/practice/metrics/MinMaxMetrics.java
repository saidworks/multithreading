/* Said Zitouni (C)2025 */
package com.saidworks.practice.metrics;

import java.util.ArrayList;
import java.util.List;

public class MinMaxMetrics extends Metrics{

    /*
        Min - Max Metrics

    In this exercise, we are going to implement a class called MinMaxMetrics .

    A single instance of this class will be passed to multiple threads in our application.

    MinMaxMetrics is an analytics class used to keep track of the minimum and the maximum of a particular business or performance metric in our application.

    Example:

    A stock trading application that keeps track of the minimum and maximum price of the stock daily.


    The class will have 3 methods:

    addSample(..) - Takes a new sample.

    getMin() - Returns the sample with the minimum value we have seen so far.

    getMax() - Returns the sample with the maximum value we have seen so far.
         */

    private final List<Long> samples;
    /**
     * Initializes all member variables
     */
    public MinMaxMetrics() {
        this.samples = new ArrayList<>();
    }

    /**
     * Adds a new sample to our metrics.
     */
    @Override
    public void addSample(long newSample) {
        this.samples.add(newSample);
    }

    /**
     * Returns the smallest sample we've seen so far.
     */
    public long getMin() {
        return this.samples.stream().min(Long::compare).orElse(-1L);
    }

    /**
     * Returns the biggest sample we've seen so far.
     */
    public long getMax() {
        return this.samples.stream().max(Long::compare).orElse(-1L);
    }
}
