package com.url_shortener.model;

import java.sql.Timestamp;

public class Url {

  private long id;
  private String shortCode;
  private String longUrl;
  private Timestamp createdAt;
  private Timestamp expiresAt;
  private long clickCount;

  public Url() {}

  public Url(long id, String shortCode, String longUrl, Timestamp createdAt, Timestamp expiresAt, long clickCount) {
    this.id = id;
    this.shortCode = shortCode;
    this.longUrl = longUrl;
    this.createdAt = createdAt;
    this.expiresAt = expiresAt;
    this.clickCount = clickCount;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getShortCode() {
    return shortCode;
  }

  public void setShortCode(String shortCode) {
    this.shortCode = shortCode;
  }

  public String getLongUrl() {
    return longUrl;
  }

  public void setLongUrl(String longUrl) {
    this.longUrl = longUrl;
  }

  public Timestamp getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(Timestamp createdAt) {
    this.createdAt = createdAt;
  }

  public Timestamp getExpiresAt() {
    return expiresAt;
  }

  public void setExpiresAt(Timestamp expiresAt) {
    this.expiresAt = expiresAt;
  }

  public long getClickCount() {
    return clickCount;
  }

  public void setClickCount(long clickCount) {
    this.clickCount = clickCount;
  }
}