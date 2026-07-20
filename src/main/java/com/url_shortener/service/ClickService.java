package com.url_shortener.service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.url_shortener.model.Click;
import com.url_shortener.model.Url;
import com.url_shortener.repository.ClickRepository;
import com.url_shortener.repository.UrlRepository;
import com.url_shortener.util.GeoUtil;

public class ClickService {
  private final ClickRepository clickRepository = new ClickRepository();
  private final UrlRepository urlRepository = new UrlRepository();

  public static Timestamp getTimestamp() {
    LocalDateTime now = LocalDateTime.now();
    Timestamp tm = Timestamp.valueOf(now);

    return tm;
  }

  public void trackClick(Url url, HttpServletRequest req) {
    try {
      String ipAddress = req.getRemoteAddr();
      String userAgent = req.getHeader("User-Agent");
      String deviceType = userAgent.toLowerCase().contains("mobile") ? "mobile" : "desktop";
      String country = GeoUtil.getCountry(ipAddress);

      Click click = new Click(
          0,
          url.getId(),
          getTimestamp(),
          ipAddress,
          userAgent,
          country,
          deviceType);

      clickRepository.addAnalytics(click, url.getId());
    } catch (Exception e) {
      System.out.println("Analytics error: " + e.getMessage());
    }
  }

  public List<Click> getAnalytics(String shortCode, long page) {
    try {
      Url url = urlRepository.findByShortCode(shortCode);

      if (url == null) {
        throw new IllegalArgumentException("Short URL not found");
      }

      return clickRepository.getAnalytics(url.getId(), page);
    } catch (IllegalArgumentException e) {
      throw e;
    } catch (Exception e) {
      throw new RuntimeException("Error while fetching analytics", e);
    }
  }
}