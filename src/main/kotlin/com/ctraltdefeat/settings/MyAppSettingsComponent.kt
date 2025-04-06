package com.ctraltdefeat.settings

import com.ctrlaltdefeat.utils.safeStorageUtil
import com.intellij.openapi.editor.event.DocumentListener
import com.intellij.ui.TitledSeparator
import com.intellij.ui.components.JBLabel
import com.intellij.ui.components.JBTextField
import com.intellij.util.ui.FormBuilder
import javax.swing.*
import javax.swing.event.DocumentEvent
import javax.swing.text.PlainDocument

class MyAppSettingsComponent() {
    private var myMainPanel: JPanel?

    private var myWorkTime : JBTextField
    private var myWorkTimeLabel : JBLabel
    private var myBreakTime : JBTextField
    private var myBreakTimeLabel : JBLabel

    private var myTeamID : JBTextField
    private var myTeamIDLabel : JBLabel
    private var myServerIP : JBTextField
    private var myServerIPLabel : JBLabel
    private var myServerPort : JBTextField
    private var myServerPortLabel : JLabel
    private var myGitTokenButton : JButton

    private fun createDialogWithTextField() {
        val textField = JTextField(30) // 20 columns wide for better visibility

        // Create an array for the possible options (OK and Cancel)
        val options = arrayOf("Save", "Cancel")

        // Display the dialog with the text field as the input
        val result = JOptionPane.showOptionDialog(
            null,               // Parent component (null means it will be centered)
            textField,          // The component to display (JTextField)
            "Enter Git Token",       // Title of the dialog
            JOptionPane.DEFAULT_OPTION,  // No custom icons
            JOptionPane.PLAIN_MESSAGE,    // No specific message type
            null,               // No icon
            options,            // Options array (OK and Cancel)
            options[0]          // Default selection (OK is selected by default)
        )

        if (result == JOptionPane.OK_OPTION) {
            safeStorageUtil.store("gitToken", textField.text)
        }
    }

    init {
        myWorkTime = JBTextField()
        myBreakTime = JBTextField()
        (myWorkTime.document as? PlainDocument)?.documentFilter = NumericDocumentFilter()
        (myBreakTime.document as? PlainDocument)?.documentFilter = NumericDocumentFilter()

        myTeamID = JBTextField()
        (myTeamID.document as? PlainDocument)?.documentFilter = NumericDocumentFilter()
        myGitTokenButton = JButton("Set Git token")
        myGitTokenButton.addActionListener {
            createDialogWithTextField()
        }
        myServerIP = JBTextField()
        myServerPort = JBTextField()
        (myServerPort.document as? PlainDocument)?.documentFilter = NumericDocumentFilter()

        val formPanel = FormBuilder.createFormBuilder().apply {
            val appSettings = MyAppSettings.getInstance()
            val currentState = appSettings.state

            myWorkTime.text = "${currentState.workTime}"
            myBreakTime.text = "${currentState.breakTime}"

            addComponent(TitledSeparator("Wellness Settings"))
            myWorkTimeLabel = JBLabel("Work time");
            addLabeledComponent(myWorkTimeLabel, myWorkTime, 1, false)
            myBreakTimeLabel = JBLabel("Break time")
            addLabeledComponent(myBreakTimeLabel, myBreakTime, 1, false)

            addComponent(TitledSeparator("Collaboration Settings"))
            myTeamIDLabel = JBLabel("Team ID")
            addLabeledComponent(myTeamIDLabel, myTeamID, 1, false)
            myServerIPLabel = JBLabel("Server IP")
            addLabeledComponent(myServerIPLabel, myServerIP, 1, false)
            myServerPortLabel = JLabel("Server port")
            addLabeledComponent(myServerPortLabel, myServerPort, 1, false)
            addComponent(myGitTokenButton)
            addComponentFillVertically(JPanel(), 0)
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

    fun getTeamID() : String {
        return myTeamID.text
    }

    fun getServerIP() : String {
        return myServerIP.text
    }

    fun getServerPort() : Int {
        return myServerPort.text.toInt()
    }

    fun setServerIp(newServerIP: String) {
        myServerIP.text = newServerIP
    }

    fun setServerPort(newServerPort: Int) {
        myServerPort.text = newServerPort.toString()
    }

    fun updateDebug() {
        val appSettings = MyAppSettings.getInstance()
        val currentState = appSettings.state
    }
}