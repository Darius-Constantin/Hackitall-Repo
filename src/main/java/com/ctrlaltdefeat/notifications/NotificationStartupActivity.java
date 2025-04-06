package com.ctrlaltdefeat.notifications;

import com.ctraltdefeat.settings.MyAppSettings;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.startup.StartupActivity;
import com.intellij.openapi.ui.Messages;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.ScheduledFuture;

public class NotificationStartupActivity implements StartupActivity {

    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private ScheduledFuture<?> scheduledTask;
    private Phase currentPhase = Phase.WORK;
    private Boolean snoozed = false;

    private final CyclePhase phaseService = ApplicationManager.getApplication().getService(CyclePhase.class);

    private enum Phase {
        WORK,
        BREAK
    }

    @Override
    public void runActivity(@NotNull Project project) {
        MyAppSettings settings = MyAppSettings.Companion.getInstance();

        startCycle(project, settings.getState().getWorkTime());

        settings.addChangeListener(() -> {
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
                }), delayInSeconds, TimeUnit.MINUTES);

    }

    private void togglePhaseAndContinue(Project project) {
        MyAppSettings settings = MyAppSettings.Companion.getInstance();

        if (currentPhase == Phase.WORK) {
            currentPhase = Phase.BREAK;
            phaseService.setCurrentPhase(CyclePhase.Phase.BREAK);
            startCycle(project, settings.getState().getBreakTime());
        } else {
            currentPhase = Phase.WORK;
            phaseService.setCurrentPhase(CyclePhase.Phase.WORK);
            startCycle(project, settings.getState().getWorkTime());
        }
    }

    private void showNotification(Project project) {
        String title;
        String content;

        if(!snoozed) {
            if (currentPhase == Phase.WORK) {
                title = "Break Time!";
                content = "You worked" + MyAppSettings.Companion.getInstance().getState().getWorkTime() + " minutes, take a break!";
            } else {
                title = "Your " + MyAppSettings.Companion.getInstance().getState().getBreakTime() + " minute break is over!";
                content = "Back to work! Let's finish what we started!";
            }
        }else{
            snoozed = false;
            if (currentPhase == Phase.WORK) {
                title = "STOP!!!";
                content = "You worked to much! Take a break!";
            } else {
                title = "Too Much Break Time!!!";
                content = "Too much break time gets you out of your rithm! Lets work!!";
            }
        }

//        Messages.showMessageDialog(
//                project,
//                content,
//                title,
//                Messages.getInformationIcon()
//        );

        String[] options = {"OK", "Snooze"};
        int choice = Messages.showDialog(
                project,
                content,
                title,
                options,
                0, // default to "OK"
                Messages.getInformationIcon()
        );

        if (choice == 1) { // Snooze clicked
            snoozeCurrentPhase(project);
        } else {
            togglePhaseAndContinue(project);
        }
    }

    private void snoozeCurrentPhase(Project project) {
        snoozed = true;
        int snoozeSeconds = 5;

        if (scheduledTask != null && !scheduledTask.isCancelled()) {
            scheduledTask.cancel(false);
        }

        scheduledTask = scheduler.schedule(() ->
                ApplicationManager.getApplication().invokeLater(() -> {
                    showNotification(project);
                }), snoozeSeconds, TimeUnit.MINUTES);
    }

    private void restartCycle(Project project) {
        currentPhase = Phase.WORK;
        phaseService.setCurrentPhase(CyclePhase.Phase.WORK);
        startCycle(project, MyAppSettings.Companion.getInstance().getState().getWorkTime());
    }
}
