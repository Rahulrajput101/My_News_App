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


class ArticleFragment : Fragment() {


   private lateinit var binding: FragmentArticleBinding
    private val args : ArticleFragmentArgs by navArgs()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentArticleBinding.inflate(inflater)

        val article = args.article

        binding.webView.apply {
            webViewClient = WebViewClient()
            loadUrl(article.url)
        }
        return binding.root
    }


}