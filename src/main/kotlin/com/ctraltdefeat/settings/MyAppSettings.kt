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
        var userId: String = "John Smith"
        var ideaStatus: Boolean = false
    }

    private var myState: State = State()

    companion object{
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
}
