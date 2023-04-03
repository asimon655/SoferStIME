package com.asimon.soferstime

import android.view.KeyEvent
import java.util.*

object KeyMap {
    val enMap = mapOf(
        KeyEvent.KEYCODE_0 to listOf(' ', '0'),
        KeyEvent.KEYCODE_1 to listOf('.', ',', '\'', '"', '1'),
        KeyEvent.KEYCODE_2 to listOf('a', 'b', 'c', '2'),
        KeyEvent.KEYCODE_3 to listOf('d', 'e', 'f', '3'),
        KeyEvent.KEYCODE_4 to listOf('g', 'h', 'i', '4'),
        KeyEvent.KEYCODE_5 to listOf('j', 'k', 'l', '5'),
        KeyEvent.KEYCODE_6 to listOf('m', 'n', 'o', '6'),
        KeyEvent.KEYCODE_7 to listOf('p', 'q', 'r', 's', '7'),
        KeyEvent.KEYCODE_8 to listOf('t', 'u', 'v', '8'),
        KeyEvent.KEYCODE_9 to listOf('w', 'x', 'y', 'z', '9')
    )
    val heMap = mapOf(
        KeyEvent.KEYCODE_0 to listOf(' ', '0'),
        KeyEvent.KEYCODE_1 to listOf('.', ',', '?', '"', '\'', '!', '@', '1'),
        KeyEvent.KEYCODE_2 to listOf('ד', 'ה', 'ו', '2'),
        KeyEvent.KEYCODE_3 to listOf('א', 'ב', 'ג', '3'),
        KeyEvent.KEYCODE_4 to listOf('מ', 'ם', 'נ', 'ן', '4'),
        KeyEvent.KEYCODE_5 to listOf('י', 'כ', 'ך', 'ל', '5'),
        KeyEvent.KEYCODE_6 to listOf('ז', 'ח', 'ט', '6'),
        KeyEvent.KEYCODE_7 to listOf('ר', 'ש', 'ת', '7'),
        KeyEvent.KEYCODE_8 to listOf('צ', 'ץ', 'ק', '8'),
        KeyEvent.KEYCODE_9 to listOf('ס', 'ע', 'פ', 'ף', '9')
    )
    val numbersMap = mapOf(
        KeyEvent.KEYCODE_0 to '0',
        KeyEvent.KEYCODE_1 to '1',
        KeyEvent.KEYCODE_2 to '2',
        KeyEvent.KEYCODE_3 to '3',
        KeyEvent.KEYCODE_4 to '4',
        KeyEvent.KEYCODE_5 to '5',
        KeyEvent.KEYCODE_6 to '6',
        KeyEvent.KEYCODE_7 to '7',
        KeyEvent.KEYCODE_8 to '8',
        KeyEvent.KEYCODE_9 to '9'
    )

    private val langMap = mapOf(
        "en" to enMap,
        "he" to heMap
    )

    private val reversedKeyMapEn: Map<Char, Int>
    private val reversedKeyMapHe: Map<Char, Int>

    private val reversedLangMap: Map<String, Map<Char, Int>>

    init {
        reversedKeyMapEn = reverseKeyMap(enMap)
        reversedKeyMapHe = reverseKeyMap(heMap)

        reversedLangMap = mapOf(
            "en" to reversedKeyMapEn,
            "he" to reversedKeyMapHe
        )
    }

    private fun getCurrentLangMap() : Map<Int, List<Char>> {
        return langMap[KeyboardSettings.getCurrentLanguage()]!!
    }

    private fun getCurrentReversedLangMap() : Map<Char, Int> {
        return reversedLangMap[KeyboardSettings.getCurrentLanguage()]!!
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
        return numbersMap[keyCode]
    }

    fun calcHash(word: String?): String {
        var word = word
        word = word!!.lowercase(Locale.forLanguageTag(KeyboardSettings.getCurrentLanguage()))
        val hash = word.toCharArray()
        for (i in hash.indices) {
            hash[i] = numbersMap[getCurrentReversedLangMap()[hash[i]]]!!
                .toChar()
        }
        return String(hash)
    }

    fun calcHash(word: String?, language: String): String {
        var word = word
        word = word!!.lowercase(Locale.forLanguageTag(KeyboardSettings.getCurrentLanguage()))
        val hash = word.toCharArray()
        for (i in hash.indices) {
            hash[i] = numbersMap[reversedLangMap[language]!![hash[i]]]!!
                .toChar()
        }
        return String(hash)
    }

    private fun reverseKeyMap(keyMap: Map<Int, List<Char>>): Map<Char, Int> {
        val reversedKeyMap: MutableMap<Char, Int> = HashMap()
        for (key in keyMap.keys) {
            for (character in keyMap[key]!!) {
                reversedKeyMap[character] = key
            }
        }
        return reversedKeyMap
    }
}