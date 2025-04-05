package com.ctraltdefeat.settings

import com.intellij.openapi.options.Configurable
import java.util.*
import javax.swing.JComponent

class MyAppSettingsConfigurable : Configurable {
    lateinit var localSettingComp : MyAppSettingsComponent

    override fun createComponent(): JComponent? {
        localSettingComp = MyAppSettingsComponent()
        return localSettingComp.getPanel()
    }

    override fun isModified(): Boolean {
        val state: MyAppSettings.State =
            Objects.requireNonNull(MyAppSettings.getInstance().getState())
        return !localSettingComp.getWorkTime().equals(state.workTime) ||
                localSettingComp.getBreakTime() != state.breakTime;
    }

    override fun apply() {
        val state: MyAppSettings.State =
            Objects.requireNonNull(MyAppSettings.getInstance().getState())
        if(state.workTime != 0 && localSettingComp.getWorkTime() != 0) state.workTime = localSettingComp.getWorkTime();
        if(state.breakTime != 0 && localSettingComp.getBreakTime() != 0) state.breakTime = localSettingComp.getBreakTime();
    }

    override fun getDisplayName(): String {
        return "Tmp Display Name"
    }
}