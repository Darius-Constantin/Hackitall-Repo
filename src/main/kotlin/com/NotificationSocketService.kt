package com

import com.ctraltdefeat.settings.MyAppSettings
import com.google.gson.JsonObject
import com.intellij.notification.NotificationGroupManager
import com.intellij.notification.NotificationType
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.Service
import com.intellij.openapi.project.Project
import io.socket.client.IO
import io.socket.client.Socket
import org.json.JSONObject

@Service(Service.Level.PROJECT)
class NotificationSocketService(private val project: Project) {

    fun emitNotification(urgency: Int) {
        val issue = JsonObject()
        issue.addProperty("teamID", MyAppSettings.getInstance().state.teamID.toInt())
        issue.addProperty("urgency", urgency)
        socket!!.emit("broadcast", issue)
    }

    private var socket: Socket? = null

    init {
        connect()
    }

    private fun connect() {
        if (socket != null && socket!!.connected()) return

        socket = IO.socket("http://"
                + MyAppSettings.getInstance().state.serverIP
                + ":"
                + MyAppSettings.getInstance().state.serverPort)

        socket!!.on(Socket.EVENT_CONNECT) {
            socket!!.emit("register", MyAppSettings.getInstance()
                .state.teamID)
        }

        socket!!.on("notification") { args ->
            val data = args[0] as JSONObject
            val teamID = data.getInt("teamID")
            if (teamID == MyAppSettings.getInstance().state.teamID.toInt()) {
                return@on;
            }
            val urgency = data.getInt("urgency")
            when(urgency) {
                1 -> NotificationGroupManager.getInstance()
                    .getNotificationGroup("com.ctrlaltdefeat.gitNotifications")
                    .createNotification("A member of your team has asked a question!",
                        NotificationType.INFORMATION).notify(project);
                2 -> NotificationGroupManager.getInstance()
                    .getNotificationGroup("com.ctrlaltdefeat.gitNotifications")
                    .createNotification("A member of your team has asked a question!",
                        NotificationType.WARNING).notify(project);
                3 -> NotificationGroupManager.getInstance()
                    .getNotificationGroup("com.ctrlaltdefeat.gitNotifications")
                    .createNotification("A member of your team has asked a question!",
                        NotificationType.ERROR).notify(project);
            }
        }

        socket!!.on(Socket.EVENT_CONNECT_ERROR) {
            ApplicationManager.getApplication().invokeLater {
                NotificationGroupManager.getInstance()
                    .getNotificationGroup("com.ctrlaltdefeat.gitNotifications")
                    .createNotification("Connection error from notification server.",
                        NotificationType.ERROR).notify(project)
            }
            socket!!.close()
        }

        socket!!.on(Socket.EVENT_DISCONNECT) {
            ApplicationManager.getApplication().invokeLater {
                NotificationGroupManager.getInstance()
                    .getNotificationGroup("com.ctrlaltdefeat.gitNotifications")
                    .createNotification("Disconnected from notification server.",
                        NotificationType.WARNING).notify(project)
            }
            socket!!.close()
        }

        socket!!.connect()
    }

    fun disconnect() {
        socket?.disconnect()
    }

    companion object {
        fun getInstance(project: Project): NotificationSocketService =
            project.getService(NotificationSocketService::class.java)
    }
}