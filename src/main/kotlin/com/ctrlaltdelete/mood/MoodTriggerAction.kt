package com.ctrlaltdelete.mood

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent

class MoodTriggerAction : AnAction() {

    override fun actionPerformed(e: AnActionEvent) {
        MoodDialog().show()
    }
}