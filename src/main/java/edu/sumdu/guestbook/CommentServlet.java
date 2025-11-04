package edu.sumdu.guestbook;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;

@WebServlet(name = "CommentServlet", urlPatterns = {"/comments"})
public class CommentServlet extends HttpServlet {
    private CommentDao dao;
    private ObjectMapper mapper;

    @Override
    public void init() {
        dao = new CommentDao();
        mapper = new ObjectMapper();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        resp.setContentType("application/json;charset=UTF-8");
        try {
            List<Comment> comments = dao.getAllComments();
            String json = mapper.writeValueAsString(comments);
            PrintWriter out = resp.getWriter();
            out.print(json);
            out.flush();
        } catch (SQLException e) {
            resp.sendError(500, "Database error: " + e.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        String author = req.getParameter("author");
        String text = req.getParameter("text");

        if (author == null || author.isBlank() || author.length() > 64 ||
                text == null || text.isBlank() || text.length() > 1000) {
            resp.sendError(400, "Invalid input data");
            return;
        }

        try {
            dao.addComment(author, text);
            resp.setStatus(204); // Success, no content
        } catch (SQLException e) {
            resp.sendError(500, "Database error: " + e.getMessage());
        }
    }
}
