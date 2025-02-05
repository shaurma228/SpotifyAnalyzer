package com.example.spotifyanalyzer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.spotifyanalyzer.databinding.FragmentAlbumsBinding
import com.example.spotifyanalyzer.viewmodel.SharedViewModel

class AlbumsFragment : Fragment() {

    private lateinit var binding: FragmentAlbumsBinding
    private val sharedViewModel by lazy {
        ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
    }
    private lateinit var adapter: AlbumsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAlbumsBinding.inflate(inflater, container, false)

        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        adapter = AlbumsAdapter()
        binding.recyclerView.adapter = adapter

        sharedViewModel.albumData.observe(viewLifecycleOwner, Observer { albumMap ->
            val list = albumMap.values.sortedByDescending { it.trackPlayed }
            adapter.submitList(list)
        })

        return binding.root
    }
}
