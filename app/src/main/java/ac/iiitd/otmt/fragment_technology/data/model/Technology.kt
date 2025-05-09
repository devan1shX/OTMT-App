package ac.iiitd.otmt.fragment_technology.data.model

import com.google.gson.annotations.SerializedName

data class Technology(
    @SerializedName("id")
    val id: String,

    @SerializedName("name")
    val name: String,

    @SerializedName("overview")
    val overview: String?,

    @SerializedName("trl")
    val trl: Int?,

    @SerializedName("description")
    val description: String?,

    @SerializedName("detailedDescription")
    val detailedDescription: String?,

    @SerializedName("genre")
    val genre: String?,

    @SerializedName("docket")
    val docket: String?,

    @SerializedName("innovators")
    val innovators: List<Innovator>?,

    @SerializedName("advantages")
    val advantages: List<String>?,

    @SerializedName("applications")
    val applications: List<String>?,

    @SerializedName("useCases")
    val useCases: List<String>?,

    @SerializedName("relatedLinks")
    val relatedLinks: List<RelatedLink>?,

    @SerializedName("technicalSpecifications")
    val technicalSpecifications: String?,

    @SerializedName("spotlight")
    val spotlight: Boolean?,

    @SerializedName("images")
    val images: List<ImageInfo>?,

    @SerializedName("patent")
    val patent: String?
)

data class Innovator(
    @SerializedName("name")
    val name: String?,
    @SerializedName("mail")
    val mail: String?
)

data class RelatedLink(
    @SerializedName("title")
    val title: String?,
    @SerializedName("url")
    val url: String?
)

data class ImageInfo(
    @SerializedName("url")
    val url: String?,
    @SerializedName("caption")
    val caption: String?
)
