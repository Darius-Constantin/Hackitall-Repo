package com.ctrlaltdefeat.annotations.actions;

import com.ctrlaltdefeat.annotations.dataContainers.AnnotationData;
import com.ctrlaltdefeat.annotations.AnnotationStorage;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiDocumentManager;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

abstract class AnnotationAction extends AnAction {
    protected AnnotationData annotation;

    @Override
    public void update(@NotNull AnActionEvent event) {
        Project project = event.getProject();
        if (project == null) {
            event.getPresentation().setEnabled(false);
            return;
        }

        Editor editor = FileEditorManager.getInstance(project).getSelectedTextEditor();
        if (editor == null) {
            event.getPresentation().setEnabled(false);
            return;
        }

        annotation = isInsideAnnotation(project, editor);
        event.getPresentation().setEnabledAndVisible(annotation != null);
    }

    protected static AnnotationData isInsideAnnotation(Project project, Editor editor) {
        int offset = editor.getCaretModel().getOffset();
        ArrayList<AnnotationData> annotationData = AnnotationStorage
                .getAnnotations(AnnotationStorage.getRelativePath(PsiDocumentManager
                        .getInstance(project)
                        .getPsiFile(editor.getDocument())
                        .getVirtualFile(), project));
        for (var data : annotationData) {
            if (offset <= data.getHighlighter().getEndOffset()
                    && offset >= data.getHighlighter().getStartOffset()) {
                return data;
            }
        }
        return null;
    }
}
