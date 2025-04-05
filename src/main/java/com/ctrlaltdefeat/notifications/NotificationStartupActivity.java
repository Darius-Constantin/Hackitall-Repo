package com.ctrlaltdefeat.notifications;

import com.intellij.notification.NotificationGroupManager;
import com.intellij.notification.NotificationType;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.startup.StartupActivity;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class NotificationStartupActivity implements StartupActivity {

    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    @Override
    public void runActivity(@NotNull Project project) {

        //int interval = NotificationSettings.getInstance().getIntervalMinutes();
        int interval = 5;

        scheduler.scheduleAtFixedRate(() -> ApplicationManager.getApplication().invokeLater(() -> {
            NotificationGroupManager.getInstance()
                    .getNotificationGroup("com.ctrlaltdefeat.notifications")
                    .createNotification(
                            "Notificare pentru JEGOSI",
                            "SPALATE LA CUR!!!!",
                            NotificationType.INFORMATION
                    )
                    .notify(project);
        }), 0, interval, TimeUnit.SECONDS);
    }
}
