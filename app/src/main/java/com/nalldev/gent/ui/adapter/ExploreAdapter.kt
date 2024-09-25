package com.nalldev.gent.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.nalldev.gent.R
import com.nalldev.gent.databinding.ItemUpcomingCaraouselBinding
import com.nalldev.gent.domain.models.EventModel
import com.nalldev.gent.utils.DateHelper

class ExploreAdapter : ListAdapter<EventModel, ExploreAdapter.ViewHolder>(this) {
    inner class ViewHolder(val binding : ItemUpcomingCaraouselBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(eventModel: EventModel) {
            binding.ivImageEvent.load(eventModel.image) {
                crossfade(true)
                error(R.drawable.event)
            }
            binding.tvDateEvent.text = DateHelper.changeFormat("yyyy-MM-dd HH:mm:ss", "dd'\n'MMM", eventModel.beginTime)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemUpcomingCaraouselBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(holder.adapterPosition))
    }

    companion object : DiffUtil.ItemCallback<EventModel>() {
        override fun areItemsTheSame(oldItem: EventModel, newItem: EventModel): Boolean = oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: EventModel, newItem: EventModel): Boolean = oldItem == newItem
    }
}