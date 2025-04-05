package com.ctraltdefeat.settings

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage

@State(
    name = "com.ctraltdefeat.settings.MyAppSettings",
    storages = [Storage("MyAppSettings.xml")]
)
class MyAppSettings : PersistentStateComponent<MyAppSettings.State> {

    class State{
        var workTime : Int = 55;
        var breakTime : Int = 5;
        var gitRepo : String = ""
        var gitBranch : String = ""
        var teamID : String = ""
    }

    private var myState: State = State()

    // Companion object to get instance of the service
    companion object {
        // JvmStatic annotation ensures the companion object method is static
        //@JvmStatic  // This makes getInstance a static method in Java
        fun getInstance(): MyAppSettings {
            return ApplicationManager.getApplication().getService(MyAppSettings::class.java)
        }
    }

    override fun getState(): State {
        return myState
    }

    override fun loadState(state: State) {
        myState = state
    }

    // Listening logic

    private val listeners = mutableListOf<() -> Unit>()

    fun addChangeListener(listener: () -> Unit) {
        listeners.add(listener)
    }

    fun notifyNotifications() {
        listeners.forEach { it.invoke() }
    }
}
