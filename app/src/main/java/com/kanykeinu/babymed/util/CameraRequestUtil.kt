package com.kanykeinu.babymed.util

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import com.kanykeinu.babymed.Constants.REQUEST_CODE_CAMERA
import com.kanykeinu.babymed.Constants.REQUEST_CODE_GALLERY
import com.kanykeinu.babymed.R
import com.mikelau.croperino.Croperino

class CameraRequestUtil {

    companion object {

        fun showPictureDialog(context: Activity) {
            val pictureDialog = AlertDialog.Builder(context)
            pictureDialog.setTitle(R.string.avatar_selection)
            val pictureDialogItems = arrayOf<String>(context.getString(R.string.from_gallery), context.getString(R.string.from_сamera))
            pictureDialog.setItems(pictureDialogItems
            ) { dialog, which ->
                when (which) {
                    0 -> if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                            && ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
                        Croperino.prepareGallery(context)
                    else requestGalleryPermission(context)
                    1 -> try {
                        if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED &&
                                ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                                && ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
                            Croperino.prepareCamera(context)
                        else requestCameraPermission(context)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                }
            }
            pictureDialog.show()
        }


        fun allowToPickAvatar(requestCode : Int, context: Activity){
            when (requestCode){
                REQUEST_CODE_CAMERA -> Croperino.prepareCamera(context)
                REQUEST_CODE_GALLERY -> Croperino.prepareGallery(context)
            }
        }


        private fun requestCameraPermission(context: Activity) {
            ActivityCompat.requestPermissions(context,
                    arrayOf(Manifest.permission.CAMERA,Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    REQUEST_CODE_CAMERA)
        }

        private fun requestGalleryPermission(context: Activity) {
            ActivityCompat.requestPermissions(context,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    REQUEST_CODE_GALLERY)
        }
    }
}