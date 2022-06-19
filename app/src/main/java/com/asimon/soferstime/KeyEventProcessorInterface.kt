package com.asimon.soferstime

import android.view.KeyEvent

interface KeyEventProcessorInterface {
    /**
     * process the onKeyDown event and decide whether to intercept (and catch at onKeyUp)
     * or to track (and catch at onKeyLongPress)
     * @param event
     * @return whether of not should we intercept
     */
    fun onKeyDown(event: KeyEvent): Boolean

    /**
     * process long press on key
     * @param event
     * @return whether or not to mark the event as canceled
     */
    fun onKeyLongPress(event: KeyEvent): Boolean

    /**
     * process normal key press
     * @param event
     * @return whether or not to pass the event to the father
     */
    fun onKeyUp(event: KeyEvent): Boolean
}