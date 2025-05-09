package ac.iiitd.otmt.fragment_event.repository

import ac.iiitd.otmt.fragment_event.network.ApiService
import ac.iiitd.otmt.fragment_event.network.RetrofitClient

class EventRepository(private val apiService: ApiService = RetrofitClient.instance) {

    suspend fun getUpcomingEvents() = apiService.getUpcomingEvents()
    suspend fun getPastEvents() = apiService.getPastEvents()
}