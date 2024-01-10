package com.usfzy.photogallery

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import com.usfzy.photogallery.repository.PreferenceRepository

const val NOTIFICATION_CHANNEL_ID = "flicker_poll"

class PhotoGalleryApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        PreferenceRepository.initialize(this)

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {
            val name = getString(R.string.notification_channel_name)
            val importance = NotificationManager.IMPORTANCE_DEFAULT

            val channel = NotificationChannel(NOTIFICATION_CHANNEL_ID, name, importance)
            val notificationManager: NotificationManager =
                getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }
}