package com.ctrlaltdelete.mood

import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import java.awt.Dimension
import java.awt.Rectangle
import java.awt.geom.Line2D
import javax.swing.JPanel
import java.awt.*;
import javax.swing.JLabel

class MoodGraph : ToolWindowFactory, DumbAware {
    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        var lastFiveDays : List<Pair<Int, Int>>

        //get saved data
        var myData : MutableList<Int> = mutableListOf(5104, 5122, 5143, 5161, 5185)

        //complete data
        class dataExtended(day : Int = 0, hour : Int = 0, value : Int = 0){
            val day : Int = day
            val hour : Int = hour
            val value : Int = value
        }
        var myDataMap : MutableMap<Int, Int> = mutableMapOf<Int, Int>()
        for(i in myData){
            myDataMap.put((i/10)%100, i % 10)
        }

        //set up panel and propper dimensions
        val myPlot = object : JPanel() {
            override fun paintComponent(g: Graphics?) {
                super.paintComponent(g)

                if (g is Graphics2D) {
                    // Set up the graphics (color, stroke)
                    g.color = Color.BLUE
                    g.stroke = BasicStroke(2f)

                    // Set up the plot dimensions
                    val panelWidth = 72
                    val panelHeight = 60
                    val dataSize = myData.size

                    // Draw the plot (connecting lines)
                    var x0 : Int = 0
                    var y0 : Int = 100
                    for (i in 1..12) {
                        if(myDataMap.containsKey(i)){
                            var x1 : Int = i * 6
                            var y1 : Int? = 100 - (myDataMap.get(i)?.times(20)!!)
                            g.draw(Line2D.Float(x0.toFloat(), y0.toFloat(), x1.toFloat(), y1!!.toFloat()))
                            x0 = x1
                            y0 = y1
                        }
                    }
                    var x1 : Int = 72
                    var y1 : Int? = 60
                    g.draw(Line2D.Float(x0.toFloat(), y0.toFloat(), x1.toFloat(), y1!!.toFloat()))
                }
            }
        }

        myPlot.size = Dimension(72, 100)
        toolWindow.component.add(myPlot)

        toolWindow.component.add(JPanel().add(JLabel("$myDataMap")))
    }
}