package com.karimsinouh.devhub.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageActivity
import com.theartofdev.edmodo.cropper.CropImageOptions

class ImagePicker(private val context: Context) {

    val cropContract= ActivityResultContracts.StartActivityForResult().apply {

    }
    val contract=ActivityResultContracts.GetContent()

    fun open(
        launcher: ManagedActivityResultLauncher<String, Uri>
    ){
        launcher.launch("image/*")
    }

    fun openCrop(
        uri:Uri,
        launcher:ActivityResultLauncher<Intent>
    ){
        val intent = Intent()
        intent.setClass(context, CropImageActivity::class.java)
        val bundle = Bundle()
        bundle.putParcelable(CropImage.CROP_IMAGE_EXTRA_SOURCE, uri)
        bundle.putParcelable(CropImage.CROP_IMAGE_EXTRA_OPTIONS, CropImageOptions())
        intent.putExtra(CropImage.CROP_IMAGE_EXTRA_BUNDLE, bundle)
        cropContract.createIntent(context,intent)
        launcher.launch(intent)
    }

}