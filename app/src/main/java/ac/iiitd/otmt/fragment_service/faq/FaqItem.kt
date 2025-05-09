package ac.iiitd.otmt.fragment_service.faq

data class FaqItem(
    val question: String,
    val answer: String,
    var isExpanded: Boolean = false
)