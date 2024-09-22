package com.nalldev.gent.ui.fragment.explore

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nalldev.gent.domain.models.EventModel
import com.nalldev.gent.domain.repositories.HomeRepository
import com.nalldev.gent.utils.UIState
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class ExploreViewModel(private val homeRepository: HomeRepository) : ViewModel() {
    private val _upcomingEvent = MutableLiveData<UIState<List<EventModel>>>()
    val upcomingEvent : LiveData<UIState<List<EventModel>>> = _upcomingEvent

    private val _finishedEvent = MutableLiveData<UIState<List<EventModel>>>()
    val finishedEvent : LiveData<UIState<List<EventModel>>> = _finishedEvent

    private val mutex = Mutex()

    fun fetchEvent() = viewModelScope.launch {
        mutex.withLock {
            _upcomingEvent.postValue(UIState.Loading)
            _finishedEvent.postValue(UIState.Loading)

            try {
                val upcomingEventList = async { homeRepository.fetchEvent(1) }
                val finishedEventList = async { homeRepository.fetchEvent(0) }

                _upcomingEvent.postValue(UIState.Success(upcomingEventList.await()))
                _finishedEvent.postValue(UIState.Success(finishedEventList.await()))
            } catch (e: Exception) {
                _upcomingEvent.postValue(UIState.Error(e.message.toString()))
                _finishedEvent.postValue(UIState.Error(e.message.toString()))
            }
        }
    }

    fun getUpcomingEvent() = viewModelScope.launch {
        _upcomingEvent.postValue(UIState.Loading)
        try {
            val upcomingEventList = homeRepository.getUpcomingEvent()
            _upcomingEvent.postValue(UIState.Success(upcomingEventList))
        } catch (e : Exception) {
            _upcomingEvent.postValue(UIState.Error(e.message.toString()))
        }
    }

    fun getFinishedEvent() = viewModelScope.launch{
        _finishedEvent.postValue(UIState.Loading)
        try {
            val finishedEventList = homeRepository.getFinishedEvent()
            _finishedEvent.postValue(UIState.Success(finishedEventList))
        } catch (e : Exception) {
            _finishedEvent.postValue(UIState.Error(e.message.toString()))
        }
    }
}