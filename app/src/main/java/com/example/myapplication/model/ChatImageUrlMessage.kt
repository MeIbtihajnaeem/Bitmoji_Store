package com.example.myapplication.model

import android.graphics.drawable.Drawable
import java.lang.ref.WeakReference

class ChatImageUrlMessage(
    override val isFromMe: Boolean,
    val imageUrl: String,
    previewDrawable: Drawable
) :
    ChatMessage {
    private val mPreviewDrawable: WeakReference<Drawable>

    val preview: Drawable?
        get() = mPreviewDrawable.get()

    override val type: ChatMessage.Type
        get() = ChatMessage.Type.IMAGE

    init {
        mPreviewDrawable = WeakReference(previewDrawable)
    }
}

