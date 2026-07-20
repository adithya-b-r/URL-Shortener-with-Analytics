package com.url_shortener.util;

public class Base62 {
  private final static String chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

  public static String encode(long num) {
    if(num == 0){
      return String.valueOf(chars.charAt(0));
    }

    StringBuilder sb = new StringBuilder();

    while (num > 0) {
      int rem = (int) (num % 62);
      sb.append(chars.charAt(rem));
      num /= 62;
    }

    return sb.reverse().toString();
  }

  public static long decode(String str) {
    long num = 0;

    for (int i = 0; i < str.length(); i++) {
      num = num * 62 + chars.indexOf(str.charAt(i));
    }

    return num;
  }
}
