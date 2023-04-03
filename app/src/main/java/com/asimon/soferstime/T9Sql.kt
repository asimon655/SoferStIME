package com.asimon.soferstime

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns
import android.util.Log
import android.widget.Toast
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.util.*

class T9Sql(private var mContext: Context?) :
    SQLiteOpenHelper(
        mContext, DB_NAME, null, DATABASE_VERSION
    ) {
    /* Inner class that defines the table contents */
    object WordsDictionary : BaseColumns {
        const val TABLE_BASE_NAME = "words_"
        const val COLUMN_NAME_WORD = "word"
        const val COLUMN_NAME_CODE = "code"
        const val COLUMN_NAME_RATING = "rating"
    }

    override fun onCreate(db: SQLiteDatabase) {
        for (language in KeyboardSettings.languages) {
            db.execSQL(SQL_CREATE_ENTRIES(language))
            loadDictionary(db, language)
        }
    }

    override fun onUpgrade(db: SQLiteDatabase, i: Int, i1: Int) {}

    private fun loadDictionary(db: SQLiteDatabase, language: String) {
        Log.d("T9", "starting...")
        val dictionary = KeyboardSettings.dictionary[language]
        if (dictionary == null) {
            Toast.makeText(mContext, "missing dictionary for $language", Toast.LENGTH_LONG).show()
            return
        }
        val input: InputStream = mContext!!.resources.openRawResource(dictionary)
        try {
            BufferedReader(InputStreamReader(input)).use { br ->
                var line: String?
                while (br.readLine().also { line = it } != null) {
                    // use line here
                    val cv = ContentValues()
                    cv.put(WordsDictionary.COLUMN_NAME_WORD, line)
                    cv.put(WordsDictionary.COLUMN_NAME_CODE, KeyMap.calcHash(line, language))
                    cv.put(WordsDictionary.COLUMN_NAME_RATING, 0)
                    db.insert(WordsDictionary.TABLE_BASE_NAME + language, null, cv)
                }
            }
        } catch (e: IOException) {
            Log.d("soferStime", "got an error")
            e.printStackTrace()
        }
        Log.d("T9", "finished!!!")
    }

    fun getWordsByCode(code: String): List<String>? {
        if (code.isEmpty()) return null
        val db = this.readableDatabase
        val cursor = db.query(
            WordsDictionary.TABLE_BASE_NAME + KeyboardSettings.getCurrentLanguage(),
            arrayOf(WordsDictionary.COLUMN_NAME_WORD),
            WordsDictionary.COLUMN_NAME_CODE + "=?", arrayOf(code),
            null,
            null,
            WordsDictionary.COLUMN_NAME_RATING + " DESC",
            "20"
        )
        val res = LinkedList<String>()
        if (cursor.moveToFirst()) {
            do {
                res.add(cursor.getString(0))
            } while (cursor.moveToNext())
            cursor.close()
            return res
        }
        return null
    }

    fun updateRating(word: String) {
        val db = this.writableDatabase
        val cv = ContentValues()
        cv.put(WordsDictionary.COLUMN_NAME_RATING, WordsDictionary.COLUMN_NAME_RATING + "+1")
        db.update(
            WordsDictionary.TABLE_BASE_NAME + KeyboardSettings.getCurrentLanguage(),
            cv,
            WordsDictionary.COLUMN_NAME_WORD + "=?", arrayOf(word)
        )
    }

    fun dbReadTest(): String {
        val db = this.readableDatabase
        val cursor = db.query(
            WordsDictionary.TABLE_BASE_NAME + KeyboardSettings.getCurrentLanguage(),
            arrayOf(WordsDictionary.COLUMN_NAME_WORD, WordsDictionary.COLUMN_NAME_CODE),
            WordsDictionary.COLUMN_NAME_WORD + " = \"Simon\"",
            null,
            null,
            null,
            null,
            "20"
        )
        if (cursor.moveToFirst()) {
            val code = cursor.getInt(1)
            cursor.close()
            return code.toString()
        }
        return "error"
    }

    companion object {
        const val DATABASE_VERSION = 1
        const val DB_NAME = "T9Words.db"
        private fun SQL_CREATE_ENTRIES(language: String) =
            "CREATE TABLE IF NOT EXISTS " + WordsDictionary.TABLE_BASE_NAME + language + " (" +
                    WordsDictionary.COLUMN_NAME_WORD + " STRING PRIMARY KEY," +
                    WordsDictionary.COLUMN_NAME_CODE + " INTEGER," +
                    WordsDictionary.COLUMN_NAME_RATING + " INTEGER)"
    }
}