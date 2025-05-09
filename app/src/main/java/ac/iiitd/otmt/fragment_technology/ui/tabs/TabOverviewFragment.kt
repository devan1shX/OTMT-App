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

class TabOverviewFragment : Fragment() {

    private val technologyViewModel: TechnologyViewModel by activityViewModels()

    private var overviewTitle: TextView? = null
    private var overviewText: TextView? = null
    private var descriptionTitle: TextView? = null
    private var descriptionText: TextView? = null
    private var detailedDescriptionTitle: TextView? = null
    private var detailedDescriptionText: TextView? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_tab_overview, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        overviewTitle = view.findViewById(R.id.tv_tab_overview_title)
        overviewText = view.findViewById(R.id.tv_tab_overview)
        descriptionTitle = view.findViewById(R.id.tv_tab_description_title)
        descriptionText = view.findViewById(R.id.tv_tab_description)
        detailedDescriptionTitle = view.findViewById(R.id.tv_tab_detailed_description_title)
        detailedDescriptionText = view.findViewById(R.id.tv_tab_detailed_description)

        observeViewModel()
    }

    private fun observeViewModel() {
        technologyViewModel.selectedTechnology.observe(viewLifecycleOwner) { technology ->
            technology?.let { populateUi(it) }
        }
    }

    private fun populateUi(tech: Technology) {
        overviewText?.apply {
            isVisible = !tech.overview.isNullOrBlank()
            if (isVisible) {
                text = tech.overview
                overviewTitle?.isVisible = true
            } else {
                overviewTitle?.isVisible = false
            }
        }

        descriptionText?.apply {
            isVisible = !tech.description.isNullOrBlank()
            if (isVisible) {
                text = tech.description
                descriptionTitle?.isVisible = true
            } else {
                descriptionTitle?.isVisible = false
            }
        }

        detailedDescriptionText?.apply {
            isVisible = !tech.detailedDescription.isNullOrBlank()
            if (isVisible) {
                text = tech.detailedDescription
                detailedDescriptionTitle?.isVisible = true
            } else {
                detailedDescriptionTitle?.isVisible = false
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        overviewTitle = null
        overviewText = null
        descriptionTitle = null
        descriptionText = null
        detailedDescriptionTitle = null
        detailedDescriptionText = null
    }

    companion object {
        fun newInstance() = TabOverviewFragment()
    }
}
