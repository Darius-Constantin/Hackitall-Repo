package com.ctrlaltdefeat.annotations.actions;

import com.AskQuestionDialog;
import com.intellij.notification.NotificationGroupManager;
import com.intellij.notification.NotificationType;
import com.intellij.openapi.actionSystem.ActionUpdateThread;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.ui.Messages;
import org.jetbrains.annotations.NotNull;
import com.ctrlaltdefeat.utils.GitUtils;
import com.ctrlaltdefeat.utils.safeStorageUtil;

import java.io.File;
import java.util.Objects;

public class QuestionOnAnnotationAction extends AnnotationAction {
    private String buildIssueBody(String selectedCode, String question) {
        return "### Code Snippet\n```\n" + selectedCode + "\n```\n\n### Question\n" + question + "\n";
    }

    @Override
    public @NotNull ActionUpdateThread getActionUpdateThread() {
        return ActionUpdateThread.BGT;
    }

    @Override
    public void update(@NotNull AnActionEvent e) {
        String selectedText = Objects
                .requireNonNull(FileEditorManager
                        .getInstance(Objects.requireNonNull(e.getProject())).getSelectedTextEditor())
                .getSelectionModel().getSelectedText();
        if (selectedText == null || selectedText.isEmpty()) {
            e.getPresentation().setEnabled(false);
            return;
        }

        e.getPresentation().setEnabled(true);
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        try {
            Project project = e.getProject();
            Editor editor = e.getData(CommonDataKeys.EDITOR);

            if (safeStorageUtil.get("gitToken").isEmpty()) {
                NotificationGroupManager.getInstance()
                        .getNotificationGroup("com.ctrlaltdefeat.gitNotifications")
                        .createNotification("You must first set up a GitHub token inside the "
                                + "plugin's settings", NotificationType.ERROR)
                        .notify(project);
            }

            if (project == null || editor == null) return;

            String selectedText = editor.getSelectionModel().getSelectedText();

            File root = new File(ProjectRootManager.getInstance(Objects.requireNonNull(e.getProject()))
                    .getContentRoots()[0].getPath());
            if (!GitUtils.isBranchUpToDate(root)) {
                Messages.showErrorDialog("Git repository is not up-to-date. "
                        + "Please pull the latest changes.", "Git Out of Sync");
                return;
            }

            var dialogue = new AskQuestionDialog(project);
            if (dialogue.showAndGet()) {
                if (dialogue.getQuestionText().trim().isEmpty()
                    || dialogue.getTitleText().trim().isEmpty()) {
                    return;
                }

                String[] parts = GitUtils.extractOwnerAndRepo(root);
                try {
                    String MY_TOKEN = safeStorageUtil.get("gitToken");
                    GitUtils.createGitIssue(parts[0], parts[1], dialogue.getTitleText(),
                            buildIssueBody(selectedText, dialogue.getQuestionText()), MY_TOKEN,
                            dialogue.getPriorityLevel(), project);
                    notifySuccess(project, "Question " +
                            "posted to GitHub successfully.");
                } catch (Exception ex) {
                    notifyFailure(project, "Failed to post question: " + ex.getMessage());
                }
            }
        } catch(Exception ignored) {
            return;
        }
    }
}
