package com.url_shortener.controller;

import com.url_shortener.service.UrlService;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import javax.servlet.annotation.*;

@WebServlet("/shorten")
public class CreateUrlServlet extends HttpServlet {
  private final UrlService urlService = new UrlService();

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
    res.setContentType("application/json");

    String longUrl = req.getParameter("url");
    PrintWriter out = res.getWriter();

    try {
      String shortCode = urlService.createShortUrl(longUrl);

      out.write("{\"url\":\"" + shortCode + "\"}");
    } catch (IllegalArgumentException e) {
      res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      out.write("{\"error\":\"" + e.getMessage() + "\"}");
    } catch (Exception e) {
      res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
      out.write("{\"error\":\"Something went wrong\"}");
    }
  }
}
