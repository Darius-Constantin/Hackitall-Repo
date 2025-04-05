package com.ctrlaltdefeat.annotations;

import com.intellij.codeInsight.daemon.DaemonCodeAnalyzer;
import com.intellij.openapi.actionSystem.ActionUpdateThread;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.SelectionModel;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.ui.Messages;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;

public class AddAnnotationAction extends AnnotationAction {
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
        };

        Editor editor = FileEditorManager.getInstance(project).getSelectedTextEditor();
        if (editor == null) {
            event.getPresentation().setEnabled(false);
            return;
        };

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

        String note = Messages.showInputDialog(project, "Enter annotation:",
                "New Annotation",
                Messages.getQuestionIcon());
        if (note == null || note.trim().isEmpty()) return;

        String filePath = PsiDocumentManager.getInstance(project).getPsiFile(editor.getDocument()).getVirtualFile().getPath();
        AnnotationData.AnnotationDataBuilder annotationBuilder = new AnnotationData.AnnotationDataBuilder(startOffset,
                endOffset, note, editor);
        AnnotationStorage.buildAndAddAnnotation(filePath, annotationBuilder);

        Messages.showMessageDialog(project, "Annotation added!", "Success", Messages.getInformationIcon());

        PsiFile file = PsiDocumentManager.getInstance(project).getPsiFile(editor.getDocument());
        if (file != null) {
            DaemonCodeAnalyzer.getInstance(project).restart();
        }
    }
}