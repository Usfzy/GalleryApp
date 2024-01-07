package com.usfzy.photogallery

import android.app.Application
import com.usfzy.photogallery.repository.PreferenceRepository

class PhotoGalleryApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        PreferenceRepository.initialize(this)
    }
}