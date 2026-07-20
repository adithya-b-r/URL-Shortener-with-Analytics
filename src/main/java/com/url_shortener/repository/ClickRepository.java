package com.url_shortener.repository;

import com.url_shortener.model.Click;
import com.url_shortener.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ClickRepository {
  public void addAnalytics(Click click, long id) {
    String sql = "INSERT INTO clicks(url_id, clicked_at, ip_address, user_agent, country, device_type)VALUES(?,?,?,?,?,?)";

    try (Connection conn = DBConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);) {

      ps.setLong(1, id);
      ps.setTimestamp(2, click.getClickedAt());
      ps.setString(3, click.getIpAddress());
      ps.setString(4, click.getUserAgent());
      ps.setString(5, click.getCountry());
      ps.setString(6, click.getDeviceType());

      int row = ps.executeUpdate();

      if (row != 1) {
        throw new RuntimeException("Failed to insert analytics");
      }
    } catch (Exception e) {
      throw new RuntimeException("Error while inserting analytics", e);
    }
  }

  public List<Click> getAnalytics(long urlId, long page) {
    if (page < 1) {
      page = 1;
    }

    final int limit = 20;
    long offset = (page - 1) * limit;

    String sql = "SELECT * FROM clicks WHERE url_id=? ORDER BY id DESC LIMIT ?, ? ";

    try (
        Connection conn = DBConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);) {

      ps.setLong(1, urlId);
      ps.setInt(2, (int) offset);
      ps.setInt(3, limit);

      try (ResultSet rs = ps.executeQuery()) {

        List<Click> res = new ArrayList<>();

        while (rs.next()) {
          res.add(
              new Click(
                  rs.getLong("id"),
                  rs.getLong("url_id"),
                  rs.getTimestamp("clicked_at"),
                  rs.getString("ip_address"),
                  rs.getString("user_agent"),
                  rs.getString("country"),
                  rs.getString("device_type")));
        }

        return res;
      }
    } catch (Exception e) {
      throw new RuntimeException("Error while fetching analytics", e);
    }
  }
}
