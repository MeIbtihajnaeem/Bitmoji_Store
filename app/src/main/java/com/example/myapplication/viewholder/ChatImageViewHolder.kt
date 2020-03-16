package com.example.myapplication.viewholder

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import com.example.myapplication.R
import com.example.myapplication.objects.imageUtils
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*



class ChatImageViewHolder(private val mContext: Context, root: ViewGroup) :
    ChatViewHolder(root) {
    private val mImageView: ImageView
    private val mLoadingIndicator: View
    private var bitmap: Bitmap? = null
    @Throws(IOException::class)
    fun setImageUrl(imageUrl: String?, preview: Drawable?) {

     Log.i("Chat Image View Holder","setImageUrl")
        mImageView.setImageDrawable(preview)
        mImageView.visibility = if (preview == null) View.GONE else View.VISIBLE
        mLoadingIndicator.visibility = if (preview == null) View.VISIBLE else View.GONE
        bitmap = (preview as BitmapDrawable)!!.bitmap
        Picasso.get()
            .load(imageUrl)
            .into(mImageView, object : com.squareup.picasso.Callback {
                override fun onSuccess() {
                    mLoadingIndicator.visibility = View.GONE
                    mImageView.visibility = View.VISIBLE
                }

                override fun onError(e: Exception?) {}
            })
        Log.i("Chat Image View Holder","setImageUrl end")
        initViews()
    }

    fun setDrawable(@DrawableRes drawableResId: Int) {
        mLoadingIndicator.visibility = View.GONE
        mImageView.setImageDrawable(mContext.resources.getDrawable(drawableResId))
    }

    fun recycle() {
        Picasso.get().cancelRequest(mImageView)
    }

    @Throws(IOException::class)
    fun onClick(v: View?) {
        val timeStamp =
            SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "PNG_" + timeStamp + "_"
        // String imageFileName = "image_1";
        bitmap?.let { imageUtils.saveImageBitmap(mContext, it, "Hello_Worldimage_1", "png") }
    }

    @Throws(IOException::class)
    private fun initViews() {
        if (ContextCompat.checkSelfPermission(
                mContext,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) { //AppCompatActivity.requestPermissions(mContext, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},REQUEST_STORAGE_PERMISSION);
        } else {
            onClick(mImageView)
        }
    }

    companion object {
        private const val REQUEST_STORAGE_PERMISSION = 101
    }

    init {
        mImageView = root.findViewById(R.id.chat_image)
        mLoadingIndicator = root.findViewById(R.id.chat_loading)
    }
}
