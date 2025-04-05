package com.ctrlaltdefeat.notifications;

import com.ctraltdefeat.settings.MyAppSettings;
import com.intellij.notification.NotificationGroupManager;
import com.intellij.notification.NotificationType;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.startup.StartupActivity;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import java.util.concurrent.ScheduledFuture;

public class NotificationStartupActivity implements StartupActivity {

    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private ScheduledFuture<?> scheduledTask;

    @Override
    public void runActivity(@NotNull Project project) {
        MyAppSettings settings = MyAppSettings.Companion.getInstance();

        scheduleNotification(project, settings.getState().getWorkTime());
        // scheduleNotification(project, 5); //for debug

        settings.addChangeListener(() -> {
            int newWorkTime = settings.getState().getWorkTime();
            System.out.println("Settings changed! New workTime: " + newWorkTime);
            rescheduleNotification(project, newWorkTime);
            return null;
        });
    }

    private void scheduleNotification(Project project, int intervalInSeconds) {
        if (scheduledTask != null && !scheduledTask.isCancelled()) {
            scheduledTask.cancel(false);
        }

        scheduledTask = scheduler.scheduleAtFixedRate(() ->
                ApplicationManager.getApplication().invokeLater(() ->
                        NotificationGroupManager.getInstance()
                                .getNotificationGroup("com.ctrlaltdefeat.notifications")
                                .createNotification(
                                        "Notificare pentru relaxare",
                                        "Ridica-te si intinde-te, esti obosit!",
                                        NotificationType.INFORMATION
                                )
                                .notify(project)
                ), 0, intervalInSeconds, TimeUnit.SECONDS);
    }

    private void rescheduleNotification(Project project, int newInterval) {
        scheduleNotification(project, newInterval);
    }
}

