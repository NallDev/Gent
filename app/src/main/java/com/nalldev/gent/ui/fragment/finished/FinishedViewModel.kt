package com.nalldev.gent.ui.fragment.finished

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nalldev.gent.domain.models.EventModel
import com.nalldev.gent.domain.repositories.EventRepository
import com.nalldev.gent.utils.AppException
import com.nalldev.gent.utils.SingleLiveEvent
import com.nalldev.gent.utils.UIState
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class FinishedViewModel(
    private val eventRepository: EventRepository
) : ViewModel() {
    private val _finishedEvent = MutableLiveData<UIState<List<EventModel>>>()
    val finishedEvent: LiveData<UIState<List<EventModel>>> = _finishedEvent

    private val _toastEvent = SingleLiveEvent<String>()
    val toastEvent: LiveData<String> = _toastEvent

    private var fetchJob: Job? = null

    fun fetchEvent(query : String = "") {
        fetchJob?.cancel()
        fetchJob = viewModelScope.launch {
            _finishedEvent.postValue(UIState.Loading)

            val finishedEventResult = async {
                try {
                    val finishedEventList = eventRepository.fetchEvent(0, query)
                    UIState.Success(finishedEventList)
                } catch (e: AppException) {
                    _toastEvent.postValue(e.message)
                    UIState.Error(e.message.toString())
                } catch (e: Exception) {
                    _toastEvent.postValue(e.message)
                    UIState.Error(e.message.toString())
                }
            }

            _finishedEvent.postValue(finishedEventResult.await())
        }
    }

    fun getFinishedEvent() = viewModelScope.launch {
        _finishedEvent.postValue(UIState.Loading)
        try {
            val finishedEventList = async { eventRepository.getFinishedEvent() }
            finishedEventList.await().let { eventList ->
                if (eventList.isEmpty()) {
                    fetchEvent()
                } else {
                    _finishedEvent.postValue(UIState.Success(eventList))
                }
            }
        } catch (e: AppException) {
            _toastEvent.postValue(e.message)
            UIState.Error(e.message.toString())
        } catch (e: Exception) {
            UIState.Error(e.message.toString())
        }
    }

    fun updateEventBookmark(event: EventModel) = viewModelScope.launch {
        try {
            eventRepository.updateEventBookmark(event)
            getFinishedEvent()
        } catch (e: AppException) {
            _toastEvent.postValue(e.message)
        } catch (e: Exception) {
            _toastEvent.postValue("An unexpected error occurred")
        }
    }
}