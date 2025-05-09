package ac.iiitd.otmt.fragment_event.fragment_past

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ac.iiitd.otmt.fragment_event.model.ApiEvent
import ac.iiitd.otmt.fragment_event.repository.EventRepository
import kotlinx.coroutines.launch

sealed class UiState_PastEvent {
    object Loading : UiState_PastEvent()
    data class Success(val events: List<ApiEvent>) : UiState_PastEvent()
    data class Error(val message: String) : UiState_PastEvent()
}

class PastEventsViewModel(
    private val repository: EventRepository = EventRepository()
) : ViewModel() {

    private val _uiState = MutableLiveData<UiState_PastEvent>()
    val uiState: LiveData<UiState_PastEvent> = _uiState

    init {
        fetchPastEvents()
    }

    fun fetchPastEvents() {
        _uiState.value = UiState_PastEvent.Loading
        viewModelScope.launch {
            try {
                val response = repository.getPastEvents()
                if (response.isSuccessful && response.body() != null) {
                    val pastEvents = response.body()!!.filter { !it.isActive }
                    _uiState.postValue(UiState_PastEvent.Success(pastEvents))
                } else {
                    _uiState.postValue(UiState_PastEvent.Error("Error: ${response.code()} ${response.message()}"))
                }
            } catch (e: Exception) {
                _uiState.postValue(UiState_PastEvent.Error("Network Error: ${e.message ?: "Unknown error"}"))
            }
        }
    }
}
