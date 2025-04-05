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

public class NotificationStartupActivity implements StartupActivity {

    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    @Override
    public void runActivity(@NotNull Project project) {

        // Access the settings instance
        MyAppSettings settings = MyAppSettings.Companion.getInstance();

        // Get the workTime and breakTime values from settings
        int workTime = settings.getState().getWorkTime();  // Get work time from the state
        System.out.println(workTime);
        int breakTime = settings.getState().getBreakTime();  // Get break time from the state


        scheduler.scheduleAtFixedRate(() -> ApplicationManager.getApplication().invokeLater(() -> {
            NotificationGroupManager.getInstance()
                    .getNotificationGroup("com.ctrlaltdefeat.notifications")
                    .createNotification(
                            "Notificare pentru JEGOSI",
                            "SPALATE LA CUR!!!!",
                            NotificationType.INFORMATION
                    )
                    .notify(project);
        }), 0, workTime, TimeUnit.SECONDS);
    }
}
