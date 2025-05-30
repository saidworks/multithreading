/* Said Zitouni (C)2025 */
package com.saidworks.practice.performance.tps;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.io.OutputStream;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class WordCountHandler implements HttpHandler {
    Logger logger = LogManager.getLogger(WordCountHandler.class);
    private String text;

    public WordCountHandler(String text) {
        this.text = text;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String query = exchange.getRequestURI().getQuery();
        String[] keyValue = query.split("=");
        String action = keyValue[0];
        String word = keyValue[1];
        if (!action.equals("word")) {
            exchange.sendResponseHeaders(400, 0);
            exchange.getResponseBody().close();
            logger.error("bad request {} {}", action, word);
            return;
        }
        long count = countWord(word);
        byte[] response = Long.toString(count).getBytes();
        exchange.sendResponseHeaders(200, response.length);
        OutputStream os = exchange.getResponseBody();
        os.write(response);
        logger.info("request processed | the word count for {} is {}", word, count);
        os.close();
    }

    private long countWord(String word) {
        long count = 0;
        int index = 0;
        while (index >= 0) {
            index = text.indexOf(word, index);
            if (index >= 0) {
                count++;
                index++;
            }
        }
        return count;
    }
}
