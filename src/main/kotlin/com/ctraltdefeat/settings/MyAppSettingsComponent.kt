package com.ctraltdefeat.settings

import com.intellij.ui.components.JBCheckBox
import com.intellij.ui.components.JBLabel
import com.intellij.ui.components.JBTextField
import com.intellij.util.ui.FormBuilder
import javax.swing.JComponent
import javax.swing.JPanel


class MyAppSettingsComponent() {
    private var myMainPanel: JPanel?
    private var myWorkTime : JBTextField
    private var myBreakTime : JBTextField

    init {
        // Initialize components
        myWorkTime = JBTextField()
        myBreakTime = JBTextField()

        // Build form and add components
        myMainPanel = FormBuilder.createFormBuilder().apply {
            val appSettings = MyAppSettings.getInstance()
            val currentState = appSettings.getState()

            addLabeledComponent(JBLabel("Work Time ${currentState.workTime} :"), myWorkTime, 1, false)
            addLabeledComponent(JBLabel("Break Time ${currentState.breakTime} :"), myBreakTime, 2, false)
            addComponentFillVertically(JPanel(), 0)
        }.panel
    }

    fun getPanel(): JPanel? {
        return myMainPanel
    }

    fun getPreferredFocusedComponent(): JComponent {
        return myWorkTime
    }

    fun getWorkTime(): Int {
        if(myWorkTime.text.isNotEmpty() && myWorkTime.text.isNotBlank()){
            return myWorkTime.text.toInt()
        }
        else
            return 0
    }

    fun setBreakTime(newBreakTime: Int) {
        myBreakTime.text = newBreakTime.toString()
    }

    fun getBreakTime() : Int {
        if(myBreakTime.text.isNotEmpty() && myBreakTime.text.isNotBlank()){
            return myBreakTime.text.toInt()
        }
        else
            return 0
    }

    fun setWorkTime(newWorkTime: Int) {
        myWorkTime.text = newWorkTime.toString()
    }

}