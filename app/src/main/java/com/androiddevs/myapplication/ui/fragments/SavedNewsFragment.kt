package com.androiddevs.myapplication.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.androiddevs.myapplication.R
import com.androiddevs.myapplication.databinding.FragmentSavedNewsBinding
import com.androiddevs.myapplication.ui.MainActivity
import com.androiddevs.myapplication.ui.viewModel.NewsViewModel

class SavedNewsFragment : Fragment() {
    private lateinit var binding : FragmentSavedNewsBinding
    lateinit var viewModel: NewsViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSavedNewsBinding.inflate(inflater)
        viewModel = (activity as MainActivity).viewModel
        return binding.root
    }


}