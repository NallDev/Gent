package com.nalldev.gent.ui.fragment.explore

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nalldev.gent.domain.models.EventModel
import com.nalldev.gent.domain.repositories.HomeRepository
import com.nalldev.gent.utils.UIState
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class ExploreViewModel(private val homeRepository: HomeRepository) : ViewModel() {
    private val _upcomingEvent = MutableLiveData<UIState<List<EventModel>>>()
    val upcomingEvent: LiveData<UIState<List<EventModel>>> = _upcomingEvent

    private val _finishedEvent = MutableLiveData<UIState<List<EventModel>>>()
    val finishedEvent: LiveData<UIState<List<EventModel>>> = _finishedEvent

    private var fetchJob: Job? = null

    fun fetchEvent() {
        if (fetchJob?.isActive == true) return

        fetchJob = viewModelScope.launch {
            _upcomingEvent.postValue(UIState.Loading)
            _finishedEvent.postValue(UIState.Loading)

            val upcomingEventResult = async {
                try {
                    val upcomingEventList = homeRepository.fetchEvent(1)
                    UIState.Success(upcomingEventList)
                } catch (e: Exception) {
                    UIState.Error(e.message.toString())
                }
            }

            val finishedEventResult = async {
                try {
                    val finishedEventList = homeRepository.fetchEvent(0)
                    UIState.Success(finishedEventList)
                } catch (e: Exception) {
                    UIState.Error(e.message.toString())
                }
            }

            _upcomingEvent.postValue(upcomingEventResult.await())
            _finishedEvent.postValue(finishedEventResult.await())
        }
    }

    fun getUpcomingEvent() = viewModelScope.launch {
        _upcomingEvent.postValue(UIState.Loading)
        val upcomingEventResult = async {
            try {
                val upcomingEventList = homeRepository.getUpcomingEvent()
                UIState.Success(upcomingEventList)
            } catch (e: Exception) {
                UIState.Error(e.message.toString())
            }
        }

        _upcomingEvent.postValue(upcomingEventResult.await())
    }

    fun getFinishedEvent() = viewModelScope.launch {
        _finishedEvent.postValue(UIState.Loading)
        val finishedEvent = async {
            try {
                val finishedEventList = homeRepository.getFinishedEvent()
                UIState.Success(finishedEventList)
            } catch (e: Exception) {
                UIState.Error(e.message.toString())
            }
        }
        _finishedEvent.postValue(finishedEvent.await())
    }
}