package ac.iiitd.otmt.fragment_technology.ui.tabs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import ac.iiitd.otmt.R
import ac.iiitd.otmt.fragment_technology.data.model.Technology
import ac.iiitd.otmt.fragment_technology.ui.viewmodel.TechnologyViewModel

class TabFeaturesFragment : Fragment() {

    private val technologyViewModel: TechnologyViewModel by activityViewModels()

    private var advantagesTitle: TextView? = null
    private var advantagesText: TextView? = null
    private var applicationsTitle: TextView? = null
    private var applicationsText: TextView? = null
    private var useCasesTitle: TextView? = null
    private var useCasesText: TextView? = null
    private var techSpecsTitle: TextView? = null
    private var techSpecsText: TextView? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_tab_features, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        advantagesTitle = view.findViewById(R.id.tv_tab_advantages_title)
        advantagesText = view.findViewById(R.id.tv_tab_advantages)
        applicationsTitle = view.findViewById(R.id.tv_tab_applications_title)
        applicationsText = view.findViewById(R.id.tv_tab_applications)
        useCasesTitle = view.findViewById(R.id.tv_tab_usecases_title)
        useCasesText = view.findViewById(R.id.tv_tab_usecases)
        techSpecsTitle = view.findViewById(R.id.tv_tab_techspecs_title)
        techSpecsText = view.findViewById(R.id.tv_tab_techspecs)

        observeViewModel()
    }

    private fun observeViewModel() {
        technologyViewModel.selectedTechnology.observe(viewLifecycleOwner) { technology ->
            technology?.let { populateUi(it) }
        }
    }

    private fun populateUi(tech: Technology) {
        advantagesText?.apply {
            val hasContent = !tech.advantages.isNullOrEmpty()
            isVisible = hasContent
            advantagesTitle?.isVisible = hasContent
            if (hasContent) text = formatList(tech.advantages)
        }

        applicationsText?.apply {
            val hasContent = !tech.applications.isNullOrEmpty()
            isVisible = hasContent
            applicationsTitle?.isVisible = hasContent
            if (hasContent) text = formatList(tech.applications)
        }

        useCasesText?.apply {
            val hasContent = !tech.useCases.isNullOrEmpty()
            isVisible = hasContent
            useCasesTitle?.isVisible = hasContent
            if (hasContent) text = formatList(tech.useCases)
        }

        techSpecsText?.apply {
            val hasContent = !tech.technicalSpecifications.isNullOrBlank()
            isVisible = hasContent
            techSpecsTitle?.isVisible = hasContent
            if (hasContent) text = tech.technicalSpecifications
        }
    }

    private fun formatList(list: List<String>?): String {
        return list?.joinToString("\n") { "• $it" } ?: ""
    }

    override fun onDestroyView() {
        super.onDestroyView()
        advantagesTitle = null
        advantagesText = null
        applicationsTitle = null
        applicationsText = null
        useCasesTitle = null
        useCasesText = null
        techSpecsTitle = null
        techSpecsText = null
    }

    companion object {
        fun newInstance() = TabFeaturesFragment()
    }
}
