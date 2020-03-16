package com.example.myapplication.model

class ChatTextMessage(override val isFromMe: Boolean, val text: String) :
    ChatMessage {

    override val type: ChatMessage.Type
        get() = ChatMessage.Type.TEXT

}
