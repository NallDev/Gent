package com.nalldev.gent.ui.fragment.upcoming

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nalldev.gent.domain.models.EventModel
import com.nalldev.gent.domain.repositories.HomeRepository
import com.nalldev.gent.utils.SingleLiveEvent
import com.nalldev.gent.utils.UIState
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class UpcomingViewModel(
    private val homeRepository: HomeRepository
) : ViewModel() {
    private val _upcomingEvent = MutableLiveData<UIState<List<EventModel>>>()
    val upcomingEvent: LiveData<UIState<List<EventModel>>> = _upcomingEvent

    private val _toastEvent = SingleLiveEvent<String>()
    val toastEvent: LiveData<String> = _toastEvent

    private var fetchJob: Job? = null

    fun fetchEvent(query : String = "") {
        fetchJob?.cancel()
        fetchJob = viewModelScope.launch {
            _upcomingEvent.postValue(UIState.Loading)

            val upcomingEventResult = async {
                try {
                    val upcomingEventList = homeRepository.fetchEvent(1, query)
                    UIState.Success(upcomingEventList)
                } catch (e: Exception) {
                    _toastEvent.postValue(e.message)
                    UIState.Error(e.message.toString())
                }
            }

            _upcomingEvent.postValue(upcomingEventResult.await())
        }
    }

    fun getUpcomingEvent() = viewModelScope.launch {
        _upcomingEvent.postValue(UIState.Loading)
        try {
            val upcomingEventList = async { homeRepository.getUpcomingEvent() }
            upcomingEventList.await().let { eventList ->
                if (eventList.isEmpty()) {
                    fetchEvent()
                } else {
                    _upcomingEvent.postValue(UIState.Success(eventList))
                }
            }
        } catch (e: Exception) {
            UIState.Error(e.message.toString())
        }
    }

    fun updateEventBookmark(event: EventModel) = viewModelScope.launch {
        homeRepository.updateEventBookmark(event)
        getUpcomingEvent()
    }
}