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
        return !localSettingComp.getUserNameText().equals(state.userId) ||
                localSettingComp.getIdeaUserStatus() != state.ideaStatus;
    }

    override fun apply() {
        val state: MyAppSettings.State =
            Objects.requireNonNull(MyAppSettings.getInstance().getState())
        state.userId = localSettingComp.getUserNameText();
        state.ideaStatus = localSettingComp.getIdeaUserStatus();
    }

    override fun getDisplayName(): String {
        return "Tmp Display Name"
    }
}