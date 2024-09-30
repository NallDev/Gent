package com.nalldev.gent.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.nalldev.gent.R
import com.nalldev.gent.databinding.ItemEventBinding
import com.nalldev.gent.domain.models.EventModel
import com.nalldev.gent.utils.DateHelper

class EventAdapter(val eventListener: EventListener? = null) : ListAdapter<EventModel, EventAdapter.ViewHolder>(this) {
    private var hideBookmark = false

    inner class ViewHolder(private val binding : ItemEventBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(eventData : EventModel) {
            binding.ivImageEvent.load(eventData.image) {
                crossfade(true)
                error(R.drawable.event)
            }
            binding.tvNameEvent.text = eventData.name
            binding.tvPlaceEvent.text = eventData.cityName
            binding.tvSummaryEvent.text = eventData.summary
            binding.tvDateEvent.text = DateHelper.changeFormat("yyyy-MM-dd HH:mm:ss", "dd'\n'MMM", eventData.beginTime)

            binding.bookmarkToggleButton.isGone = hideBookmark
            binding.bookmarkToggleButton.isChecked = eventData.isBookmark
            changeBookmark(eventData)
        }

        fun changeBookmark(eventData: EventModel) {
            binding.bookmarkToggleButton.isChecked = eventData.isBookmark
            initListener(eventData)
        }

        private fun initListener(eventData: EventModel) {
            binding.bookmarkToggleButton.setOnClickListener {
                eventListener?.onBookmarkClicked(eventData)
            }

            binding.root.setOnClickListener {
                eventListener?.onItemClicked(eventData)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemEventBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(holder.adapterPosition))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, payloads: MutableList<Any>) {
        when (val payload = payloads.lastOrNull()) {
            is List<*> -> {
                payload.forEach { item ->
                    when (item) {
                        is ItemEventChangePayload.ChangeBookmark -> holder.changeBookmark(item.eventData)
                    }
                }
            }
            else -> onBindViewHolder(holder, holder.adapterPosition)
        }
    }

    fun hideBookmark() {
        hideBookmark = true
    }

    companion object : DiffUtil.ItemCallback<EventModel>() {
        override fun areItemsTheSame(oldItem: EventModel, newItem: EventModel): Boolean = oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: EventModel, newItem: EventModel): Boolean = oldItem == newItem

        override fun getChangePayload(oldItem: EventModel, newItem: EventModel): Any? {
            val diffBundle = mutableListOf<ItemEventChangePayload>()

            if (oldItem.isBookmark != newItem.isBookmark) diffBundle.add(ItemEventChangePayload.ChangeBookmark(newItem))

            return diffBundle.ifEmpty { null }
        }
    }

    private sealed interface ItemEventChangePayload {
        data class ChangeBookmark(val eventData : EventModel) : ItemEventChangePayload
    }

    interface EventListener {
        fun onBookmarkClicked(eventData: EventModel) {}
        fun onItemClicked(eventData: EventModel) {}
    }
}