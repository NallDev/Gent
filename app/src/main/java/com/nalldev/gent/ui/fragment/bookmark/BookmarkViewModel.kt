package com.nalldev.gent.ui.fragment.bookmark

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nalldev.gent.domain.models.EventModel
import com.nalldev.gent.domain.repositories.HomeRepository
import com.nalldev.gent.utils.SingleLiveEvent
import com.nalldev.gent.utils.UIState
import kotlinx.coroutines.launch

class BookmarkViewModel(
    private val homeRepository: HomeRepository
) : ViewModel() {
    private val _bookmarkEvent = MutableLiveData<UIState<List<EventModel>>>()
    val bookmarkEvent: LiveData<UIState<List<EventModel>>> = _bookmarkEvent

    private val _toastEvent = SingleLiveEvent<String>()
    val toastEvent: LiveData<String> = _toastEvent

    fun getBookmarkEvent() = viewModelScope.launch {
        _bookmarkEvent.postValue(UIState.Loading)
        try {
            val bookmarkEventList = homeRepository.getEventBookmark()
            _bookmarkEvent.postValue(UIState.Success(bookmarkEventList))
        } catch (e: Exception) {
            UIState.Error(e.message.toString())
        }
    }

    fun updateEventBookmark(event: EventModel) = viewModelScope.launch {
        homeRepository.updateEventBookmark(event)
        getBookmarkEvent()
    }
}