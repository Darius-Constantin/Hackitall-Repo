package com.ctrlaltdefeat.notifications;

import ai.grazie.utils.attributes.Attributes;
import com.intellij.openapi.components.Service;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationGroupManager;
import com.intellij.notification.NotificationType;
import lombok.Setter;
import openAI.MyOpenAIClient;

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
    @Setter
    private Project project;
    private long lastActivityTime = System.currentTimeMillis();
    private static final long IDLE_TIME_LIMIT = 5 * 1000; // 5 minutes in milliseconds
    private Timer timer;
    private Boolean alreadySent = false;

    CyclePhase phaseService = ApplicationManager.getApplication().getService(CyclePhase.class);

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
        }, 0, 5 * 1000); // Check every 5 seconds
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
        if (idleTime >= IDLE_TIME_LIMIT && !alreadySent) {
            // Notify user after the idle period has passed
            sendIdleNotification();
        }
    }

    private void sendIdleNotification() {
        if (!phaseService.isWorkPhase()) {
            return; // Don't show dialog if it's not work phase
        }
        alreadySent = true;
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
                // Generate help based on current file and editor
                ApplicationManager.getApplication().invokeLater(() -> {
                    try {
                        MyOpenAIClient client = new MyOpenAIClient();
                        String prompt = buildContextPrompt(project);
                        String suggestion = client.generateText(prompt, 300);

                        Messages.showDialog(
                                project,
                                suggestion,
                                "Sugestie inteligentă 💡",
                                new String[]{"Thanks!"}, // One button
                                0,
                                Messages.getInformationIcon()
                        );
                    } catch (Exception e) {
                        e.printStackTrace();
                        Messages.showErrorDialog(project, "Eroare la generarea sugestiei: " + e.getMessage(), "Oops");
                    }
                });
            }
            lastActivityTime = System.currentTimeMillis();
            alreadySent = false;
        });
    }

    private String buildContextPrompt(Project project) {
        var editor = com.intellij.openapi.fileEditor.FileEditorManager.getInstance(project).getSelectedTextEditor();
        if (editor == null) {
            return "The developer became idle, but no editor was active. Offer a general productivity suggestion." +
                    " Address to the first person direclty to him with no more explication";
        }

        var document = editor.getDocument();
        var text = document.getText();
        var file = com.intellij.openapi.fileEditor.FileDocumentManager.getInstance().getFile(document);
        String fileName = file != null ? file.getName() : "UnknownFile.java";

        // Optional: limit text length
        if (text.length() > 2000) {
            text = text.substring(0, 2000) + "\n\n... [truncated]";
        }

        return "The developer was idle while editing " + fileName +
                ". Here's the current code:\n\n" + text +
                "\n\nWhat are some helpful tips, possible next steps, or improvements they could consider?" +
                " Address to the first person direclty to him with no more explication";
    }

}