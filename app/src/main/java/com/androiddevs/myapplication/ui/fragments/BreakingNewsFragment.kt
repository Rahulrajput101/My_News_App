package com.androiddevs.myapplication.ui.fragments

import android.opengl.Visibility
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.androiddevs.myapplication.R
import com.androiddevs.myapplication.adapter.MyAdapter
import com.androiddevs.myapplication.databinding.FragmentBreakingNewsBinding
import com.androiddevs.myapplication.ui.MainActivity
import com.androiddevs.myapplication.ui.viewModel.NewsViewModel
import com.androiddevs.myapplication.utils.Resource
import retrofit2.Response

class BreakingNewsFragment : Fragment() {

    private lateinit var binding: FragmentBreakingNewsBinding
    lateinit var viewModel: NewsViewModel
    private lateinit var adapter: MyAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle? 
    ): View? {
        // Inflate the layout for this fragment
        binding =FragmentBreakingNewsBinding.inflate(inflater)

        viewModel = (activity as MainActivity).viewModel

        adapter = MyAdapter(MyAdapter.OnUserClickListener{
            Toast.makeText(context," clicked",Toast.LENGTH_LONG).show()
            val bundle = Bundle().apply {
                putSerializable(" article", it)
            }
            findNavController().navigate(R.id.action_breakingNewsFragment_to_articleFragment,bundle)

        })






        binding.rvBreakingNews.adapter =adapter
        binding.rvBreakingNews.layoutManager =LinearLayoutManager(context)
        viewModel.breakingNews.observe(viewLifecycleOwner, Observer {response->
            when(response){
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let {
                        adapter.differ.submitList(it.articles)
                    }
                }
                is Resource.Error -> {
                    showProgressBar()
                    response.message?.let {
                        Log.d(" BreakingNews", " an error occured  $it")
                    }
                }
                 is Resource.Loading ->{
                     showProgressBar()
                    }

            }

        })


        return binding.root
    }

    private fun hideProgressBar() {
       binding.paginationProgressBar.visibility = View.INVISIBLE
    }
    private fun showProgressBar() {
        binding.paginationProgressBar.visibility = View.VISIBLE
    }


}