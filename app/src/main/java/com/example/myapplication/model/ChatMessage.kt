package com.example.myapplication.model

interface ChatMessage {
    enum  class Type {
        IMAGE, TEXT
    }

    val isFromMe: Boolean
    val type: Type

}
