package com.ctrlaltdefeat.annotations.actions;

import com.ctrlaltdefeat.annotations.dataContainers.AnnotationData;
import com.ctrlaltdefeat.annotations.AnnotationStorage;
import com.ctrlaltdefeat.annotations.ui.AnnotationInputDialog;
import com.intellij.openapi.actionSystem.ActionUpdateThread;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.SelectionModel;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class AddAnnotationAction extends AnnotationAction {
    public final static int DEFAULT_OPACITY = 20;
    public final static int DEFAULT_R = 255;
    public final static int DEFAULT_G = 50;
    public final static int DEFAULT_B = 50;

    @Override
    public @NotNull ActionUpdateThread getActionUpdateThread() {
        return ActionUpdateThread.BGT;
    }

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

        SelectionModel selectionModel = editor.getSelectionModel();
        int startOffset = selectionModel.getSelectionStart();
        int endOffset = selectionModel.getSelectionEnd();

        event.getPresentation().setEnabledAndVisible(startOffset != endOffset);
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent event) {
        Project project = event.getProject();
        if (project == null) return;

        Editor editor = FileEditorManager.getInstance(project).getSelectedTextEditor();
        if (editor == null) return;

        SelectionModel selectionModel = editor.getSelectionModel();
        int startOffset = selectionModel.getSelectionStart();
        int endOffset = selectionModel.getSelectionEnd();

        if (startOffset == endOffset) {
            return;
        }

        String note = "";
        Color color = new Color(DEFAULT_R, DEFAULT_G, DEFAULT_B, DEFAULT_OPACITY);
        AnnotationInputDialog input = new AnnotationInputDialog(selectionModel.getSelectedText());
        if (input.showAndGet()) {
            note = input.getNoteText();
            Color colorTmp = input.getColor();
            color = new Color(colorTmp.getRed(), colorTmp.getGreen(), colorTmp.getBlue(),
                    DEFAULT_OPACITY);
        } else {
            return;
        }

        PsiFile psiFile = PsiDocumentManager.getInstance(project).getPsiFile(editor.getDocument());
        if (psiFile == null) return;
        VirtualFile vFile = psiFile.getVirtualFile();

        ToolWindow annotationToolWindow = ToolWindowManager.getInstance(project).getToolWindow(
                "Annotation Panel");
        if (annotationToolWindow != null) {
            annotationToolWindow.activate(null);
        }

        AnnotationData annotation = new AnnotationData(startOffset, endOffset, note, color, editor);
        AnnotationStorage
                .setCurrentAnnotation(AnnotationStorage
                        .createAnnotation(AnnotationStorage.getRelativePath(vFile, project), annotation));
    }
}