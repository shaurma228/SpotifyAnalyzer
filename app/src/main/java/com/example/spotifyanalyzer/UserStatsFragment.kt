package com.example.spotifyanalyzer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.Observer
import com.example.spotifyanalyzer.databinding.FragmentUserStatsBinding
import com.example.spotifyanalyzer.viewmodel.SharedViewModel

class UserStatsFragment : Fragment() {

    private lateinit var binding: FragmentUserStatsBinding
    private val sharedViewModel by lazy {
        ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUserStatsBinding.inflate(inflater, container, false)

        sharedViewModel.timeCounter.observe(viewLifecycleOwner, Observer { time ->
            val minutes = time / 60000
            binding.totalTimeTextView.text = "Общее время прослушивания: $minutes мин"
        })

        sharedViewModel.listensCounter.observe(viewLifecycleOwner, Observer { listens ->
            binding.totalListensTextView.text = "Количество прослушиваний: $listens"
        })

        return binding.root
    }
}
