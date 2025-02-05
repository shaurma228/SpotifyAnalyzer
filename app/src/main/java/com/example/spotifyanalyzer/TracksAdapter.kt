// TracksAdapter.kt
package com.example.spotifyanalyzer

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.spotifyanalyzer.databinding.ItemTrackBinding
import com.example.spotifyanalyzer.viewmodel.TrackData

class TracksAdapter : ListAdapter<TrackData, TracksAdapter.TrackViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val binding = ItemTrackBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TrackViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class TrackViewHolder(private val binding: ItemTrackBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(track: TrackData) {
            binding.trackName.text = track.name
            binding.artistName.text = track.artist
            binding.playsCount.text = "Прослушано раз: ${track.plays}"
            binding.timePlayed.text = "Время: ${track.timePlayed / 60000} мин"
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<TrackData>() {
        override fun areItemsTheSame(oldItem: TrackData, newItem: TrackData): Boolean {

            return oldItem.name == newItem.name && oldItem.artist == newItem.artist
        }

        override fun areContentsTheSame(oldItem: TrackData, newItem: TrackData): Boolean {
            return oldItem == newItem
        }
    }
}
