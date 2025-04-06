package com.ctrlaltdefeat.notifications;

import com.intellij.openapi.components.Service;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.application.ApplicationManager;
import lombok.Setter;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.Timer;
import java.util.TimerTask;

@Service
public final class ActivityTracker {
    @Setter
    private Project project;
    private long lastActivityTime = System.currentTimeMillis();
    private static final long IDLE_TIME_LIMIT = 10 * 1000 * 100; // 5 * 100 seconds in milliseconds
    private Timer timer;

    public ActivityTracker() {
        // Track keyboard and mouse events
        trackUserActivity();

        // Start a timer to check for idle time every second
        timer = new Timer(true);
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                checkIdleTime();
            }
        }, 0, 5 * 1000); // Check every second
    }

    private void trackUserActivity() {
        // Keyboard activity
        Toolkit.getDefaultToolkit().addAWTEventListener(e -> resetIdleTime(), KeyEvent.KEY_EVENT_MASK);

        // Mouse activity
        Toolkit.getDefaultToolkit().addAWTEventListener(e -> resetIdleTime(), MouseEvent.MOUSE_EVENT_MASK);
    }

    private void resetIdleTime() {
        lastActivityTime = System.currentTimeMillis();
    }

    private void checkIdleTime() {
        long idleTime = System.currentTimeMillis() - lastActivityTime;
        if (idleTime >= IDLE_TIME_LIMIT) {
            // Notify user after the idle period has passed
            sendIdleNotification();
        }
    }

    private void sendIdleNotification() {
        ApplicationManager.getApplication().invokeLater(() -> {
            // Send a notification if user is idle for too long
            String title = "Opaaaa";
            String content = "Se pare ca ai ramas impotmolit! Ai nevoie de ajutor?";
            String[] options = {"Dea", "Nah"};
            int choice = Messages.showDialog(
                project,
                content,
                title,
                options,
                0,
                Messages.getInformationIcon()
            );
            if (choice == 0) {
                System.out.println("Aici vine ajutor de la chat");
            }
            lastActivityTime = System.currentTimeMillis();
        });
    }
}
