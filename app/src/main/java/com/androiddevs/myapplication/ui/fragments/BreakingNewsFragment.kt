package com.androiddevs.myapplication.ui.fragments


import android.opengl.Visibility
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.androiddevs.myapplication.R
import com.androiddevs.myapplication.adapter.MyAdapter
import com.androiddevs.myapplication.databinding.FragmentBreakingNewsBinding
import com.androiddevs.myapplication.ui.MainActivity
import com.androiddevs.myapplication.ui.viewModel.NewsViewModel
import com.androiddevs.myapplication.utils.Constants
import com.androiddevs.myapplication.utils.Resource
import retrofit2.Response


class BreakingNewsFragment : Fragment() {

    private lateinit var binding: FragmentBreakingNewsBinding
    lateinit var viewModel: NewsViewModel
    private lateinit var adapter: MyAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle? 
    ): View {
        // Inflate the layout for this fragment
        binding =FragmentBreakingNewsBinding.inflate(inflater)

        viewModel = (activity as MainActivity).viewModel

        adapter = MyAdapter(MyAdapter.OnUserClickListener{ article->
            Toast.makeText(context," clicked",Toast.LENGTH_LONG).show()
//            val bundle = Bundle().apply {
//                putSerializable(" article", article)
//            }
            findNavController().navigate(BreakingNewsFragmentDirections.actionBreakingNewsFragmentToArticleFragment(article))
        })



        binding.rvBreakingNews.adapter =adapter
        binding.rvBreakingNews.layoutManager =LinearLayoutManager(context)
        binding.rvBreakingNews.addOnScrollListener(this@BreakingNewsFragment.scrollListener)
        viewModel.breakingNews.observe(viewLifecycleOwner, Observer {response->
            when(response){
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let {
                        adapter.differ.submitList(it.articles.toList())
                        val totalPage = it.totalResults / Constants.QUERY_PAGE_COUNT +2
                        isLastPage = viewModel.breakingNewsPage == totalPage
                        if(isLastPage){
                            binding.rvBreakingNews.setPadding(0,0,0,0)
                        }
                    }
                }
                is Resource.Error -> {
                    response.message?.let {
                        Log.d(" BreakingNews", " an error occured  $it")
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

//    private fun hideErrorMessage() {
//        binding.itemErrorMessage = View.INVISIBLE
//        isError = false
//    }
//
//    private fun showErrorMessage(message: String) {
//        binding.itemErrorMessage.visibility = View.VISIBLE
//        binding.itemErrorMessage.tvErrorMessage.error = message
//        isError = true
//    }
    var isError = false
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
                viewModel.getBreakingNews("us")
                isScrolling = false
            }
        }
    }


}