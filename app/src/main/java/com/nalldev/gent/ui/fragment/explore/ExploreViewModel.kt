package com.nalldev.gent.ui.fragment.explore

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

class ExploreViewModel(private val homeRepository: HomeRepository) : ViewModel() {
    private val _upcomingEvent = MutableLiveData<UIState<List<EventModel>>>()
    val upcomingEvent: LiveData<UIState<List<EventModel>>> = _upcomingEvent

    private val _finishedEvent = MutableLiveData<UIState<List<EventModel>>>()
    val finishedEvent: LiveData<UIState<List<EventModel>>> = _finishedEvent

    private val _toastEvent = SingleLiveEvent<String>()
    val toastEvent: LiveData<String> = _toastEvent

    private var fetchJob: Job? = null

    fun fetchEvent() {
        if (fetchJob?.isActive == true) return

        fetchJob = viewModelScope.launch {
            _upcomingEvent.postValue(UIState.Loading)
            _finishedEvent.postValue(UIState.Loading)

            val upcomingEventResult = async {
                try {
                    val upcomingEventList = homeRepository.fetchEvent(1).take(5)
                    UIState.Success(upcomingEventList)
                } catch (e: Exception) {
                    _toastEvent.postValue(e.message)
                    UIState.Error(e.message.toString())
                }
            }

            val finishedEventResult = async {
                try {
                    val finishedEventList = homeRepository.fetchEvent(0).take(5)
                    UIState.Success(finishedEventList)
                } catch (e: Exception) {
                    _toastEvent.postValue(e.message)
                    UIState.Error(e.message.toString())
                }
            }

            _upcomingEvent.postValue(upcomingEventResult.await())
            _finishedEvent.postValue(finishedEventResult.await())
        }
    }

    fun getUpcomingEvent() = viewModelScope.launch {
        _upcomingEvent.postValue(UIState.Loading)
        try {
            val upcomingEventList = async { homeRepository.getUpcomingEvent().take(5) }
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

    fun getFinishedEvent() = viewModelScope.launch {
        _finishedEvent.postValue(UIState.Loading)
        try {
            val upcomingEventList = async { homeRepository.getFinishedEvent().take(5) }
            upcomingEventList.await().let { eventList ->
                if (eventList.isEmpty()) {
                    fetchEvent()
                } else {
                    _finishedEvent.postValue(UIState.Success(eventList))
                }
            }
        } catch (e: Exception) {
            UIState.Error(e.message.toString())
        }
    }
}