package ac.iiitd.otmt.fragment_event.fragment_upcoming

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ac.iiitd.otmt.fragment_event.model.ApiEvent
import ac.iiitd.otmt.fragment_event.repository.EventRepository
import kotlinx.coroutines.launch

sealed class UiState {
    object Loading : UiState()
    data class Success(val events: List<ApiEvent>) : UiState()
    data class Error(val message: String) : UiState()
    object Idle : UiState()
}

class UpcomingEventsViewModel(
    private val repository: EventRepository = EventRepository()
) : ViewModel() {

    private val _uiState = MutableLiveData<UiState>(UiState.Idle)
    val uiState: LiveData<UiState> = _uiState

    fun loadUpcomingEvents() {
        if (_uiState.value == UiState.Loading) {
            return
        }

        _uiState.value = UiState.Loading
        viewModelScope.launch {
            try {
                val response = repository.getUpcomingEvents()
                if (response.isSuccessful && response.body() != null) {
                    val activeEvents = response.body()!!.filter { it.isActive }
                    _uiState.postValue(UiState.Success(activeEvents))
                } else {
                    val errorMsg = "Error: ${response.code()} ${response.message()}"
                    _uiState.postValue(UiState.Error(errorMsg))
                }
            } catch (e: Exception) {
                val errorMsg = "Network Error: ${e.message ?: "Unknown error"}"
                _uiState.postValue(UiState.Error(errorMsg))
            }
        }
    }
}
