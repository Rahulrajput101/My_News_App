package com.androiddevs.myapplication.ui.fragments

import android.os.Bundle
import android.text.Editable
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.androiddevs.myapplication.R
import com.androiddevs.myapplication.adapter.MyAdapter
import com.androiddevs.myapplication.databinding.FragmentSearchBinding
import com.androiddevs.myapplication.ui.MainActivity
import com.androiddevs.myapplication.ui.viewModel.NewsViewModel
import com.androiddevs.myapplication.utils.Constants
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

         adapter = MyAdapter(MyAdapter.OnUserClickListener {

            Toast.makeText(context," clciked", Toast.LENGTH_LONG).show()
             findNavController().navigate(SearchFragmentDirections.actionSearchFragmentToArticleFragment(it))
        })
        binding.rvSearchNews.adapter =adapter
        binding.rvSearchNews.layoutManager = LinearLayoutManager(context)
        binding.rvSearchNews.addOnScrollListener(this@SearchFragment.scrollListener)

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
                       val totalPage = it.totalResults/Constants.QUERY_PAGE_COUNT +2
                       isLastPage = viewModel.searchNewsPage == totalPage
                       if(isLastPage){
                           binding.rvSearchNews.setPadding(0,0,0,0)
                       }

                   }

               }
               is Resource.Error -> {
                   showProgressBar()
                   response.message?.let {
                       Log.d(" SearchNews", " an error occured  $it")
                       Toast.makeText(activity, "An error occured: $it", Toast.LENGTH_LONG).show()
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
        isLoading = false
    }
    private fun showProgressBar() {
        binding.paginationProgressBar.visibility = View.VISIBLE
        isLoading =true
    }

    var isLoading = false
    var isLastPage =false
    var isScrolling = false

    val scrollListener = object  : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if(newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
                isScrolling = true
            }
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val visibleCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount

            val isNotLoadingAndNotLastPage = !isLoading && !isLastPage
            val isAtLastItem= firstVisibleItemPosition + visibleCount >= totalItemCount
            val isNotBegining = firstVisibleItemPosition > 0
            val isTotalMoreThanVisible = totalItemCount >= Constants.QUERY_PAGE_COUNT

            val shouldPaginate = isNotLoadingAndNotLastPage && isAtLastItem && isNotBegining && isTotalMoreThanVisible && isScrolling
            if(shouldPaginate){
                viewModel.getSearchNews(binding.etSearch.text.toString())
                isScrolling = false
            }
        }
    }

}