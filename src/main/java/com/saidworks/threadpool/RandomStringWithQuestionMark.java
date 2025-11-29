/* ADSK  | Product Accesss SZ Q2 Goal (C)2025 */
package com.saidworks.threadpool;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomStringWithQuestionMark {
  private static final Random random = new Random();

  public static String generate(int length) {
    String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    StringBuilder sb = new StringBuilder(length);
    for (int i = 0; i < length - 1; i++) {
      sb.append(chars.charAt(random.nextInt(chars.length())));
    }
    sb.append('?');
    return sb.toString();
  }

  public static List<String> generateListOfStrings(int length, int wordLength) {
    List<String> list = new ArrayList<>();
    for (int i = 0; i < length; i++) {
      list.add(generate(wordLength));
    }
    return list;
  }
}
