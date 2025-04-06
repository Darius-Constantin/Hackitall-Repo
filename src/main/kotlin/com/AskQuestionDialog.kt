package com

import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.ComboBox
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.ui.components.JBTextArea
import com.intellij.ui.components.JBTextField
import com.intellij.util.ui.UIUtil
import java.awt.Color
import javax.swing.*
import javax.swing.border.CompoundBorder

class AskQuestionDialog(project: Project?) : DialogWrapper(project) {
    private val titleField = JBTextField("Question title")
    private val questionField = JBTextArea("Question body")
    private val priorityCombo = ComboBox(arrayOf("Whenever", "As soon as possible", "Urgent"))

    init {
        init()
        title = "Ask a Question"
        setSize(450, preferredSize.height)
    }


    override fun createCenterPanel(): JPanel {
        // Create the main panel with GridBagLayout
        val panel = JPanel()
        panel.layout = BoxLayout(panel, BoxLayout.Y_AXIS)
        panel.add(titleField)
        titleField.setFont(UIUtil.getLabelFont())
        titleField.setSize(titleField.getPreferredSize().width, 50);
        titleField.border = CompoundBorder(CompoundBorder(BorderFactory.createEmptyBorder(5,
            5, 5, 5), BorderFactory.createLineBorder(Color.GRAY, 1)),
            BorderFactory.createEmptyBorder(5, 5, 5, 5))
        panel.add(Box.createVerticalStrut(10))
        panel.add(questionField)
        questionField.rows = 3
        questionField.setFont(UIUtil.getLabelFont())
        questionField.border = CompoundBorder(CompoundBorder(BorderFactory.createEmptyBorder(5,
            5, 5, 5), BorderFactory.createLineBorder(Color.GRAY, 1)),
            BorderFactory.createEmptyBorder(5, 5, 5, 5))
        panel.add(Box.createVerticalStrut(10))
        panel.add(priorityCombo)

        return panel
    }


    fun getTitleText(): String = titleField.text.trim()
    fun getQuestionText(): String = questionField.text.trim()
    fun getPriorityLevel(): Int = when (priorityCombo.selectedItem as String) {
        "Urgent" -> 3
        "As soon as possible" -> 2
        else -> 1
    }
}