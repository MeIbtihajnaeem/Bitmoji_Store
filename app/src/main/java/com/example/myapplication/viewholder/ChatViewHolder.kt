package com.example.myapplication.viewholder

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R


abstract class ChatViewHolder(private val mRoot: ViewGroup) : RecyclerView.ViewHolder(
    mRoot
) {
    private val mSpacer: View?
    private var mIsFromMe = true
    open fun setIsFromMe(isFromMe: Boolean) {
        if (mSpacer == null || isFromMe == mIsFromMe) {
            return
        }
        if (isFromMe) {
            mRoot.removeView(mSpacer)
            mRoot.addView(mSpacer, 0)
        } else {
            mSpacer.bringToFront()
        }
        mIsFromMe = isFromMe
    }

    init {
        mSpacer = mRoot.findViewById(R.id.chat_spacer)
    }
}
