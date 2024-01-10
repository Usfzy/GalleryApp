package com.usfzy.photogallery.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.usfzy.photogallery.model.GalleryItem
import com.usfzy.photogallery.repository.PhotoRepository
import com.usfzy.photogallery.repository.PreferenceRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PhotoGalleryViewModel : ViewModel() {
    private val photoRepository = PhotoRepository()
    private val preferenceRepository = PreferenceRepository.get()

    private val _uiState: MutableStateFlow<PhotoGalleryUiState> = MutableStateFlow(
        PhotoGalleryUiState()
    )
    val uiState: StateFlow<PhotoGalleryUiState> get() = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            preferenceRepository.storedQuery.collectLatest { storedQuery ->
                try {
                    val items = fetchGalleryItems(storedQuery)
                    _uiState.update {
                        it.copy(
                            image = items,
                            query = storedQuery
                        )
                    }
                } catch (ex: Exception) {
                    Log.d(TAG, "EXCEPTION: ${ex.localizedMessage} ")
                }
            }
        }

        viewModelScope.launch {
            preferenceRepository.isPolling.collect { isPolling ->
                _uiState.update {
                    it.copy(isPolling = isPolling)
                }
            }
        }
    }

    fun toggleIsPolling() {
        viewModelScope.launch {
            preferenceRepository.setPolling(!uiState.value.isPolling)
        }

    }

    fun setQuery(query: String) {
        viewModelScope.launch {
            preferenceRepository.setStoredQuery(query)
        }
    }

    private suspend fun fetchGalleryItems(query: String): List<GalleryItem> {
        return if (query.isEmpty()) photoRepository.fetchPhotos()
        else photoRepository.searchPhotos(query)
    }

    companion object {
        private const val TAG = "PhotoGalleryViewModel"
    }
}

data class PhotoGalleryUiState(
    val image: List<GalleryItem> = listOf(),
    val query: String = "",
    val isPolling: Boolean = false,
)