package com.asimon.soferstime

import android.view.KeyEvent

class TextComposer(private val IME: SoferStIME) {
    private val t9 = T9Sql(IME)
    private var words: List<String>? = null
    private var wordIndex = 0
    private var code = ""

    fun addKeyLongPress(keyCode: Int) {
        selectWord()
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
        wordIndex++
        setWord(words?.get(wordIndex % (words?.size ?: 1)))
        IME.nextWord()
    }

    fun previousWord() {
        if (!KeyboardSettings.t9) return
        wordIndex--
        setWord(words?.get(wordIndex % (words?.size ?: 1)))
        IME.previousWord()
    }

    fun addKey(keyCode: Int) {
        if (KeyboardSettings.t9) {
            if (keyCode == KeyEvent.KEYCODE_0) { // space
                selectWord()
                sendCharacter(' ')
                return
            }
            code += KeyMap.getNumber(keyCode)
            words = t9.getWordsByCode(code)
            IME.updateViewWordList(words)
            wordIndex = 0
            setWord(words?.get(0))
        } else {
            KeyEventRepeat.update(keyCode)
            IME.currentInputConnection.beginBatchEdit()
            if (KeyEventRepeat.repeatCount > 0) {
                IME.currentInputConnection.deleteSurroundingText(1, 0)
            }
            val character = KeyMap.getCharacter(keyCode, KeyEventRepeat.repeatCount)
            sendCharacter(character)
            IME.currentInputConnection.endBatchEdit()
        }
    }

    fun delete() {
        if (KeyboardSettings.t9) {
            if (code.isEmpty()) {
                IME.currentInputConnection.deleteSurroundingText(1, 0)
            } else {
                code = code.dropLast(1)
                words = t9.getWordsByCode(code)
                IME.updateViewWordList(words)
                wordIndex = 0
                setWord(words?.get(0))
            }
        } else {
            KeyEventRepeat.reset()
            IME.currentInputConnection.deleteSurroundingText(1, 0)
        }
    }

    fun reset() {
        KeyEventRepeat.reset()
        code = ""
        words = null
        wordIndex = 0
        IME.updateViewWordList(null)
    }

    fun isT9Empty(): Boolean {
        return code.isEmpty()
    }

    private fun sendCharacter(character: Char?) {
        if (character == null) return
        val composedText = Character.toString(character)
        IME.currentInputConnection.commitText(composedText, 1)
    }

    private fun setWord(word: String?) {
        if (word == null) {
            IME.currentInputConnection.setComposingText("", 1)
            return
        }
        IME.currentInputConnection.setComposingText(word, 1)
    }

    /**
     * if in t9 mode select the current word update rating and reset
     */
    fun selectWord(): Boolean {
        if (!KeyboardSettings.t9) return false
        if (code.isEmpty()) return false
        IME.currentInputConnection.finishComposingText()
        words?.let {
            t9.updateRating(it[wordIndex % (it.size)])
        }
        reset()
        return true
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