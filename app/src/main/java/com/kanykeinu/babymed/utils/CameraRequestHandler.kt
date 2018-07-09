package com.kanykeinu.babymed.utils

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.kanykeinu.babymed.utils.Constants.REQUEST_CODE_CAMERA
import com.kanykeinu.babymed.utils.Constants.REQUEST_CODE_GALLERY
import com.kanykeinu.babymed.R
import com.mikelau.croperino.Croperino
import com.mikelau.croperino.CroperinoConfig
import com.mikelau.croperino.CroperinoFileUtil

class CameraRequestHandler {

    companion object {

        fun showPictureDialog(context: Activity) {
            val pictureDialog = AlertDialog.Builder(context)
            pictureDialog.setTitle(R.string.avatar_selection)
            val pictureDialogItems = arrayOf<String>(context.getString(R.string.from_gallery), context.getString(R.string.from_сamera))
            pictureDialog.setItems(pictureDialogItems
            ) { _, which ->
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

        fun handleOnActivityResult(requestCode: Int, resultCode: Int, data: Intent?, context: Activity ) : Uri?{
            when (requestCode) {
                CroperinoConfig.REQUEST_TAKE_PHOTO -> if (resultCode == Activity.RESULT_OK) {
                    Croperino.runCropImage(CroperinoFileUtil.getTempFile(), context, true, 1, 1, R.color.gray, R.color.gray_variant)
                }
                CroperinoConfig.REQUEST_PICK_FILE -> if (resultCode == Activity.RESULT_OK) {
                    CroperinoFileUtil.newGalleryFile(data, context)
                    Croperino.runCropImage(CroperinoFileUtil.getTempFile(), context, true, 0, 0, R.color.gray, R.color.gray_variant)
                }
                CroperinoConfig.REQUEST_CROP_PHOTO -> if (resultCode == Activity.RESULT_OK) {
                    val photo = Uri.fromFile(CroperinoFileUtil.getTempFile())
                    return photo
                }
                else -> {}
            }
            return null
        }

        fun handleRequestPermissionResult(requestCode: Int,grantResults: IntArray,context: Activity){
            when (requestCode) {
                REQUEST_CODE_CAMERA -> {
                    if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                        allowToPickAvatar(REQUEST_CODE_CAMERA, context)
                    }
                }
                REQUEST_CODE_GALLERY -> {
                    if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        allowToPickAvatar(REQUEST_CODE_GALLERY, context)
                    }
                }
                else -> {}
            }
        }
    }
}