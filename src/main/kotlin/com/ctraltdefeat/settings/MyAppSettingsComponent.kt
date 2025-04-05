package com.ctraltdefeat.settings

import com.intellij.ui.IdeBorderFactory
import com.intellij.ui.SideBorder
import com.intellij.ui.TitledSeparator
import com.intellij.ui.components.JBCheckBox
import com.intellij.ui.components.JBLabel
import com.intellij.ui.components.JBTextField
import com.intellij.util.ui.FormBuilder
import com.intellij.util.ui.JBUI
import javax.swing.BorderFactory
import javax.swing.JComponent
import javax.swing.JPanel
import javax.swing.text.DocumentFilter
import javax.swing.text.PlainDocument


class MyAppSettingsComponent() {
    private var myMainPanel: JPanel?
    private var myWorkTime : JBTextField
    private var myWorkTimeLabel : JBLabel
    private var myBreakTime : JBTextField
    private var myBreakTimeLabel : JBLabel

    init {
        myWorkTime = JBTextField()
        (myWorkTime.document as? PlainDocument)?.documentFilter = NumericDocumentFilter()
        myBreakTime = JBTextField()
        (myBreakTime.document as? PlainDocument)?.documentFilter = NumericDocumentFilter()

        val formPanel = FormBuilder.createFormBuilder().apply {
            val appSettings = MyAppSettings.getInstance()
            val currentState = appSettings.state

            myWorkTime.text = "${currentState.workTime}"
            myBreakTime.text = "${currentState.breakTime}"

            addComponent(TitledSeparator("Wellness Settings"))
            myWorkTimeLabel = JBLabel("Work time");
            addLabeledComponent(myWorkTimeLabel, myWorkTime, 1, false)
            myBreakTimeLabel = JBLabel("Break time")
            addLabeledComponent(myBreakTimeLabel, myBreakTime, 2, false)

            addComponent(TitledSeparator("Collaboration Settings"))
            myWorkTimeLabel = JBLabel("Git repository link");
            addLabeledComponent(myWorkTimeLabel, myWorkTime, 1, false)
            myBreakTimeLabel = JBLabel("Team ID")
            addLabeledComponent(myBreakTimeLabel, myBreakTime, 2, false)
            myBreakTimeLabel = JBLabel("Server IP")
            addLabeledComponent(myBreakTimeLabel, myBreakTime, 3, false)
            myBreakTimeLabel = JBLabel("Server port")
            addLabeledComponent(myBreakTimeLabel, myBreakTime, 4, false)
        }.panel

        myMainPanel = formPanel
    }

    fun getPanel(): JPanel? {
        return myMainPanel
    }

    fun getPreferredFocusedComponent(): JComponent {
        return myWorkTime
    }

    fun getWorkTime(): Int {
        return if(myWorkTime.text.isNotEmpty() && myWorkTime.text.isNotBlank()){
            myWorkTime.text.toInt()
        } else
            MyAppSettings.getInstance().state.workTime
    }

    fun setBreakTime(newBreakTime: Int) {
        myBreakTime.text = newBreakTime.toString()
    }

    fun getBreakTime() : Int {
        return if(myBreakTime.text.isNotEmpty() && myBreakTime.text.isNotBlank()){
            myBreakTime.text.toInt()
        } else
            MyAppSettings.getInstance().state.breakTime
    }

    fun setWorkTime(newWorkTime: Int) {
        myWorkTime.text = newWorkTime.toString()
    }

    fun updateDebug() {
        val appSettings = MyAppSettings.getInstance()
        val currentState = appSettings.state
        myWorkTime.text = "${currentState.workTime}"
        myBreakTime.text = "${currentState.breakTime}"
    }
}