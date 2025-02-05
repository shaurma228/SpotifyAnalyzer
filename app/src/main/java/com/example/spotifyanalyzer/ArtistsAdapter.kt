package com.example.spotifyanalyzer

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.spotifyanalyzer.databinding.ItemArtistBinding
import com.example.spotifyanalyzer.viewmodel.ArtistData

class ArtistsAdapter : ListAdapter<ArtistData, ArtistsAdapter.ArtistViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArtistViewHolder {
        val binding = ItemArtistBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ArtistViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ArtistViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ArtistViewHolder(private val binding: ItemArtistBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(artistData: ArtistData) {
            binding.artistName.text = artistData.name
            binding.playsCount.text = "Прослушано раз: ${artistData.plays}"
            binding.timePlayed.text = "Время: ${artistData.timePlayed / 60000} мин"
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<ArtistData>() {
        override fun areItemsTheSame(oldItem: ArtistData, newItem: ArtistData): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: ArtistData, newItem: ArtistData): Boolean {
            return oldItem == newItem
        }
    }
}
