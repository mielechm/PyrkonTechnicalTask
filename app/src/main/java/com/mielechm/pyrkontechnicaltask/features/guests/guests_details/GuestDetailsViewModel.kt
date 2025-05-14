package com.mielechm.pyrkontechnicaltask.features.guests.guests_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mielechm.pyrkontechnicaltask.data.model.mappers.toGuestItem
import com.mielechm.pyrkontechnicaltask.data.model.model.GuestItem
import com.mielechm.pyrkontechnicaltask.repositories.LocalDbGuestRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GuestDetailsViewModel @Inject constructor(private val localDbGuestRepository: LocalDbGuestRepository) :
    ViewModel() {
    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()
    private val _guest = MutableStateFlow(GuestItem())
    val guest = _guest.asStateFlow()

    fun getGuestDetails(name: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.value = true
            _guest.value = localDbGuestRepository.getGuestEntityByName(name).toGuestItem()

            _isLoading.value = false
        }
    }
}