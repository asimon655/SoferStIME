package com.asimon.soferstime

object KeyboardSettings {
    var t9 = false
    val languages = listOf("he", "en")
    var currentLanguageIndex = 0
    val icons = mapOf(
        "he" to R.drawable.he_ab_icon,
        "en" to R.drawable.en_ab_icon
    )
    val iconsT9 = mapOf(
        "he" to R.drawable.he_ab_t9_icon,
        "en" to R.drawable.en_ab_t9_icon
    )
    val dictionary = mapOf(
        "he" to R.raw.he_utf8,
        "en" to R.raw.en_utf8
    )
    val directions = mapOf(
        "he" to "rtl",
        "en" to "ltr"
    )

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

    fun getCurrentDirection() : String {
        return directions[getCurrentLanguage()] ?: "ltr"
    }

    fun toggleT9() {
        t9 = !t9
    }

    fun nextLang() {
        currentLanguageIndex = (currentLanguageIndex + 1) % languages.size
    }
}