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
    private lateinit var myWorkTimeLabel : JBLabel
    private var myBreakTime : JBTextField
    private lateinit var myBreakTimeLabel : JBLabel

    init {
        // Initialize components
        myWorkTime = JBTextField()
        myBreakTime = JBTextField()

        // Build form and add components
        myMainPanel = FormBuilder.createFormBuilder().apply {
            val appSettings = MyAppSettings.getInstance()
            val currentState = appSettings.getState()

            myWorkTimeLabel = JBLabel("Work Time ${currentState.workTime} :");
            addLabeledComponent(myWorkTimeLabel, myWorkTime, 1, false)
            myBreakTimeLabel = JBLabel("Break Time ${currentState.breakTime} :")
            addLabeledComponent(myBreakTimeLabel, myBreakTime, 2, false)
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
            return MyAppSettings.getInstance().getState().workTime
    }

    fun setBreakTime(newBreakTime: Int) {
        myBreakTime.text = newBreakTime.toString()
    }

    fun getBreakTime() : Int {
        if(myBreakTime.text.isNotEmpty() && myBreakTime.text.isNotBlank()){
            return myBreakTime.text.toInt()
        }
        else
            return MyAppSettings.getInstance().getState().breakTime
    }

    fun setWorkTime(newWorkTime: Int) {
        myWorkTime.text = newWorkTime.toString()
    }

    fun updateDebug() {
        val appSettings = MyAppSettings.getInstance()
        val currentState = appSettings.getState()
        myWorkTimeLabel.text = "Work Time ${currentState.workTime} :"
        myWorkTime.text = ""
        myBreakTimeLabel.text = "Break Time ${currentState.breakTime} :"
        myBreakTime.text = ""
    }
}