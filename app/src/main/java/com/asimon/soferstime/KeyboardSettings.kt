package com.asimon.soferstime

import java.util.*

object KeyboardSettings {
    var t9 = false
    val languages = listOf("he", "en")
    var currentLanguageIndex = 0
    val icons = HashMap<String, Int>()
    val iconsT9 = HashMap<String, Int>()

    init {
        icons.run {
            put("he", R.drawable.he_ab_icon)
            put("en", R.drawable.en_ab_icon)
        }
        iconsT9.run {
            put("he", R.drawable.he_ab_t9_icon)
            put("en", R.drawable.en_ab_t9_icon)
        }
    }

    fun getCurrentIcon() : Int {
        val currentLanguage = getCurrentLanguage()
        return if (t9) {
            iconsT9[currentLanguage] ?: R.drawable.he_ab_icon
        } else {
            icons[currentLanguage] ?: R.drawable.he_ab_icon
        }
    }

    fun getCurrentLanguage() : String {
        return languages[currentLanguageIndex]
    }

    fun toggleT9() {
        t9 = !t9
    }

    fun nextLang() {
        currentLanguageIndex = (currentLanguageIndex + 1) % languages.size
    }
}