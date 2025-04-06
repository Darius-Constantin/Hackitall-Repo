package com.ctrlaltdelete.mood

import java.awt.BorderLayout
import java.awt.GridLayout
import javax.swing.JComponent
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.JSlider
import javax.swing.event.ChangeEvent

class MoodDialogComponent() : JComponent() {
    private var myPanel : JPanel = JPanel()
    private var mySlider : JSlider = JSlider(JSlider.HORIZONTAL, 1, 5, 3)
    private var myLabel : JLabel = JLabel("How is your mood today? Currently : ${mySlider.value}")

    init{
        mySlider.setMajorTickSpacing(1)

        mySlider.addChangeListener({ e : ChangeEvent? ->
            myLabel.text = "How is your mood today? Currently : ${mySlider.value}"
        })

        myPanel.apply {
            setLayout(GridLayout(2, 2))
            //add(JLabel("/*${Mood.getInstance().state.moodList}*/ mood list"))
            add(myLabel)
            add(mySlider)
        }
    }

    fun getPanel() : JPanel {
        return myPanel
    }

    fun getSliderValue() : Int {
        return mySlider.value
    }

}