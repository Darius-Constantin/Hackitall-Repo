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
    private Phase currentPhase = Phase.WORK;

    private enum Phase {
        WORK,
        BREAK
    }

    @Override
    public void runActivity(@NotNull Project project) {
        MyAppSettings settings = MyAppSettings.Companion.getInstance();

        startCycle(project, settings.getState().getWorkTime());

        settings.addChangeListener(() -> {
            System.out.println("Settings changed!");
            restartCycle(project);
            return null;
        });
    }

    private void startCycle(Project project, int delayInSeconds) {
        if (scheduledTask != null && !scheduledTask.isCancelled()) {
            scheduledTask.cancel(false);
        }

        scheduledTask = scheduler.schedule(() ->
                ApplicationManager.getApplication().invokeLater(() -> {
                    showNotification(project);
                    togglePhaseAndContinue(project);
                }), delayInSeconds, TimeUnit.SECONDS);
    }

    private void togglePhaseAndContinue(Project project) {
        MyAppSettings settings = MyAppSettings.Companion.getInstance();

        if (currentPhase == Phase.WORK) {
            currentPhase = Phase.BREAK;
            startCycle(project, settings.getState().getBreakTime());
        } else {
            currentPhase = Phase.WORK;
            startCycle(project, settings.getState().getWorkTime());
        }
    }

    private void showNotification(Project project) {
        String title;
        String content;

        if (currentPhase == Phase.WORK) {
            title = "Pauză!";
            content = "Ai lucrat " + MyAppSettings.Companion.getInstance().getState().getWorkTime() + " secunde, ia o pauză!";
        } else {
            title = "Gata pauza de " + MyAppSettings.Companion.getInstance().getState().getBreakTime() + " secunde!";
            content = "Înapoi la muncă, să terminăm ce am început!";
        }

        NotificationGroupManager.getInstance()
                .getNotificationGroup("com.ctrlaltdefeat.notifications")
                .createNotification(title, content, NotificationType.WARNING)
                .notify(project);
    }

    private void restartCycle(Project project) {
        currentPhase = Phase.WORK;
        startCycle(project, MyAppSettings.Companion.getInstance().getState().getWorkTime());
    }
}
