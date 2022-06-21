package com.asimon.soferstime

import android.view.KeyEvent
import java.util.*
import kotlin.collections.HashMap

object KeyMap {
    val enMap = HashMap<Int, List<Char>>()
    val heMap = HashMap<Int, List<Char>>()
    val numbersMap = HashMap<Int, Char>()

    private val langMap = HashMap<String, Map<Int, List<Char>>>()

    init {
        langMap.run {
            put("en", enMap)
            put("he", heMap)
        }

        enMap.run {
            put(KeyEvent.KEYCODE_0, listOf(' ', '0'))
            put(KeyEvent.KEYCODE_1, listOf('.', ',', '\'', '"', '1'))
            put(KeyEvent.KEYCODE_2, listOf('a', 'b', 'c', '2'))
            put(KeyEvent.KEYCODE_3, listOf('d', 'e', 'f', '3'))
            put(KeyEvent.KEYCODE_4, listOf('g', 'h', 'i', '4'))
            put(KeyEvent.KEYCODE_5, listOf('j', 'k', 'l', '5'))
            put(KeyEvent.KEYCODE_6, listOf('m', 'n', 'o', '6'))
            put(KeyEvent.KEYCODE_7, listOf('p', 'q', 'r', 's', '7'))
            put(KeyEvent.KEYCODE_8, listOf('t', 'u', 'v', '8'))
            put(KeyEvent.KEYCODE_9, listOf('w', 'x', 'y', 'z', '9'))
        }
        heMap.run {
            put(KeyEvent.KEYCODE_0, listOf(' ', '0'))
            put(KeyEvent.KEYCODE_1, listOf('.', ',', '?', '"', '\'', '!', '@', '1'))
            put(KeyEvent.KEYCODE_2, listOf('ד', 'ה', 'ו', '2'))
            put(KeyEvent.KEYCODE_3, listOf('א', 'ב', 'ג', '3'))
            put(KeyEvent.KEYCODE_4, listOf('מ', 'ם', 'נ', 'ן', '4'))
            put(KeyEvent.KEYCODE_5, listOf('י', 'כ', 'ך', 'ל', '5'))
            put(KeyEvent.KEYCODE_6, listOf('ז', 'ח', 'ט', '6'))
            put(KeyEvent.KEYCODE_7, listOf('ר', 'ש', 'ת', '7'))
            put(KeyEvent.KEYCODE_8, listOf('צ', 'ץ', 'ק', '8'))
            put(KeyEvent.KEYCODE_9, listOf('ס', 'ע', 'פ', 'ף', '9'))
        }
        numbersMap.run {
            put(KeyEvent.KEYCODE_0, '0')
            put(KeyEvent.KEYCODE_1, '1')
            put(KeyEvent.KEYCODE_2, '2')
            put(KeyEvent.KEYCODE_3, '3')
            put(KeyEvent.KEYCODE_4, '4')
            put(KeyEvent.KEYCODE_5, '5')
            put(KeyEvent.KEYCODE_6, '6')
            put(KeyEvent.KEYCODE_7, '7')
            put(KeyEvent.KEYCODE_8, '8')
            put(KeyEvent.KEYCODE_9, '9')
        }
    }

    private fun getCurrentLangMap() : Map<Int, List<Char>> {
        return langMap[KeyboardSettings.getCurrentLanguage()]!!
    }

    /**
     * retrieve mapped character by a keycode and repeat count
     * @param keyCode the key code
     * @param repeat the repeat pressing count on the key
     * @return mapped character or null if not found
     */
    fun getCharacter(keyCode: Int, repeat: Int): Char? {
        val characters: List<Char> = getCurrentLangMap()[keyCode] ?: return null
        return characters[repeat % characters.size]
    }


    /**
     * retrieve mapped number by a keycode
     * @param keyCode the key code
     * @return mapped character or null if not found
     */
    fun getNumber(keyCode: Int): Char? {
        val characters: List<Char> = getCurrentLangMap()[keyCode] ?: return null
        return characters[characters.size - 1]
    }
}