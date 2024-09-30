package com.nalldev.gent.ui.activity.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nalldev.gent.domain.models.EventModel
import com.nalldev.gent.domain.repositories.EventRepository
import kotlinx.coroutines.launch

class DetailViewModel(
    private val eventRepository: EventRepository
) : ViewModel() {
    fun updateEventBookmark(event: EventModel) = viewModelScope.launch {
        eventRepository.updateEventBookmark(event)
    }
}