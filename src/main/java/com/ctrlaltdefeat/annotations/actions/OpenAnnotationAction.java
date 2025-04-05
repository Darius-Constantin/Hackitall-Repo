package com.ctrlaltdefeat.annotations.actions;

import com.ctrlaltdefeat.annotations.AnnotationStorage;
import com.intellij.openapi.actionSystem.ActionUpdateThread;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowManager;
import org.jetbrains.annotations.NotNull;

public class OpenAnnotationAction extends AnnotationAction {
    @Override
    public @NotNull ActionUpdateThread getActionUpdateThread() {
        return ActionUpdateThread.BGT;
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent event) {
        Project project = event.getProject();
        if (project == null) return;

        ToolWindow annotationToolWindow = ToolWindowManager.getInstance(project).getToolWindow(
                "Annotation Panel");
        if (annotationToolWindow != null) {
            annotationToolWindow.activate(null);
        }
        AnnotationStorage.setCurrentAnnotation(annotation);
    }
}
