package com.usfzy.photogallery.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.usfzy.photogallery.repository.PhotoRepository
import com.usfzy.photogallery.model.GalleryItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PhotoGalleryViewModel : ViewModel() {
    private val photoRepository = PhotoRepository()

    private val _galleryItems: MutableStateFlow<List<GalleryItem>> =
        MutableStateFlow(emptyList())
    val galleryItems: StateFlow<List<GalleryItem>> get() = _galleryItems.asStateFlow()

    init {
        viewModelScope.launch {
            try {
                val items = photoRepository.fetchPhotos()
                _galleryItems.value = items
            } catch (ex: Exception) {
                Log.d(TAG, "EXCEPTION: ${ex.localizedMessage} ")
            }
        }
    }

    companion object {
        private const val TAG = "PhotoGalleryViewModel"
    }
}