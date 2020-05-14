package org.dailykit.util

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import android.widget.Toast

/**
 * Created by Danish Rafique on 07-12-2018.
 */
class MarshMallowPermission(internal var activity: Activity) {

    fun checkPermissionForExternalStorage(): Boolean {
        val result = ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        return result == PackageManager.PERMISSION_GRANTED
    }

    fun requestPermissionForExternalStorage() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            Toast.makeText(activity, "External Storage permission needed. Please allow in App Settings for additional functionality.", Toast.LENGTH_LONG).show()
        } else {
            ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE)
        }
    }

    fun checkPermissionForPhoneState(): Boolean {
        val result = ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_PHONE_STATE)
        return result == PackageManager.PERMISSION_GRANTED
    }

    fun checkPermissionForCamera(): Boolean {
        val result = ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA)
        return result == PackageManager.PERMISSION_GRANTED
    }

    fun requestPermissionForPhoneState() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.READ_PHONE_STATE)) {
            Toast.makeText(activity, "Read Phone State permission needed. Please allow in App Settings for additional functionality.", Toast.LENGTH_LONG).show()
        } else {
            ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.READ_PHONE_STATE), READ_PHONE_STATE_PERMISSION_REQUEST_CODE)
        }
    }

    companion object {
        val EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE = 2
        val READ_PHONE_STATE_PERMISSION_REQUEST_CODE = 3
    }

}

