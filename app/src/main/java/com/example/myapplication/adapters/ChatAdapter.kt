package com.example.myapplication.adapters

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.model.ChatImageMessage
import com.example.myapplication.model.ChatImageUrlMessage
import com.example.myapplication.model.ChatMessage
import com.example.myapplication.model.ChatTextMessage
import com.example.myapplication.viewholder.ChatImageViewHolder
import com.example.myapplication.viewholder.ChatTextViewHolder
import com.example.myapplication.viewholder.ChatViewHolder
import java.io.IOException
import java.util.*



 class ChatAdapter : RecyclerView.Adapter<ChatViewHolder>() {
    private val mMessages= ArrayList<ChatMessage>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val context = parent.context

        if(getMessageType(viewType)==ChatMessage.Type.TEXT){
            return ChatTextViewHolder(
                View.inflate(context, R.layout.chat_text_view, null) as ViewGroup
            )
        }
        else if(getMessageType(viewType)==ChatMessage.Type.IMAGE){
            return ChatImageViewHolder(
                context,
                View.inflate(context, R.layout.chat_image_view, null) as ViewGroup
            )

        }
 return null as ChatImageViewHolder
    }



    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        val message: ChatMessage = getMessage(position)
        holder.setIsFromMe(message.isFromMe)
        if (message is ChatImageMessage) {
            (holder as ChatImageViewHolder).setDrawable(
                (message ).drawableResId
            )
        } else if (message is ChatImageUrlMessage) {
            try {
                (holder as ChatImageViewHolder).setImageUrl(
                    (message ).imageUrl,
                    (message ).preview
                )
            } catch (e: IOException) {
                e.printStackTrace()
            }
        } else if (message is ChatTextMessage) {
            (holder as ChatTextViewHolder).setText(
                (message).text
            )
        }
    }

    override fun getItemViewType(position: Int): Int {
        return getMessage(position).type?.ordinal as Int

    }

    override fun getItemId(position: Int): Long { // Chats don't change
        return getPositionInMessagesLong(position)
    }

    fun onViewRecycled(holder: ChatViewHolder?) {
        if (holder is ChatImageViewHolder) {
            holder.recycle()
        }
    }


    override fun getItemCount(): Int {
        return mMessages.size
    }
   fun add(message: ChatMessage) {

        mMessages.add(message)
        notifyDataSetChanged()
    }

    private fun getMessage(position: Int): ChatMessage {
        return mMessages[getPositionInMessagesInt(position) as Int]
    }

    private fun getPositionInMessagesInt(position: Int): Int { // messages are stored in reverse order
        return mMessages.size - 1 - position
    }
     private fun getPositionInMessagesLong(position: Int): Long { // messages are stored in reverse order
         return (mMessages.size - 1 - position).toLong()
     }

    private  fun getMessageType(viewType: Int): ChatMessage.Type {
        return ChatMessage.Type.values()[viewType]
    }
}