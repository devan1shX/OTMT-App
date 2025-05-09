package ac.iiitd.otmt.fragment_technology.data.network


import retrofit2.http.GET
import ac.iiitd.otmt.fragment_technology.data.model.Technology

interface ApiService {
    @GET("technologies")
    suspend fun getTechnologies(): List<Technology>
}