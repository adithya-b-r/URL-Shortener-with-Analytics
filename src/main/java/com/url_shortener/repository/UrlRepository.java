package com.url_shortener.repository;

import com.url_shortener.model.Url;
import com.url_shortener.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.ResultSet;
import java.time.LocalDateTime;

public class UrlRepository {
  public static Timestamp getTimestamp() {
    LocalDateTime now = LocalDateTime.now();
    Timestamp tm = Timestamp.valueOf(now);

    return tm;
  }

  public void incrementClickCount(Long id) {
    String sql = "UPDATE urls SET click_count=click_count+1 WHERE id=?";

    try(Connection conn = DBConnection.getConnection()){
      PreparedStatement ps = conn.prepareStatement(sql);
      ps.setLong(1, id);

      int rows = ps.executeUpdate();

      if(rows != 1){
        throw new RuntimeException("Failed to updating click count");
      }

    }catch(Exception e){
      throw new RuntimeException("Error while updating click count", e);
    }
  }

  public Url findByShortCode(String shortCode) {
    String sql = "SELECT * FROM urls WHERE short_code=?";

    try (Connection conn = DBConnection.getConnection()) {
      PreparedStatement ps = conn.prepareStatement(sql);

      ps.setString(1, shortCode);
      ResultSet rs = ps.executeQuery();

      if (rs.next()) {
        return new Url(
            rs.getLong("id"),
            rs.getString("short_code"),
            rs.getString("long_url"),
            rs.getTimestamp("created_at"),
            rs.getTimestamp("expires_at"),
            rs.getLong("click_count"));
      }

      return null;
    } catch (Exception e) {
      throw new RuntimeException("Error while retrieving url", e);
    }
  }

  public void updateShortCode(long id, String shortCode) {
    String sql = "UPDATE urls SET short_code=? where id=?";

    try (Connection conn = DBConnection.getConnection()) {
      PreparedStatement ps = conn.prepareStatement(sql);

      ps.setString(1, shortCode);
      ps.setLong(2, id);

      int rows = ps.executeUpdate();

      if (rows != 1) {
        throw new RuntimeException("Failed to  update short code");
      }
    } catch (Exception e) {
      throw new RuntimeException("Error while updating short code", e);
    }
  }

  public long saveURL(String longURL) {
    String sql = "INSERT INTO urls (long_url, created_at) VALUES (?, ?)";

    try (Connection conn = DBConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

      ps.setString(1, longURL);
      ps.setTimestamp(2, getTimestamp());

      int rows = ps.executeUpdate();

      if (rows != 1) {
        throw new RuntimeException("Error while inserting URL");
      }

      try (ResultSet rs = ps.getGeneratedKeys()) {
        if (rs.next()) {
          return rs.getLong(1);
        } else {
          throw new RuntimeException("Failed to retrieve generated ID");
        }
      }

    } catch (Exception e) {
      throw new RuntimeException("Error while saving URL", e);
    }
  }
}
