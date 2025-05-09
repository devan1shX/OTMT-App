package ac.iiitd.otmt.fragment_event.network

import ac.iiitd.otmt.fragment_event.model.ApiEvent
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {

    @GET("events")
    suspend fun getUpcomingEvents(): Response<List<ApiEvent>>

    @GET("events")
    suspend fun  getPastEvents(): Response<List<ApiEvent>>
}