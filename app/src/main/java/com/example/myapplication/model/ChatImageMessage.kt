package com.example.myapplication.model

import androidx.annotation.DrawableRes


class ChatImageMessage(
    override val isFromMe: Boolean, @get:DrawableRes
    @param:DrawableRes val drawableResId: Int
) :
    ChatMessage {

    override val type: ChatMessage.Type
        get() = ChatMessage.Type.IMAGE

}
