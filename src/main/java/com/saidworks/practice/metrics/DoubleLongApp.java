/* Said Zitouni (C)2025 */
package com.saidworks.practice.metrics;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DoubleLongApp {
    private static final Logger log = LogManager.getLogger(DoubleLongApp.class);

    public static void main(String[] args) {
        //        Metrics metrics = new Metrics();
        //        BusinessLogic logic1 = new BusinessLogic(metrics);
        //        BusinessLogic logic2 = new BusinessLogic(metrics);
        //        BusinessLogic logic3 = new BusinessLogic(metrics);
        //        MetricsPrinter printer = new MetricsPrinter(metrics);
        //        // start threads
        //        logic1.start();
        //        logic2.start();
        //        logic3.start();
        //        printer.start();
                MinMaxMetrics metrics = new MinMaxMetrics();
                BusinessLogicMinMax logic1 = new BusinessLogicMinMax(metrics);
                BusinessLogicMinMax logic2 = new BusinessLogicMinMax(metrics);
                BusinessLogicMinMax logic3 = new BusinessLogicMinMax(metrics);
                MinMaxMetricsPrinter printer = new MinMaxMetricsPrinter(metrics);
                // start threads
                Thread thread1 = new Thread(logic1);
                Thread thread2 = new Thread(logic2);
                Thread thread3 = new Thread(logic3);
                thread1.start();
                thread2.start();
                thread3.start();
                printer.start();

    }
}
