package ac.iiitd.otmt.fragment_service.expandableCard

data class ExpandableCardItem(
    val id: Int,
    val iconResId: Int,
    val title: String,
    val subtitle: String,
    val services: List<String>,
    var isExpanded: Boolean = false,
    val learnMoreUrl: String
)