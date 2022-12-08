package com.androiddevs.myapplication.ui.fragments

import android.annotation.SuppressLint
import android.content.ClipData
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.accessibility.AccessibilityEventCompat.setAction
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.androiddevs.myapplication.R
import com.androiddevs.myapplication.adapter.MyAdapter
import com.androiddevs.myapplication.databinding.FragmentSavedNewsBinding
import com.androiddevs.myapplication.databinding.ItemParticleViewBinding
import com.androiddevs.myapplication.ui.MainActivity
import com.androiddevs.myapplication.ui.viewModel.NewsViewModel
import com.google.android.material.snackbar.Snackbar

class SavedNewsFragment : Fragment() {
    private lateinit var binding: FragmentSavedNewsBinding
    lateinit var viewModel: NewsViewModel
    private lateinit var adapter: MyAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSavedNewsBinding.inflate(inflater)
        viewModel = (activity as MainActivity).viewModel



        adapter = MyAdapter(MyAdapter.OnUserClickListener { article ->

            findNavController().navigate(
                SavedNewsFragmentDirections.actionSavedNewsFragmentToArticleFragment(
                    article
                )
            )
        })

        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            @SuppressLint("ShowToast")
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val article = adapter.differ.currentList[position]
                viewModel.deleteArticle(article)

                view?.let {
                    Snackbar.make(it, "Successfully deleted article", Snackbar.LENGTH_LONG).apply {
                        setAction("Undo") {
                            viewModel.saveArticle(article)

                        }.show()
                    }
                }
            }

        }

        ItemTouchHelper(itemTouchHelperCallback).apply {
            attachToRecyclerView(binding.rvSavedNews)
        }



//
//        ItemTouchHelper(itemTouchHelperCallback).apply {
//            attachToRecyclerView(binding.rvSavedNews)
//        }
        binding.rvSavedNews.adapter = adapter
        binding.rvSavedNews.layoutManager = LinearLayoutManager(context)
        viewModel.getSavedNews().observe(viewLifecycleOwner, Observer { article ->

            adapter.differ.submitList(article)


        })


        return binding.root
    }


}