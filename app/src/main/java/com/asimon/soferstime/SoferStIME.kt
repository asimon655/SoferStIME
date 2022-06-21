package com.asimon.soferstime

import android.inputmethodservice.InputMethodService
import android.util.Log
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo

class SoferStIME : InputMethodService() {
    private val keyEventProcessor = KeyEventProcessor(this)

    override fun onCreate() {
        Log.d("soferStime", "starting...")
        super.onCreate()
    }

    // need to override this to intercept the KeyEvent in onKeyUp
    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        Log.d("soferStime", "onKeyDown() keyCode : " + keyCode + " KeyCount : " + event.repeatCount)
        return keyEventProcessor.onKeyDown(keyCode, event)
    }

    // need to override this to intercept the KeyEvent in onKeyUp
    override fun onKeyLongPress(keyCode: Int, event: KeyEvent): Boolean {
        Log.d("soferStime", "onKeyLongPress() keyCode : $keyCode")
        return keyEventProcessor.onKeyLongPress(keyCode, event)
    }

    // here we intercept the KeyEvent and do something with it or just pass it on
    override fun onKeyUp(keyCode: Int, event: KeyEvent): Boolean {
        Log.d("soferStime", "onKeyUp() keyCode : $keyCode")
        if (event.isCanceled) {
            return true
        }
        if (keyEventProcessor.onKeyUp(keyCode, event)) {
            return true
        }
        // what wasn't handled go to default
        return super.onKeyUp(keyCode, event)
    }

    override fun onStartInput(editorInfo: EditorInfo, restarting: Boolean) {
        keyEventProcessor.updateStatusIcon()
        Log.d(
            "soferStime",
            """
            CLASS : ${editorInfo.inputType and EditorInfo.TYPE_MASK_CLASS}
            FLAG : ${editorInfo.inputType and EditorInfo.TYPE_MASK_FLAGS}
            VARIATION : ${editorInfo.inputType and EditorInfo.TYPE_MASK_VARIATION}
            """.trimIndent()
        )
        Log.d("soferStime", "action label : " + editorInfo.actionLabel)
    }
}