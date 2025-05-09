package ac.iiitd.otmt.fragment_technology.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ac.iiitd.otmt.fragment_technology.data.model.Technology
import ac.iiitd.otmt.fragment_technology.data.network.RetrofitInstance
import kotlinx.coroutines.launch

class TechnologyViewModel : ViewModel() {

    private val _technologies = MutableLiveData<List<Technology>>()
    val technologies: LiveData<List<Technology>> get() = _technologies

    private val _selectedTechnology = MutableLiveData<Technology?>()
    val selectedTechnology: LiveData<Technology?> get() = _selectedTechnology

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> get() = _error

    private val apiService = RetrofitInstance.api

    private var originalTechnologiesList: List<Technology>? = null

    fun fetchTechnologies() {
        if (_isLoading.value == true) {
            return
        }
        if (originalTechnologiesList != null) {
            if (_technologies.value != originalTechnologiesList) {
                _technologies.value = originalTechnologiesList!!
            }
            return
        }

        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            _selectedTechnology.value = null
            try {
                val fetchedList = apiService.getTechnologies()
                originalTechnologiesList = fetchedList
                _technologies.value = fetchedList

                if (fetchedList.isEmpty()) {
                    _error.value = "No technologies found."
                }

            } catch (e: Exception) {
                originalTechnologiesList = null
                _technologies.value = emptyList()
                _error.value = "Failed to fetch technologies: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun findTechnologyByDocket(docket: String?) {
        if (docket.isNullOrBlank()) {
            _selectedTechnology.value = null
            _error.value = "Invalid docket provided."
            return
        }

        if (originalTechnologiesList == null) {
            _selectedTechnology.value = null
            return
        }

        var found: Technology? = null
        try {
            found = originalTechnologiesList?.find {
                it.docket == docket
            }
        } catch (e: Exception) {
            _error.value = "Error searching data."
            _selectedTechnology.value = null
            return
        }

        if (found != null) {
            _selectedTechnology.value = found
            _error.value = null
        } else {
            _error.value = "Technology details not found for Docket $docket."
            _selectedTechnology.value = null
        }
    }

    fun filterTechnologies(query: String) {
        val listToFilter = originalTechnologiesList
        if (listToFilter == null) {
            return
        }
        val trimmedQuery = query.trim()
        if (trimmedQuery.isBlank()) {
            if (_technologies.value != listToFilter) {
                _technologies.value = listToFilter ?: emptyList()
            }
        } else {
            val filteredList = listToFilter.filter { technology ->
                technology.name.contains(trimmedQuery, ignoreCase = true) ||
                        (technology.overview?.contains(trimmedQuery, ignoreCase = true) ?: false)
            }
            _technologies.value = filteredList
        }
    }

    fun onErrorShown() {
        _error.value = null
    }

    fun clearSelectedTechnology() {
        _selectedTechnology.value = null
    }
}
