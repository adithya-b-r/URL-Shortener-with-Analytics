package com.url_shortener.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;

import javax.servlet.annotation.*;
import javax.servlet.http.*;

import com.url_shortener.service.ClickService;
import com.url_shortener.model.Click;

@WebServlet("/analytics/*")
public class AnalyticsServlet extends HttpServlet {
  private String escape(String s) {
    return s == null ? "" : s.replace("\\", "\\\\").replace("\"", "\\\"");
  }

  private final ClickService clickService = new ClickService();

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
    String path = req.getPathInfo();

    if (path == null || path.equals("/")) {
      res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      return;
    }

    String shortCode = path.substring(1).trim();

    String pageParam = req.getParameter("page");
    long page = 1;

    // To handle non-numeric input, /abc for page param.
    try {
      if (pageParam != null) {
        page = Long.parseLong(pageParam);
      }
    } catch (NumberFormatException e) {
      page = 1;
    }

    res.setContentType("application/json");
    res.setCharacterEncoding("UTF-8");
    res.setHeader("Access-Control-Allow-Origin", "*");

    try {
      List<Click> clicks = clickService.getAnalytics(shortCode, page);

      StringBuilder json = new StringBuilder();

      json.append("{\"data\":[");

      for (int i = 0; i < clicks.size(); i++) {
        Click click = clicks.get(i);

        json.append("{")
            .append("\"ip\":\"").append(click.getIpAddress()).append("\",")
            .append("\"country\":\"").append(click.getCountry()).append("\",")
            .append("\"device\":\"").append(click.getDeviceType()).append("\",")
            .append("\"time\":\"").append(click.getClickedAt()).append("\",")
            .append("\"userAgent\":\"").append(escape(click.getUserAgent())).append("\"")
            .append("}");

        if (i != clicks.size() - 1) {
          json.append(",");
        }
      }

      json.append("]}");

      res.getWriter().write(json.toString());
    } catch (IllegalArgumentException e) {
      res.setStatus(HttpServletResponse.SC_NOT_FOUND);
      res.getWriter().write("{\"error\":\"Short URL not found\"}");
    } catch (Exception e) {
      res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
      res.getWriter().write("{\"error\":\"Something went wrong\"}");
    }
  }
}