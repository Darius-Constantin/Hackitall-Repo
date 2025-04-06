package com.ctrlaltdelete.mood

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import jetbrains.datalore.plot.builder.PlotSvgComponent
import openAI.MyOpenAIClient
import java.awt.Dimension
import java.awt.Rectangle
import java.awt.geom.Line2D
import javax.swing.JPanel
import java.awt.*;
import javax.swing.JLabel
import org.jetbrains.letsPlot.*
import org.jetbrains.letsPlot.awt.plot.PlotSvgExport
import org.jetbrains.letsPlot.awt.plot.component.ApplicationContext
import org.jetbrains.letsPlot.awt.plot.component.DefaultPlotContentPane
import org.jetbrains.letsPlot.awt.plot.component.PlotPanel
import org.jetbrains.letsPlot.batik.plot.component.DefaultPlotPanelBatik
import org.jetbrains.letsPlot.commons.formatting.string.wrap
import org.jetbrains.letsPlot.commons.geometry.DoubleVector
import org.jetbrains.letsPlot.core.util.MonolithicCommon
import org.jetbrains.letsPlot.geom.geomLine
import org.jetbrains.letsPlot.themes.*
import org.jetbrains.letsPlot.export.ggsave
import org.jetbrains.letsPlot.intern.toSpec
import org.jetbrains.letsPlot.jfx.plot.component.DefaultPlotPanelJfx
import org.jetbrains.letsPlot.themes.theme
import javax.swing.UIManager
import java.time.LocalDateTime
import javax.swing.BorderFactory.createEmptyBorder
import javax.swing.BoxLayout
import javax.swing.SwingConstants


class MoodGraph : ToolWindowFactory, DumbAware {
    var panel : JPanel? = null
    var client : MyOpenAIClient = MyOpenAIClient()

    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        val stateList = Mood.getInstance().getState().moodList
        val fileredList = stateList.filter {
            it%1000 == LocalDateTime.now().dayOfMonth
        }
        println(fileredList)
        println(stateList)
        println(stateList.map{
                (it/10)%100
        })
        println(stateList.map{
                it%10
        })

        val data : MutableMap<String, Any> = mutableMapOf(
            "hour" to stateList.map{
                elem:Int -> (elem/10)%100
            },
            "stress" to stateList.map{
                elem:Int -> elem%10 - 3
            }
        )

        var myTheme : theme = theme(
            title = "Mood Graph",
        )

        // Create a Lets-Plot chart
        val plot = letsPlot(data) +
                geomLine {
                    x = "hour"
                    y = "stress"
                }

        var rawSpec = plot.toSpec()
        var processedSpec = MonolithicCommon.processRawSpecs(rawSpec, frontendOnly = false)
        panel = DefaultPlotPanelBatik(
            processedSpec = processedSpec,
            preserveAspectRatio = true,
            preferredSizeFromPlot = false,
            repaintDelay = 100, ){messages->
                for (message in messages) {
                    println(message)
                }
        }

        val mainPanel = JPanel(BorderLayout())
        mainPanel.border = createEmptyBorder(15, 15, 15, 15) // Add margin around everything

// Title
        val titleLabel = JLabel("Stress Graph of Today", SwingConstants.CENTER)
        titleLabel.font = titleLabel.font.deriveFont(16f)
        mainPanel.add(titleLabel, BorderLayout.NORTH)

// Chart panel
        panel?.border = createEmptyBorder(10, 0, 10, 0) // Add spacing above/below chart
        panel?.size?.height = 50
        mainPanel.add(panel, BorderLayout.CENTER) // Chart goes in the CENTER now

// Chat-style response panel
        val chatPanel = JPanel()
        chatPanel.layout = BoxLayout(chatPanel, BoxLayout.Y_AXIS)
        chatPanel.border = createEmptyBorder(10, 10, 10, 10)
        chatPanel.background = Color(50, 50, 50) // Dark background for chat bubble

// Prompt for chat response
        val prompt = """
Hey! I've looked at your self-reported stress levels for today, which range from -2 (very relaxed) to +2 (very stressed).
Based on your data — ${data["stress"]} — here are a few personal observations about how your day went.

Try to be encouraging if the user seemed mostly relaxed, with comments like ‘You did great today!’ or ‘You stayed calm even through ups and downs.’

If the user experienced more stress, be gentle and supportive, and offer helpful, realistic suggestions as a bullet point list. Before each item leave a blank line.

Suggestions should be calm activities appropriate for right now, such as:
- Look out the window
- Take a short nap
- Do a light stretch
- Breathe slowly for one minute

Keep it short and kind — like a check-in from a helpful companion.
""".trimIndent()

        val responseText = "<html><div style='width:200px; color:white;'>${client.generateText(prompt).replace("\n", "<br><br>")}</div></html>"
        val responseLabel = JLabel(responseText)
        responseLabel.alignmentX = Component.LEFT_ALIGNMENT
        chatPanel.add(responseLabel)

        mainPanel.add(chatPanel, BorderLayout.SOUTH) // Chat response goes BELOW the chart

// Add the layout to the ToolWindow
        toolWindow.component.add(mainPanel)

    }
}