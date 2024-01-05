package com.usfzy.photogallery.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import com.usfzy.photogallery.adapter.PhotoGalleryAdapter
import com.usfzy.photogallery.databinding.FragmentPhotoGalleryBinding
import com.usfzy.photogallery.viewmodel.PhotoGalleryViewModel
import kotlinx.coroutines.launch

class PhotoGalleryFragment : Fragment() {

    private var _binding: FragmentPhotoGalleryBinding? = null
    private val binding: FragmentPhotoGalleryBinding
        get() = checkNotNull(_binding) {
            "Binding cannot be null"
        }

    private val photoGalleryViewModel: PhotoGalleryViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        _binding = FragmentPhotoGalleryBinding.inflate(layoutInflater, container, false)
        binding.photoGrid.layoutManager = GridLayoutManager(context, 3)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                photoGalleryViewModel.galleryItems.collect {
                    binding.photoGrid.adapter = PhotoGalleryAdapter(it)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }

    companion object {
        private const val TAG = "PhotoGalleryFragment"
    }
}