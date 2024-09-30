package com.nalldev.gent.ui.activity.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.nalldev.gent.domain.repositories.EventRepository

class SplashViewModel(
    eventRepository: EventRepository
) : ViewModel() {
    val isDarkMode: LiveData<Boolean> = eventRepository.getIsDarkMode().asLiveData()

}