package com.mielechm.pyrkontechnicaltask.features.guests.guests_list

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mielechm.pyrkontechnicaltask.data.model.entities.GuestEntity
import com.mielechm.pyrkontechnicaltask.data.model.mappers.toGuestEntity
import com.mielechm.pyrkontechnicaltask.data.model.model.GuestItem
import com.mielechm.pyrkontechnicaltask.repositories.LocalDbGuestRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONException
import java.io.IOException
import java.nio.charset.Charset
import javax.inject.Inject


@OptIn(FlowPreview::class)
@HiltViewModel
class GuestsListViewModel @Inject constructor(
    private val localDbGuestRepository: LocalDbGuestRepository,
) : ViewModel() {

    private val _error = MutableStateFlow("")
    val error = _error.asStateFlow()
    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()
    private val _isSearching = MutableStateFlow(false)
    val isSearching = _isSearching.asStateFlow()
    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()
    private val _guestItems = MutableStateFlow<List<GuestItem>>(emptyList())
    val guestItems = searchText.debounce(500L).onEach { _isSearching.update { true } }
        .combine(_guestItems) { text, guests ->
            if (text.isBlank()) {
                guests
            } else {
                guests.filter {
                    it.name.contains(text, ignoreCase = true)
                }
            }
        }.onEach { _isSearching.update { false } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), _guestItems.value)

    fun onSearchTextChange(text: String) {
        _searchText.value = text
    }

    fun getGuestDataFromLocalFile(context: Context, filename: String) {
        viewModelScope.launch {
            _error.value = ""
            _isLoading.value = true
            try {
                val jsonString = withContext(Dispatchers.IO) {
                    readLocalJsonFile(context, filename)
                }
                val itemType = object : TypeToken<List<GuestItem>>() {}.type
                val items = Gson().fromJson<List<GuestItem>>(jsonString, itemType)
                _guestItems.value = items
            } catch (e: IOException) {
                _error.value = "File error: ${e.message}"
            } catch (e: JSONException) {
                _error.value = "Parsing error: ${e.message}"
            }
        }
    }

    fun upsertGuests(guests: List<GuestItem>) {
        viewModelScope.launch {
            guests.forEach { guest ->
                upsertGuestEntity(guest.toGuestEntity())
            }
            _isLoading.value = false
            _error.value = ""
        }
    }

    private fun readLocalJsonFile(context: Context, filename: String): String {
        val inputStream = context.assets.open(filename)
        val size = inputStream.available()
        val buffer = ByteArray(size)
        inputStream.read(buffer)
        inputStream.close()
        return String(buffer, Charset.forName("UTF-8"))
    }

    private fun upsertGuestEntity(guestEntity: GuestEntity) {
        viewModelScope.launch {
            localDbGuestRepository.upsertGuestEntity(guestEntity)
        }
    }
}