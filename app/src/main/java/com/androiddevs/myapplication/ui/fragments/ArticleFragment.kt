package com.androiddevs.myapplication.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.navigation.fragment.navArgs
import com.androiddevs.myapplication.R
import com.androiddevs.myapplication.databinding.FragmentArticleBinding
import com.androiddevs.myapplication.databinding.FragmentSearchBinding
import com.androiddevs.myapplication.ui.MainActivity
import com.androiddevs.myapplication.ui.viewModel.NewsViewModel
import com.google.android.material.snackbar.Snackbar


class ArticleFragment : Fragment() {


    private lateinit var binding: FragmentArticleBinding
    private val args: ArticleFragmentArgs by navArgs()
    private lateinit var viewModel: NewsViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentArticleBinding.inflate(inflater)
        viewModel = (activity as MainActivity).viewModel

        val article = args.article

        binding.webView.apply {
            webViewClient = WebViewClient()
            loadUrl(article.url)
        }
        binding.fab.setOnClickListener {
            viewModel.saveArticle(article)
            view?.let { it1 -> Snackbar.make(it1," article is saved",Snackbar.LENGTH_LONG).show() }
        }
        return binding.root
    }


}