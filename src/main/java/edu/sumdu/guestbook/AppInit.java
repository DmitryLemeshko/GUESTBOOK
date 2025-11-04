package edu.sumdu.guestbook;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;

public class AppInit implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            new CommentDao().init(); // створює таблицю comments, якщо її ще немає
            System.out.println("[INFO] Database initialized successfully.");
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize database", e);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        // нічого робити не потрібно
    }
}
