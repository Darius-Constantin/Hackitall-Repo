package com.ctrlaltdefeat.notifications;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.startup.StartupActivity;
import org.jetbrains.annotations.NotNull;

public class ActivityTrackerStartup implements StartupActivity.Background {
    @Override
    public void runActivity(@NotNull Project project) {
        // Force the ActivityTracker to initialize
        ActivityTracker activityTracker = ApplicationManager.getApplication().getService(ActivityTracker.class);
        activityTracker.setProject(project);
    }
}
