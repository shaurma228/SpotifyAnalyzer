package com.example.spotifyanalyzer

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.spotifyanalyzer.databinding.ItemAlbumBinding
import com.example.spotifyanalyzer.viewmodel.AlbumData

class AlbumsAdapter : ListAdapter<AlbumData, AlbumsAdapter.AlbumViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumViewHolder {
        val binding = ItemAlbumBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AlbumViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AlbumViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class AlbumViewHolder(private val binding: ItemAlbumBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(albumData: AlbumData) {
            binding.albumName.text = albumData.name
            binding.artistName.text = albumData.artist
            binding.playsCount.text = "Прослушано раз: ${albumData.trackPlayed}"
            binding.timePlayed.text = "Время: ${albumData.timePlayed / 60000} мин"
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<AlbumData>() {
        override fun areItemsTheSame(oldItem: AlbumData, newItem: AlbumData): Boolean {
            return oldItem.name == newItem.name && oldItem.artist == newItem.artist
        }

        override fun areContentsTheSame(oldItem: AlbumData, newItem: AlbumData): Boolean {
            return oldItem == newItem
        }
    }
}
