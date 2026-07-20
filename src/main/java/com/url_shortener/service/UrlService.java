package com.url_shortener.service;

import com.url_shortener.model.Url;
import com.url_shortener.repository.UrlRepository;
import com.url_shortener.util.Base62;
import com.url_shortener.util.CheckValidUrl;

public class UrlService {

  private final UrlRepository urlRepository = new UrlRepository();

  public String createShortUrl(String longUrl) throws Exception {

    if (!CheckValidUrl.isValidURL(longUrl)) {
      throw new IllegalArgumentException("Invalid URL");
    }

    long id = urlRepository.saveURL(longUrl);

    String shortCode = Base62.encode(id);

    urlRepository.updateShortCode(id, shortCode);

    return shortCode;
  }

  public Url getUrlByShortCode(String shortCode) {

    if (shortCode == null || shortCode.isBlank()) {
      throw new IllegalArgumentException("Short code cannot be empty");
    }

    Url url = urlRepository.findByShortCode(shortCode);

    if (url == null) {
      throw new IllegalArgumentException("Short URL not found");
    }

    return url;
  }

  public void incrementClick(long id) {
    urlRepository.incrementClickCount(id);
  }
}