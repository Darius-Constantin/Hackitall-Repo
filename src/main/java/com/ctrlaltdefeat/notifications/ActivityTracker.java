package com.ctrlaltdefeat.notifications;

import com.intellij.openapi.components.Service;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationGroupManager;
import com.intellij.notification.NotificationType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Timer;
import java.util.TimerTask;

@Service
public final class ActivityTracker {
    private long lastActivityTime = System.currentTimeMillis();
    private static final long IDLE_TIME_LIMIT = 5 * 60 * 1000; // 5 minutes in milliseconds
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
        }, 0, 1000); // Check every second
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
            Notification notification = NotificationGroupManager.getInstance()
                    .getNotificationGroup("com.ctrlaltdefeat.notifications")
                    .createNotification(
                            "Time to Take a Break!",
                            "You’ve been idle for 5 minutes. Please take a break and stretch!",
                            NotificationType.INFORMATION
                    );
            notification.notify(null);
        });
    }
}
