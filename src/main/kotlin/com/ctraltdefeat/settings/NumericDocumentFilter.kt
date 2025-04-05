package com.ctraltdefeat.settings

import javax.swing.text.AttributeSet
import javax.swing.text.DocumentFilter

class NumericDocumentFilter : DocumentFilter() {
    override fun insertString(fb: FilterBypass, offset: Int, string: String?, attr: AttributeSet?) {
        if (string != null && string.matches(Regex("\\d*"))) {
            super.insertString(fb, offset, string, attr)
        }
    }

    override fun replace(fb: FilterBypass, offset: Int, length: Int, string: String?, attrs: AttributeSet?) {
        if (string != null && string.matches(Regex("\\d*"))) {
            super.replace(fb, offset, length, string, attrs)
        }
    }
}