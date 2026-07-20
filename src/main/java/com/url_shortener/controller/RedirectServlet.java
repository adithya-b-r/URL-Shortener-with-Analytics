package com.url_shortener.controller;

import com.url_shortener.model.Url;
import com.url_shortener.service.ClickService;
import com.url_shortener.service.UrlService;

import javax.servlet.annotation.*;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/u/*")
public class RedirectServlet extends HttpServlet {

  private final UrlService urlService = new UrlService();
  private final ClickService clickService = new ClickService();

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse res)
      throws IOException {

    String path = req.getPathInfo();

    if (path == null || path.equals("/")) {
      res.getWriter().write("URL Shortener Running...");
      return;
    }

    String shortCode = path.substring(1).trim();

    try {
      Url url = urlService.getUrlByShortCode(shortCode);

      clickService.trackClick(url, req);

      urlService.incrementClick(url.getId());

      res.sendRedirect(url.getLongUrl());

    } catch (IllegalArgumentException e) {
      res.setContentType("application/json");
      res.setStatus(HttpServletResponse.SC_NOT_FOUND);
      res.getWriter().write("{\"error\":\"" + e.getMessage() + "\"}");

    } catch (Exception e) {
      res.setContentType("application/json");
      res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
      res.getWriter().write("{\"error\":\"Something went wrong\"}");
    }
  }
}