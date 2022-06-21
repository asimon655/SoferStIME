package com.asimon.soferstime

class TextComposer(private val IME: SoferStIME) {

    fun addKeyLongPress(keyCode: Int) {
        KeyEventRepeat.reset()
        val character = KeyMap.getNumber(keyCode)
        sendCharacter(character)
    }

    fun addKeyNum(keyCode: Int) {
        KeyEventRepeat.reset()
        val character = KeyMap.getNumber(keyCode)
        sendCharacter(character)
    }

    fun nextWord() {
        if (!KeyboardSettings.t9) return
    }

    fun addKey(keyCode: Int) {
        KeyEventRepeat.update(keyCode)
        if (KeyEventRepeat.repeatCount > 0) {
            IME.currentInputConnection.deleteSurroundingText(1, 0)
        }
        val character = KeyMap.getCharacter(keyCode, KeyEventRepeat.repeatCount)
        sendCharacter(character)
    }

    fun delete() {
        KeyEventRepeat.reset()
        IME.currentInputConnection.deleteSurroundingText(1, 0)
    }

    private fun sendCharacter(character: Char?) {
        if (character == null) return
        val composedText = Character.toString(character)
        IME.currentInputConnection.commitText(composedText, 1)
    }

    /**
     * mechanism for controlling the tap count
     */
    internal object KeyEventRepeat {
        private var lastKeyEventTime: Long = -1
        private var lastKeyEventCode = -1
        var repeatCount = 0

        fun update(keyCode: Int) {
            val currentTimeMillis = System.currentTimeMillis()
            if (lastKeyEventCode == keyCode && currentTimeMillis - lastKeyEventTime < 500) {
                ++repeatCount
            } else {
                reset()
            }
            lastKeyEventTime = currentTimeMillis
            lastKeyEventCode = keyCode
        }

        fun reset() {
            repeatCount = 0
        }
    }
}