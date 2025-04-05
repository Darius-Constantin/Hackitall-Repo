package com.ctrlaltdefeat.annotations;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiDocumentManager;

import java.util.ArrayList;

abstract class AnnotationAction extends AnAction {
    protected AnnotationData annotation;

    protected static AnnotationData isInsideAnnotation(Project project, Editor editor) {
        int offset = editor.getCaretModel().getOffset();
        ArrayList<AnnotationData> annotationData = AnnotationStorage
                .getAnnotations(PsiDocumentManager
                        .getInstance(project)
                        .getPsiFile(editor.getDocument())
                        .getVirtualFile().getPath());
        for (var data : annotationData) {
            if (offset <= data.getHighlighter().getEndOffset() && offset >= data.getHighlighter().getStartOffset()) {
                return data;
            }
        }
        return null;
    }
}
