package ac.iiitd.otmt.fragment_technology.ui.tabs

import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.URLSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import ac.iiitd.otmt.R
import ac.iiitd.otmt.fragment_technology.data.model.RelatedLink
import ac.iiitd.otmt.fragment_technology.data.model.Technology
import ac.iiitd.otmt.fragment_technology.ui.viewmodel.TechnologyViewModel

class TabLinksPeopleFragment : Fragment() {

    private val technologyViewModel: TechnologyViewModel by activityViewModels()

    private var innovatorsTitle: TextView? = null
    private var innovatorsText: TextView? = null
    private var patentTitle: TextView? = null
    private var patentText: TextView? = null
    private var relatedLinksTitle: TextView? = null
    private var relatedLinksContainer: LinearLayout? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_tab_links_people, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        innovatorsTitle = view.findViewById(R.id.tv_tab_innovators_title)
        innovatorsText = view.findViewById(R.id.tv_tab_innovators)
        patentTitle = view.findViewById(R.id.tv_tab_patent_title)
        patentText = view.findViewById(R.id.tv_tab_patent)
        relatedLinksTitle = view.findViewById(R.id.tv_tab_related_links_title)
        relatedLinksContainer = view.findViewById(R.id.ll_tab_related_links_container)

        observeViewModel()
    }

    private fun observeViewModel() {
        technologyViewModel.selectedTechnology.observe(viewLifecycleOwner) { technology ->
            technology?.let { populateUi(it) }
        }
    }

    private fun populateUi(tech: Technology) {
        innovatorsText?.apply {
            val hasContent = !tech.innovators.isNullOrEmpty()
            isVisible = hasContent
            innovatorsTitle?.isVisible = hasContent
            if (hasContent) {
                text = tech.innovators?.joinToString("\n") { innovator ->
                    val name = innovator.name ?: "Unknown Innovator"
                    val mail = innovator.mail
                    "• $name ${mail?.let { "($it)" } ?: ""}"
                } ?: ""
            }
        }

        patentText?.apply {
            val hasContent = !tech.patent.isNullOrBlank()
            isVisible = hasContent
            patentTitle?.isVisible = hasContent
            if (hasContent) text = tech.patent
        }

        populateRelatedLinks(tech.relatedLinks)
    }

    private fun populateRelatedLinks(links: List<RelatedLink>?) {
        relatedLinksContainer?.removeAllViews()
        val hasLinks = !links.isNullOrEmpty()
        relatedLinksContainer?.isVisible = hasLinks
        relatedLinksTitle?.isVisible = hasLinks

        if (hasLinks) {
            links?.forEach { link ->
                if (!link.url.isNullOrBlank()) {
                    val linkTextView = TextView(context).apply {
                        layoutParams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                        ).apply { topMargin = 4 }

                        val title = link.title ?: link.url
                        val fullText = "• $title"

                        val spannableString = SpannableString(fullText)
                        try {
                            spannableString.setSpan(
                                URLSpan(link.url),
                                0,
                                fullText.length,
                                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                            )
                            text = spannableString
                            movementMethod = LinkMovementMethod.getInstance()
                            setTextColor(Color.BLUE)
                            textSize = 15f
                        } catch (e: Exception) {
                            text = fullText
                            setTextColor(Color.DKGRAY)
                            textSize = 15f
                        }
                    }
                    relatedLinksContainer?.addView(linkTextView)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        innovatorsTitle = null
        innovatorsText = null
        patentTitle = null
        patentText = null
        relatedLinksTitle = null
        relatedLinksContainer = null
    }

    companion object {
        fun newInstance() = TabLinksPeopleFragment()
    }
}
