package com.example.spotifyanalyzer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.spotifyanalyzer.databinding.FragmentArtistsBinding
import com.example.spotifyanalyzer.viewmodel.SharedViewModel

class ArtistsFragment : Fragment() {

    private lateinit var binding: FragmentArtistsBinding
    private val sharedViewModel by lazy {
        ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
    }
    private lateinit var adapter: ArtistsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentArtistsBinding.inflate(inflater, container, false)

        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        adapter = ArtistsAdapter()
        binding.recyclerView.adapter = adapter

        sharedViewModel.artistData.observe(viewLifecycleOwner, Observer { artistMap ->
            val list = artistMap.values.sortedByDescending { it.plays }
            adapter.submitList(list)
        })

        return binding.root
    }
}

