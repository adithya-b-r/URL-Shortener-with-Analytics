package com.url_shortener.util;

import java.net.URI;
import java.net.URISyntaxException;

public class CheckValidUrl {
  public static boolean isValidURL(String longUrl){
    try{
      URI uri = new URI(longUrl);

      return uri.getScheme() != null && uri.getHost() != null;
    }catch(URISyntaxException e){
      return false;
    }
  }
}
