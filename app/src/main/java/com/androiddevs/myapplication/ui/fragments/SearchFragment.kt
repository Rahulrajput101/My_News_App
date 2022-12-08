package com.androiddevs.myapplication.ui.fragments

import android.os.Bundle
import android.text.Editable
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.androiddevs.myapplication.R
import com.androiddevs.myapplication.adapter.MyAdapter
import com.androiddevs.myapplication.databinding.FragmentSearchBinding
import com.androiddevs.myapplication.ui.MainActivity
import com.androiddevs.myapplication.ui.viewModel.NewsViewModel
import com.androiddevs.myapplication.utils.Constants.Companion.SEARCH_DELAY
import com.androiddevs.myapplication.utils.Resource
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Response

class SearchFragment : Fragment() {
    private lateinit var binding : FragmentSearchBinding
    lateinit var viewModel: NewsViewModel
    private lateinit var adapter: MyAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSearchBinding.inflate(inflater)
        viewModel = (activity as MainActivity).viewModel

        val adapter = MyAdapter(MyAdapter.OnUserClickListener {

            Toast.makeText(context," clciked", Toast.LENGTH_LONG).show()
//            val bundle = Bundle().apply {
//                putSerializable("article",it)
//            }
//            findNavController().navigate(R.id.action_breakingNewsFragment_to_articleFragment,bundle)
        })
        binding.rvSearchNews.adapter =adapter
        binding.rvSearchNews.layoutManager = LinearLayoutManager(context)

        var job :Job? =null
        binding.etSearch.addTextChangedListener{ editable ->
            job?.cancel()

            job= MainScope().launch {
                delay(SEARCH_DELAY)
                editable?.let {
                    if (editable.toString().isNotEmpty()){
                        viewModel.getSearchNews(editable.toString())
                }
            }

        }

//        binding.etSearch.let { it->
//            if(it.toString().isNotEmpty()){
//                viewModel.getSearchNews(it.toString())
//            }

        }


        viewModel.searchNews.observe(viewLifecycleOwner, Observer { response ->
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
                       Log.d(" SearchNews", " an error occured  $it")
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