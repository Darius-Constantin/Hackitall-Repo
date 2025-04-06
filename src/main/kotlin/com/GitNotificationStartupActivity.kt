package com//package com.ctraltdefeat.settings

import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.ProjectActivity
import javax.swing.SwingUtilities

class GitNotificationStartupActivity : ProjectActivity {
    override suspend fun execute(project: Project) {
        NotificationSocketService.getInstance(project)
    }
}