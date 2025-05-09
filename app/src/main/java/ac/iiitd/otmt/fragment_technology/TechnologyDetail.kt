package ac.iiitd.otmt.fragment_technology

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.viewpager2.widget.ViewPager2
import ac.iiitd.otmt.R
import ac.iiitd.otmt.fragment_technology.data.model.Technology
import ac.iiitd.otmt.fragment_technology.ui.adapter.TechnologyDetailsPagerAdapter
import ac.iiitd.otmt.fragment_technology.ui.viewmodel.TechnologyViewModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

private const val ARG_TECHNOLOGY_DOCKET = "TECHNOLOGY_DOCKET"

class TechnologyDetailFragment : Fragment() {

    private val technologyViewModel: TechnologyViewModel by activityViewModels()

    private var backButtonArea: LinearLayout? = null
    private var techNameTextView: TextView? = null
    private var trlTextView: TextView? = null
    private var genreTextView: TextView? = null
    private var docketTextView: TextView? = null
    private var spotlightTextView: TextView? = null

    private var tabLayout: TabLayout? = null
    private var viewPager: ViewPager2? = null
    private var pagerAdapter: TechnologyDetailsPagerAdapter? = null

    private var technologyDocket: String? = null
    private var detailsLoadRequested: Boolean = false

    companion object {
        fun newInstance(techDocket: String): TechnologyDetailFragment {
            val fragment = TechnologyDetailFragment()
            val args = Bundle().apply {
                putString(ARG_TECHNOLOGY_DOCKET, techDocket)
            }
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            technologyDocket = it.getString(ARG_TECHNOLOGY_DOCKET)
        }
        if (technologyDocket.isNullOrBlank()) {
            Toast.makeText(requireContext(), "Error: Missing identifier.", Toast.LENGTH_SHORT).show()
            parentFragmentManager.popBackStack()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_technology_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeViews(view)
        setupBackButton()
        setupTabs()
        observeViewModel()

        val selectedTech = technologyViewModel.selectedTechnology.value
        if (selectedTech != null && selectedTech.docket == technologyDocket) {
            populateBaseUi(selectedTech)
            detailsLoadRequested = true
        } else if (!detailsLoadRequested && !technologyDocket.isNullOrBlank()){
            technologyViewModel.findTechnologyByDocket(technologyDocket)
            detailsLoadRequested = true
        }
    }

    private fun initializeViews(view: View) {
        try {
            backButtonArea = view.findViewById(R.id.ll_back_button_area)
            techNameTextView = view.findViewById(R.id.tv_detail_tech_name)
            trlTextView = view.findViewById(R.id.tv_detail_trl)
            genreTextView = view.findViewById(R.id.tv_detail_genre)
            docketTextView = view.findViewById(R.id.tv_detail_docket)

            tabLayout = view.findViewById(R.id.tab_layout)
            viewPager = view.findViewById(R.id.view_pager)

            if (backButtonArea == null || techNameTextView == null || tabLayout == null || viewPager == null) {
                throw NullPointerException("Essential detail views (incl. tabs/pager) not found.")
            }

        } catch (e: Exception) {
            Toast.makeText(requireContext(), "Layout Error.", Toast.LENGTH_LONG).show()
            parentFragmentManager.popBackStack()
        }
    }

    private fun setupBackButton() {
        backButtonArea?.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }

    private fun setupTabs() {
        pagerAdapter = TechnologyDetailsPagerAdapter(this)
        viewPager?.adapter = pagerAdapter

        if (tabLayout != null && viewPager != null) {
            TabLayoutMediator(tabLayout!!, viewPager!!) { tab, position ->
                tab.text = getTabTitle(position)
            }.attach()
        }
    }

    private fun getTabTitle(position: Int): String {
        return when (position) {
            0 -> "Overview"
            1 -> "Features"
            2 -> "Links & People"
            3 -> "Media"
            else -> "Unknown"
        }
    }

    private fun observeViewModel() {
        technologyViewModel.selectedTechnology.observe(viewLifecycleOwner) { technology ->
            if (technology != null && technology.docket == technologyDocket) {
                populateBaseUi(technology)
                detailsLoadRequested = true
            } else if (technology != null) {
                // Mismatch, do nothing or log warning if needed for debugging other issues
            } else {
                // Null selected technology
            }
        }

        technologyViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            // Handle loading state if a ProgressBar is added
        }

        technologyViewModel.error.observe(viewLifecycleOwner) { errorMessage ->
            errorMessage?.let {
                Toast.makeText(requireContext(), "Error: $it", Toast.LENGTH_LONG).show()
                technologyViewModel.onErrorShown()
            }
        }
    }

    private fun populateBaseUi(tech: Technology) {
        try {
            techNameTextView?.text = tech.name ?: "Unnamed Technology"

            spotlightTextView?.isVisible = tech.spotlight == true
            genreTextView?.apply {
                isVisible = !tech.genre.isNullOrBlank()
                if (isVisible) text = tech.genre
            }
            trlTextView?.apply {
                isVisible = tech.trl != null && tech.trl > 0
                if (isVisible) text = "TRL-${tech.trl}"
            }
            docketTextView?.apply {
                isVisible = !tech.docket.isNullOrBlank()
                if (isVisible) text = "Docket: ${tech.docket}"
            }

        } catch (e: Exception) {
            Toast.makeText(requireContext(), "Error displaying base details", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        backButtonArea = null
        techNameTextView = null
        trlTextView = null
        genreTextView = null
        docketTextView = null
        spotlightTextView = null

        viewPager?.adapter = null
        pagerAdapter = null

        tabLayout = null
        viewPager = null
    }
}
