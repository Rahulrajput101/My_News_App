package com.androiddevs.myapplication.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.androiddevs.myapplication.databinding.ItemParticleViewBinding
import com.androiddevs.myapplication.model.Article
import com.bumptech.glide.Glide

class MyAdapter(val onUserClickListener: OnUserClickListener) :
    RecyclerView.Adapter<MyAdapter.MyViewHolder>() {


    private val diffUtil = object : DiffUtil.ItemCallback<Article>() {
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem.url == newItem.url
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem == newItem
        }

    }


    val differ = AsyncListDiffer(this, diffUtil)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val article = differ.currentList[position]

        holder.bind(article)
        holder.itemView.setOnClickListener {
            onUserClickListener.onClick(article)
        }
//        holder.itemView.apply {
//            setOnItemClickListener {
//                onItemClickListener?.let { it(article) }
//            }
//        }

    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }


    class MyViewHolder(private val binding: ItemParticleViewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        companion object {

            fun from(parent: ViewGroup): MyViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                val binding = ItemParticleViewBinding.inflate(inflater, parent, false)
                return MyViewHolder(binding)

            }


        }


        fun bind(article: Article) {
            Glide.with(binding.root).load(article.urlToImage).into(binding.ivArticleImage)
            binding.tvSource.text = article.source.name
            binding.tvTitle.text = article.title
            binding.tvDescription.text = article.description
            binding.tvPublishedAt.text = article.publishedAt


        }


    }

    //    private var onItemClickListener : ((Article) -> Unit)? = null
//
//    fun setOnItemClickListener(listener : (Article)->Unit){
//        onItemClickListener = listener
//    }
    class OnUserClickListener(val clickListener: (article: Article) -> Unit) {
        fun onClick(article: Article) = clickListener(article)
    }
}