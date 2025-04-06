package com.ctrlaltdefeat.annotations.actions;

import com.ctrlaltdefeat.utils.GitUtils;
import com.ctrlaltdefeat.utils.safeStorageUtil;
import com.intellij.notification.NotificationGroupManager;
import com.intellij.notification.NotificationType;
import com.intellij.openapi.actionSystem.ActionUpdateThread;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ProjectRootManager;
import groovy.lang.Tuple2;
import openAI.MyOpenAIClient;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Objects;

public class SearchPastQuestionAction extends AnnotationAction {
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
            if (safeStorageUtil.get("gitToken").isEmpty()) {
                NotificationGroupManager.getInstance()
                        .getNotificationGroup("com.ctrlaltdefeat.gitNotifications")
                        .createNotification("You must first set up a GitHub token inside the "
                                        + "plugin's settings", NotificationType.ERROR)
                        .notify(e.getProject());
            }

            Project project = e.getProject();
            Editor editor = e.getData(CommonDataKeys.EDITOR);
            if (project == null || editor == null) return;

            String selectedText = editor.getSelectionModel().getSelectedText();

            File root = new File(ProjectRootManager.getInstance(Objects.requireNonNull(e.getProject()))
                    .getContentRoots()[0].getPath());

            String[] parts = GitUtils.extractOwnerAndRepo(root);
            ArrayList<String> links = new ArrayList<>();
            try {
                String MY_TOKEN = safeStorageUtil.get("gitToken");
                ArrayList<Tuple2<String, String>> list = GitUtils.listGitIssues(parts[0], parts[1],
                        MY_TOKEN);
                MyOpenAIClient client = new MyOpenAIClient();
                for (Tuple2<String, String> pair : list) {
                    if (client.isCodeRelated(selectedText, pair.getV2())) {
                        links.add(pair.getV1());
                    }
                }
                if (!links.isEmpty()) {
                    StringBuilder htmlMessage = new StringBuilder("<html><body>");
                    htmlMessage.append("<h3>Found Code-Related Issues:</h3><ul>");

                    // Create an HTML list of links
                    for (String link : links) {
                        htmlMessage.append("<li><a href=\"").append(link)
                                .append("\">").append(link).append("</a></li>");
                    }

                    htmlMessage.append("</ul></body></html>");

                    // Create a custom dialog with the HTML message using JEditorPane
                    JTextArea editorPane = new JTextArea(htmlMessage.toString());
                    editorPane.setEditable(false); // Make it non-editable

                    // Show the custom dialog with the HTML content
                    JOptionPane.showMessageDialog(null, editorPane,
                            "Code-Related Issues", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    NotificationGroupManager.getInstance()
                            .getNotificationGroup("com.ctrlaltdefeat.gitNotifications")
                            .createNotification("No issues found.",
                                    NotificationType.INFORMATION).notify(project);
                }

            } catch (Exception ex) {
                notifyFailure(project, "Failed to fetch issues: " + ex.getMessage());
            }
        } catch(Exception ignored) {
            return;
        }
    }
}
