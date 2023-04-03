package com.asimon.soferstime

import android.content.Context
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class WordListView(context: Context): LinearLayout(context) {
    private val adapter = WordListAdapter(context)
    private var listView: RecyclerView
    init {
        inflate(this.context, R.layout.word_list, this)
        listView = findViewById(R.id.word_list)
        listView.adapter = adapter
        listView.layoutManager = LinearLayoutManager(this.context,
            LinearLayoutManager.HORIZONTAL,
            KeyboardSettings.getCurrentDirection() == "rtl"
        )
    }

    fun updateWordList(wordList: List<String>?) {
        adapter.submitList(wordList)
        adapter.resetSelected()
    }

    fun nextWord() {
        adapter.nextWord()
    }

    fun previousWord() {
        adapter.previousWord()
    }
}