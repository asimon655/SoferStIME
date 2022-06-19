package com.asimon.soferstime

import android.inputmethodservice.InputMethodService
import android.util.Log
import android.view.KeyEvent

class SoferStIME : InputMethodService() {
    private var keyEventProcessor : KeyEventProcessorInterface = TODO("implement KeyEventProcessorInterface")


    override fun onCreate() {
        Log.d("soferStime", "starting...")
        super.onCreate()
    }

    // need to override this to intercept the KeyEvent in onKeyUp
    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        Log.d("soferStime", "onKeyDown() keyCode : " + keyCode + " KeyCount : " + event.repeatCount)
        return keyEventProcessor.onKeyDown(event)
    }

    // need to override this to intercept the KeyEvent in onKeyUp
    override fun onKeyLongPress(keyCode: Int, event: KeyEvent): Boolean {
        Log.d("soferStime", "onKeyLongPress() keyCode : $keyCode")
        return keyEventProcessor.onKeyLongPress(event)
    }

    // here we intercept the KeyEvent and do something with it or just pass it on
    override fun onKeyUp(keyCode: Int, event: KeyEvent): Boolean {
        Log.d("soferStime", "onKeyUp() keyCode : $keyCode")
        if (event.isCanceled) {
            return true
        }
        if (keyEventProcessor.onKeyUp(event)) {
            return true
        }
        // what wasn't handled go to default
        return super.onKeyUp(keyCode, event)
    }

}