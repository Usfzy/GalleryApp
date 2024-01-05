package com.usfzy.photogallery.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.usfzy.photogallery.databinding.ItemPhotoGalleryBinding
import com.usfzy.photogallery.model.GalleryItem

class PhotoViewHolder(private val binding: ItemPhotoGalleryBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(item: GalleryItem) {
        binding.imageGallery.load(item.url)
    }
}

class PhotoGalleryAdapter(private val items: List<GalleryItem>) :
    RecyclerView.Adapter<PhotoViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemPhotoGalleryBinding.inflate(inflater, parent, false)

        return PhotoViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
    }
}