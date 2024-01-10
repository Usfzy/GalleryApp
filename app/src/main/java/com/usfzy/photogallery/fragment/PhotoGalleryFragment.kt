package com.usfzy.photogallery.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.usfzy.photogallery.R
import com.usfzy.photogallery.adapter.PhotoGalleryAdapter
import com.usfzy.photogallery.databinding.FragmentPhotoGalleryBinding
import com.usfzy.photogallery.viewmodel.PhotoGalleryViewModel
import com.usfzy.photogallery.worker.PollWorker
import kotlinx.coroutines.launch

class PhotoGalleryFragment : Fragment(), MenuProvider {

    private var _binding: FragmentPhotoGalleryBinding? = null
    private val binding: FragmentPhotoGalleryBinding
        get() = checkNotNull(_binding) {
            "Binding cannot be null"
        }

    private val photoGalleryViewModel: PhotoGalleryViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.UNMETERED)
            .build()

        val workRequest = OneTimeWorkRequest
            .Builder(PollWorker::class.java)
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance(requireContext()).enqueue(workRequest)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        _binding = FragmentPhotoGalleryBinding.inflate(layoutInflater, container, false)
        binding.photoGrid.layoutManager = GridLayoutManager(context, 3)

        (requireActivity() as MenuHost).addMenuProvider(this)

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

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.fragment_photo_gallery, menu)

        val searchItem: MenuItem = menu.findItem(R.id.menu_item_search)
        val searchView = searchItem.actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }

            override fun onQueryTextSubmit(query: String?): Boolean {
                photoGalleryViewModel.setQuery(query ?: "")
                return true
            }
        })
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return when (menuItem.itemId) {
            R.id.menu_item_clear -> {
                photoGalleryViewModel.setQuery("")
                return true
            }

            else -> false
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