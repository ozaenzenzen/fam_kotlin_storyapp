package com.example.famstoryappkotlin.ui.views.home

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.famstoryappkotlin.R
import com.example.famstoryappkotlin.data.response.StoryItem
import com.example.famstoryappkotlin.databinding.StoryItemBinding
import com.example.famstoryappkotlin.utils.localDateFormat

class ListStoryAdapter : PagingDataAdapter<StoryItem, ListStoryAdapter.MyViewHolder>(DIFF_CALLBACK) {
    class MyViewHolder(val binding: StoryItemBinding) : RecyclerView.ViewHolder(binding.root) {
        //
    }

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        val binding = StoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val itemStory = getItem(position)
        holder.binding.apply {
            holder.binding.tvStoryDescription.text = "${itemStory?.description}"
            holder.binding.tvStoryUsername.text = "${itemStory?.name}"
            holder.binding.tvStoryDate.text = localDateFormat("${itemStory?.createdAt}")
            Glide
                .with(holder.itemView.context)
                .load(itemStory?.photoUrl.toString())
                .placeholder(R.drawable.image_item2)
                .into(holder.binding.ivStoryImage)
        }

        holder.itemView.setOnClickListener { onItemClickCallback.onItemClicked(itemStory!!) }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<StoryItem>() {
            override fun areItemsTheSame(oldItem: StoryItem, newItem: StoryItem): Boolean {
                return oldItem == newItem
            }

            @SuppressLint("DiffUtilEquals")
            override fun areContentsTheSame(
                oldItem: StoryItem,
                newItem: StoryItem
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(story: StoryItem)
    }

}