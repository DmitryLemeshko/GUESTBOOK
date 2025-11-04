package edu.sumdu.guestbook;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CommentDao {

    private static final String DB_URL = "jdbc:h2:file:./data/guest;AUTO_SERVER=TRUE";
    private static final String DB_USER = "sa";
    private static final String DB_PASSWORD = "";

    public CommentDao() {
        // Ініціалізуємо таблицю, якщо її ще немає
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("""
                CREATE TABLE IF NOT EXISTS comments (
                    id IDENTITY PRIMARY KEY,
                    author VARCHAR(64) NOT NULL,
                    text VARCHAR(1000) NOT NULL
                )
            """);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }

    public void addComment(String author, String text) throws SQLException {
        String sql = "INSERT INTO comments(author, text) VALUES (?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, author);
            stmt.setString(2, text);
            stmt.executeUpdate();
        }
    }

    public List<Comment> getAllComments() throws SQLException {
        List<Comment> comments = new ArrayList<>();
        String sql = "SELECT * FROM comments ORDER BY id DESC";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                comments.add(new Comment(
                        rs.getInt("id"),
                        rs.getString("author"),
                        rs.getString("text")
                ));
            }
        }
        return comments;
    }
}

