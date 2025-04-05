package com.ctrlaltdelete.mood

import com.ctraltdefeat.settings.MyAppSettings
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import groovy.lang.Tuple
import groovy.lang.Tuple2
import groovy.lang.Tuple3
import java.util.Date

@State(
    name = "com.ctraltdeleat.mood.Mood",
    storages = [Storage("Mood.xml")]
)
class Mood : PersistentStateComponent<Mood.State> {
    class State {
        var moodList : MutableList<Int> = ArrayList()
    }

    private var myState: Mood.State = State()

    companion object{
        fun getInstance(): Mood {
            return ApplicationManager.getApplication().getService(Mood::class.java)
        }
    }

    override fun getState(): State {
        return myState
    }

    override fun loadState(state: State) {
        myState = state
    }
}