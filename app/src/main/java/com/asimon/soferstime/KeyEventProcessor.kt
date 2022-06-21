package com.asimon.soferstime

import android.content.Intent
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo

class KeyEventProcessor(private val IME: SoferStIME) {
    private val textComposer = TextComposer(IME);

    /**
     * process the onKeyDown event and decide whether to intercept (and catch at onKeyUp)
     * or to track (and catch at onKeyLongPress)
     * @param event
     * @return whether of not should we intercept
     */
    fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        val editorInfo = IME.currentInputEditorInfo
        // TYPE_CLASS_PHONE to numbers entry
        if (editorInfo.inputType and EditorInfo.TYPE_CLASS_PHONE == EditorInfo.TYPE_CLASS_PHONE) {
            //spacial handle for the center key
            if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER) return true
            // handle character typing
            if (KeyEvent.KEYCODE_0 <= keyCode && keyCode <= KeyEvent.KEYCODE_9) return true
        }
        // TYPE_CLASS_TEXT
        else if (editorInfo.inputType and EditorInfo.TYPE_CLASS_TEXT == EditorInfo.TYPE_CLASS_TEXT) {
            //spacial handle for the back del key
            if (keyCode == KeyEvent.KEYCODE_BACK) return true
            //spacial handle for the center key
            if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER) {
                event.startTracking()
                return true
            }
            //spacial handle for the pound key
            if (keyCode == KeyEvent.KEYCODE_POUND) {
                event.startTracking()
                return true
            }
            //spacial handle for the star key
            if (keyCode == KeyEvent.KEYCODE_STAR) return true
            // handle character typing
            if (KeyEvent.KEYCODE_0 <= keyCode && keyCode <= KeyEvent.KEYCODE_9) {
                event.startTracking()
                return true
            }
            //spacial handle for the menu key
            if (keyCode == KeyEvent.KEYCODE_MENU) {
                event.startTracking()
                return true
            }
        }
        // TYPE_NULL to handle moving in the app grid and typing a number
        else if (editorInfo.inputType == EditorInfo.TYPE_NULL) {
            //spacial handle for the center key
            if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER) return true
            //spacial handle for the menu key
            if (keyCode == KeyEvent.KEYCODE_MENU) {
                event.startTracking()
                return true
            }
        }
        return false
    }

    /**
     * process long press on key
     * @param event
     * @return whether or not to mark the event as canceled
     */
    fun onKeyLongPress(keyCode: Int, event: KeyEvent): Boolean {
        val editorInfo = IME.currentInputEditorInfo
        // TYPE_CLASS_TEXT
        if (editorInfo.inputType and EditorInfo.TYPE_CLASS_TEXT == EditorInfo.TYPE_CLASS_TEXT) {
            //spacial handle for the center key
            if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER) {
                startDictation()
                return true
            }
            //spacial handle for the center key
            if (keyCode == KeyEvent.KEYCODE_POUND) {
                KeyboardSettings.toggleT9()
                updateStatusIcon()
                return true
            }
            // handle character typing
            if (KeyEvent.KEYCODE_0 <= keyCode && keyCode <= KeyEvent.KEYCODE_9) {
                textComposer.addKeyLongPress(keyCode)
                return true
            }
            //spacial handle for the menu key
            if (keyCode == KeyEvent.KEYCODE_MENU) {
                startAssistant()
                return true
            }
        }
        // TYPE_NULL to handle start assistant in the home screen
        else if (editorInfo.inputType == EditorInfo.TYPE_NULL) {
            //spacial handle for the menu key
            if (keyCode == KeyEvent.KEYCODE_MENU) {
                startAssistant()
                return true
            }
        }
        return false
    }

    /**
     * process normal key press
     * @param event
     * @return whether or not to pass the event to the father
     */
    fun onKeyUp(keyCode: Int, event: KeyEvent): Boolean {
        val editorInfo = IME.currentInputEditorInfo
        // TYPE_CLASS_PHONE to numbers entry
        if (editorInfo.inputType and EditorInfo.TYPE_CLASS_PHONE == EditorInfo.TYPE_CLASS_PHONE) {
            //spacial handle for the center key
            if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER) {
                performEnterAction()
                return true
            }
            // handle character typing
            if (KeyEvent.KEYCODE_0 <= keyCode && keyCode <= KeyEvent.KEYCODE_9) {
                textComposer.addKeyNum(keyCode)
                return true
            }
        }
        // TYPE_CLASS_TEXT
        else if (editorInfo.inputType and EditorInfo.TYPE_CLASS_TEXT == EditorInfo.TYPE_CLASS_TEXT) {
            //spacial handle for the back del key
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                handleBackKey()
                return true
            }
            //spacial handle for the center key
            if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER) {
                performEnterAction()
                return true
            }
            //special handle for the starkey
            if (keyCode == KeyEvent.KEYCODE_STAR) {
                textComposer.nextWord()
                return true
            }
            //spacial handle for the center key
            if (keyCode == KeyEvent.KEYCODE_POUND) {
                KeyboardSettings.nextLang()
                updateStatusIcon()
                return true
            }
            // handle character typing
            if (KeyEvent.KEYCODE_0 <= keyCode && keyCode <= KeyEvent.KEYCODE_9) {
                textComposer.addKey(keyCode)
                return true
            }
        }
        // TYPE_NULL to handle open app in the home screen
        else if (editorInfo.inputType == EditorInfo.TYPE_NULL) {
            //spacial handle for the center key
            if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER) {
                performEnterAction()
                return true
            }
        }
        return false
    }


    private fun handleBackKey() {
        val currentInputConnection = IME.currentInputConnection
        // if text is out of the question
        if (currentInputConnection == null) {
            hitKey(KeyEvent.KEYCODE_BACK)
            return
        }
        // check if there is some text
        var text = currentInputConnection.getTextBeforeCursor(1, 0)
        if (text != null && text.isNotEmpty()) {
            textComposer.delete()
            return
        }
        // if there is no text at all
        text = currentInputConnection.getTextAfterCursor(1, 0)
        if (text == null || text.isEmpty()) {
            hitKey(KeyEvent.KEYCODE_BACK)
        }
    }

    private fun performEnterAction() {
        IME.currentInputConnection.performEditorAction(IME.currentInputEditorInfo.actionId)
    }

    private fun startAssistant() {
        IME.startActivity(Intent(Intent.ACTION_VOICE_COMMAND).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
    }

    private fun startDictation() {
        IME.switchInputMethod("com.google.android.googlequicksearchbox/com.google.android.voicesearch.ime.VoiceInputMethodService")
    }

    private fun hitKey(keyCode: Int) {
        IME.currentInputConnection.sendKeyEvent(KeyEvent(KeyEvent.ACTION_DOWN, keyCode))
        IME.currentInputConnection.sendKeyEvent(KeyEvent(KeyEvent.ACTION_UP, keyCode))
    }

    fun updateStatusIcon() {
        val editorInfo = IME.currentInputEditorInfo
        if (editorInfo.inputType and EditorInfo.TYPE_CLASS_PHONE == EditorInfo.TYPE_CLASS_PHONE) {
            IME.showStatusIcon(R.drawable.num_icon)
        }
        else if (editorInfo.inputType and EditorInfo.TYPE_CLASS_TEXT == EditorInfo.TYPE_CLASS_TEXT) {
            IME.showStatusIcon(KeyboardSettings.getCurrentIcon())
        }
        else {
            IME.hideStatusIcon()
        }
    }
}