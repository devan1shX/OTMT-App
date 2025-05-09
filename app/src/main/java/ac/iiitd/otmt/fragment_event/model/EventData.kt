package ac.iiitd.otmt.fragment_event.model

import com.google.gson.annotations.SerializedName

data class ApiEvent(
    @SerializedName("title")
    val title: String,

    @SerializedName("month")
    val month: String,

    @SerializedName("day")
    val day: String,

    @SerializedName("location")
    val location: String?,

    @SerializedName("time")
    val time: String?,

    @SerializedName("description")
    val description: String?,

    @SerializedName("registration")
    val registrationUrl: String?,

    @SerializedName("isActive")
    val isActive: Boolean = false
) {
    fun getFormattedDate(): String {
        return "$month $day"
    }
}
