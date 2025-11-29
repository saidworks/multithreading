/* ADSK  | Product Accesss SZ Q2 Goal (C)2025 */
package com.saidworks.threadpool;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

@RestController
public class DemoDefferedController {
  @GetMapping("/deferred")
  public DeferredResult<String> getAsyncResult() {
    DeferredResult<String> result = new DeferredResult<>();
    new Thread(
            () -> {
              try {
                Thread.sleep(500);
              } catch (InterruptedException ignored) {
              }
              result.setResult("Result from DeferredResult");
            })
        .start();
    return result;
  }
}
