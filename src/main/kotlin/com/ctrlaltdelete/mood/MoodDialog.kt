package com.ctrlaltdelete.mood

import com.intellij.openapi.ui.DialogWrapper
import com.intellij.util.Time
import groovy.lang.Tuple3
import java.util.*
import javax.swing.JComponent
import groovy.lang.Tuple2
import java.time.LocalDateTime
import java.util.Date
import kotlin.time.Duration.Companion.hours


class MoodDialog : DialogWrapper(true) {
    var localComponent : MoodDialogComponent = MoodDialogComponent()

    init{
        title = "Mood"
        init()
    }

    override fun createCenterPanel(): JComponent? {
        localComponent = MoodDialogComponent()

        return localComponent.getPanel()
    }

    override fun getExitCode(): Int {
        return super.getExitCode()
    }

    override fun doOKAction() {
        var dataCode : Int = 0;
        dataCode += LocalDateTime.now().dayOfMonth *1000
        dataCode += LocalDateTime.now().hour * 10
        dataCode += localComponent.getSliderValue()

        Mood.getInstance().getState().moodList.add(dataCode)

        //Mood.getInstance().getState().moodList= mutableListOf(6101, 6122, 6143, 6151, 6184)

        super.doOKAction()
    }
}