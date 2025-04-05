package com.ctrlaltdefeat.annotations;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManagerListener;
import org.jetbrains.annotations.NotNull;

public class AnnotationProjectManagerListener implements ProjectManagerListener {
    @Override
    public void projectClosing(@NotNull Project project) {
        AnnotationStorage.saveNotes(project);
    }
}
