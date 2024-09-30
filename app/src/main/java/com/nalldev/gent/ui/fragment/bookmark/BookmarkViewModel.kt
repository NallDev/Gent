package com.nalldev.gent.ui.fragment.bookmark

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nalldev.gent.domain.models.EventModel
import com.nalldev.gent.domain.repositories.EventRepository
import com.nalldev.gent.utils.AppException
import com.nalldev.gent.utils.SingleLiveEvent
import com.nalldev.gent.utils.UIState
import kotlinx.coroutines.launch

class BookmarkViewModel(
    private val eventRepository: EventRepository
) : ViewModel() {
    private val _bookmarkEvent = MutableLiveData<UIState<List<EventModel>>>()
    val bookmarkEvent: LiveData<UIState<List<EventModel>>> = _bookmarkEvent

    private val _toastEvent = SingleLiveEvent<String>()
    val toastEvent: LiveData<String> = _toastEvent

    fun getBookmarkEvent() = viewModelScope.launch {
        _bookmarkEvent.postValue(UIState.Loading)
        try {
            val bookmarkEventList = eventRepository.getEventBookmark()
            _bookmarkEvent.postValue(UIState.Success(bookmarkEventList))
        } catch (e: AppException) {
            _toastEvent.postValue(e.message)
            UIState.Error(e.message.toString())
        } catch (e: Exception) {
            _toastEvent.postValue(e.message)
            UIState.Error(e.message.toString())
        }
    }

    fun updateEventBookmark(event: EventModel) = viewModelScope.launch {
        try {
            eventRepository.updateEventBookmark(event)
            getBookmarkEvent()
        } catch (e: AppException) {
            _toastEvent.postValue(e.message)
        } catch (e: Exception) {
            _toastEvent.postValue(e.message)
        }
    }
}