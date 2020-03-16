package com.example.myapplication.activities

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.myapplication.R
import com.example.myapplication.objects.imageUtils


class activity_check : AppCompatActivity() {
    private var mImageView: ImageView? = null
    private var mLoadButton: Button? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_check)
        initViews()
    }

    private fun initViews() {
        mImageView = findViewById<View>(R.id.imageView) as ImageView
        mLoadButton = findViewById<View>(R.id.loadButton) as Button
        mLoadButton!!.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    this@activity_check,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this@activity_check,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    REQUEST_STORAGE_PERMISSION
                )
            } else {
                loadImage()
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            REQUEST_STORAGE_PERMISSION -> {
                // If request is cancelled, the result arrays are empty
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) { // permission was granted, yay!
                    loadImage()
                } else { // permission denied, boo!
                    Toast.makeText(
                        this@activity_check,
                        "Image cannot be loaded.",
                        Toast.LENGTH_LONG
                    ).show()
                }
                return
            }
        }
    }

    private fun loadImage() {
        val bitmap: Bitmap? =
            imageUtils.loadImageBitmap(this@activity_check, "Hello_Worldimage_1", "png")
        mImageView!!.setImageBitmap(bitmap)
        if (bitmap == null) {
            Toast.makeText(this@activity_check, "No image found", Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        private const val REQUEST_STORAGE_PERMISSION = 102
    }
}
