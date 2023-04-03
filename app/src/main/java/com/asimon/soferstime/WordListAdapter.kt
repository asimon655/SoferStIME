package com.asimon.soferstime

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat.getDrawable
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView


class WordListAdapter(val context: Context) : ListAdapter<String, WordListAdapter.WordViewHolder>(DIFF_CALLBACK) {
    private val mInflater: LayoutInflater = LayoutInflater.from(context)
    private var selectedPosition = 0
    private val selectedPos get() = if (itemCount != 0) selectedPosition % itemCount else 0


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordViewHolder {
        return WordViewHolder(mInflater.inflate(R.layout.word, parent, false))
    }

    override fun onBindViewHolder(holder: WordViewHolder, position: Int) {
        holder.textView.text = getItem(position)
        if (selectedPos == position) {
            holder.textView.background = getDrawable(context, R.drawable.rounded_corner)
        } else {
            holder.textView.setBackgroundColor(Color.WHITE)
        }
    }

    fun nextWord() {
        val oldSelected = selectedPos
        selectedPosition += 1
        notifyItemChanged(oldSelected)
        notifyItemChanged(selectedPos)
    }
    fun resetSelected() {
        val oldSelected = selectedPos
        selectedPosition = 0
        notifyItemChanged(oldSelected)
    }

    fun previousWord() {
        val oldSelected = selectedPos
        selectedPosition -= 1
        notifyItemChanged(oldSelected)
        notifyItemChanged(selectedPos)
    }

    class WordViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView: TextView = itemView.findViewById(R.id.word_text)
    }


    companion object {
        val DIFF_CALLBACK = object :
            DiffUtil.ItemCallback<String>() {
            override fun areItemsTheSame(oldWord: String, newWord: String): Boolean {
                // User properties may have changed if reloaded from the DB, but ID is fixed
                return oldWord === newWord
            }

            override fun areContentsTheSame(oldWord: String, newWord: String): Boolean {
                // NOTE: if you use equals, your object must properly override Object#equals()
                // Incorrectly returning false here will result in too many animations.
                return oldWord == newWord
            }
        }
    }
}