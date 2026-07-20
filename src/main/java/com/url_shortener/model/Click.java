package com.url_shortener.model;

import java.sql.Timestamp;

public class Click {

  private long id;
  private long urlId;
  private Timestamp clickedAt;
  private String ipAddress;
  private String userAgent;
  private String country;
  private String deviceType;

  public Click() {}

  public Click(long id, long urlId, Timestamp clickedAt, String ipAddress, String userAgent, String country, String deviceType) {
    this.id = id;
    this.urlId = urlId;
    this.clickedAt = clickedAt;
    this.ipAddress = ipAddress;
    this.userAgent = userAgent;
    this.country = country;
    this.deviceType = deviceType;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public long getUrlId() {
    return urlId;
  }

  public void setUrlId(long urlId) {
    this.urlId = urlId;
  }

  public Timestamp getClickedAt() {
    return clickedAt;
  }

  public void setClickedAt(Timestamp clickedAt) {
    this.clickedAt = clickedAt;
  }

  public String getIpAddress() {
    return ipAddress;
  }

  public void setIpAddress(String ipAddress) {
    this.ipAddress = ipAddress;
  }

  public String getUserAgent() {
    return userAgent;
  }

  public void setUserAgent(String userAgent) {
    this.userAgent = userAgent;
  }

  public String getCountry() {
    return country;
  }

  public void setCountry(String country) {
    this.country = country;
  }

  public String getDeviceType() {
    return deviceType;
  }

  public void setDeviceType(String deviceType) {
    this.deviceType = deviceType;
  }
}