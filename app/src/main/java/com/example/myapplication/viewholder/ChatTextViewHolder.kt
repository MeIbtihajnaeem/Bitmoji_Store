package com.example.myapplication.viewholder


import android.graphics.Color
import android.view.ViewGroup
import android.widget.TextView
import com.example.myapplication.R



class ChatTextViewHolder(root: ViewGroup) : ChatViewHolder(root) {
    private val mTextView: TextView
    fun setText(text: String?) {
        mTextView.text = text
    }

    override fun setIsFromMe(isFromMe: Boolean) {
        super.setIsFromMe(isFromMe)
        if (isFromMe) {
            mTextView.setTextColor(Color.BLACK)
            mTextView.setBackgroundResource(R.drawable.chat_bubble_grey)
        } else {
            mTextView.setTextColor(Color.WHITE)
            mTextView.setBackgroundResource(R.drawable.chat_bubble_green)
        }
    }

    init {
        mTextView = root.findViewById(R.id.chat_text)
    }
}
