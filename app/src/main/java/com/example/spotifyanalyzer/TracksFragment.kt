package com.example.spotifyanalyzer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.spotifyanalyzer.databinding.FragmentTracksBinding
import com.example.spotifyanalyzer.viewmodel.SharedViewModel

class TracksFragment : Fragment() {

    private lateinit var binding: FragmentTracksBinding
    private val sharedViewModel by lazy {
        ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
    }
    private lateinit var adapter: TracksAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTracksBinding.inflate(inflater, container, false)

        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        adapter = TracksAdapter()
        binding.recyclerView.adapter = adapter

        sharedViewModel.trackData.observe(viewLifecycleOwner, Observer { trackMap ->
            val list = trackMap.values.sortedByDescending { it.plays }
            adapter.submitList(list)
        })

        return binding.root
    }
}
