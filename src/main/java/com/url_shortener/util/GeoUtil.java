package com.url_shortener.util;

public class GeoUtil {
  public static String getCountry(String ip) {
    try {
      java.net.URL url = new java.net.URL("http://ip-api.com/json/" + ip);
      java.net.HttpURLConnection conn = (java.net.HttpURLConnection) url.openConnection();

      conn.setRequestMethod("GET");

      java.io.BufferedReader reader =
          new java.io.BufferedReader(new java.io.InputStreamReader(conn.getInputStream()));

      StringBuilder response = new StringBuilder();
      String line;

      while ((line = reader.readLine()) != null) {
        response.append(line);
      }

      reader.close();

      String res = response.toString();

      int start = res.indexOf("\"country\":\"") + 11;
      int end = res.indexOf("\"", start);

      return res.substring(start, end);

    } catch (Exception e) {
      return "unknown";
    }
  }
}