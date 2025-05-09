package ac.iiitd.otmt.fragment_home

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.view.isVisible
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import ac.iiitd.otmt.databinding.FragmentHomeBinding
import ac.iiitd.otmt.fragment_event.fragment_upcoming.UiState as EventUiState
import ac.iiitd.otmt.fragment_event.fragment_upcoming.UpcomingEventsViewModel
import ac.iiitd.otmt.fragment_technology.ui.viewmodel.TechnologyViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import ac.iiitd.otmt.fragment_event.EventFragment
import ac.iiitd.otmt.fragment_technology.TechnologiesFragment
import ac.iiitd.otmt.fragment_technology.TechnologyDetailFragment
import ac.iiitd.otmt.MainActivity
import ac.iiitd.otmt.fragment_about.AboutFragment
import ac.iiitd.otmt.R
import ac.iiitd.otmt.fragment_home.EventAdapter.HomeEventAdapter
import ac.iiitd.otmt.fragment_home.TechAdapter.HomeTechnologyAdapter
import java.util.Locale


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val upcomingEventsViewModel: UpcomingEventsViewModel by viewModels()
    private val technologyViewModel: TechnologyViewModel by activityViewModels()

    private lateinit var homeEventAdapter: HomeEventAdapter
    private lateinit var homeTechnologyAdapter: HomeTechnologyAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupButtonClickListeners()
        setupSearch()
        setupUpcomingEventsRecyclerView()
        setupTechnologiesRecyclerView()

        observeUpcomingEventsViewModel()
        observeTechnologyViewModel()

        upcomingEventsViewModel.loadUpcomingEvents()
        if (technologyViewModel.isLoading.value != true) {
            technologyViewModel.fetchTechnologies()
        } else {
        }
    }

    private fun openRegistrationUrl(urlString: String) {
        val context = requireContext()
        try {
            val finalUrlString = if (urlString.startsWith("http://") || urlString.startsWith("https://")) {
                urlString
            } else {
                "https://$urlString"
            }

            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(finalUrlString))

            if (intent.resolveActivity(context.packageManager) != null) {
                context.startActivity(intent)
            } else {
                Toast.makeText(context, "No app found to open this link.", Toast.LENGTH_LONG).show()
            }
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(context, "No app found to open this link.", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Toast.makeText(context, "Could not open link. Error: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
        }
    }

    private fun setupButtonClickListeners() {
        binding.getStartedButton.setOnClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://docs.google.com/forms/d/e/1FAIpQLSfKpIvwOYj2-a5-ocXptLMyd1tPHH0ABw5z6txjsFhXMEtu-g/viewform"))
            startActivity(browserIntent)
        }
        binding.services.setOnClickListener {
            navigateToServicesFragment()
        }
        binding.innovation.setOnClickListener {
            navigateToTechnologiesFragment()
        }
        binding.collaborate.setOnClickListener {
            navigateToAboutFragment()
        }

        binding.btnScheduleMeeting.setOnClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://calendar.google.com/calendar/u/0/r/eventedit?add=alok@iiitd.ac.in&cls=0&hl=en&pli=1"))
            startActivity(browserIntent)
        }

        binding.events.setOnClickListener { navigateToEventsFragment() }
        binding.btnViewAllEvents.setOnClickListener { navigateToEventsFragment() }
        binding.btnViewAllTechnologies.setOnClickListener { navigateToTechnologiesFragment() }
    }

    private fun navigateToFragment(
        fragment: Fragment,
        containerIdRes: Int,
        bottomNavItemIdRes: Int? = null
    ) {
        if (context == null || activity == null) {
            return
        }
        val mainActivity = activity as? MainActivity
        if (mainActivity == null) {
            Toast.makeText(context, "Navigation Error", Toast.LENGTH_SHORT).show()
            return
        }
        if (containerIdRes == 0 || containerIdRes == View.NO_ID || mainActivity.findViewById<View>(containerIdRes) == null) {
            Toast.makeText(context, "Navigation setup error", Toast.LENGTH_SHORT).show()
            return
        }

        parentFragmentManager.commit {
            replace(containerIdRes, fragment)
            addToBackStack(fragment::class.java.simpleName)
            setReorderingAllowed(true)
        }

        bottomNavItemIdRes?.let { navItemId ->
            if (navItemId != 0 && navItemId != View.NO_ID) {
                try {
                    val bottomNavView = mainActivity.findViewById<BottomNavigationView>(R.id.bottom_nav_view)
                    if (bottomNavView == null) {
                        return@let
                    }
                    if (bottomNavView.menu.findItem(navItemId) == null) {
                        return@let
                    }
                    if (bottomNavView.selectedItemId != navItemId) {
                        bottomNavView.selectedItemId = navItemId
                    } else {
                    }
                } catch (e: Exception) {
                }
            } else {
            }
        }
    }

    private fun navigateToEventsFragment() {
        navigateToFragment(
            EventFragment(),
            R.id.fragment_container,
            R.id.navigation_events
        )
    }

    private fun navigateToServicesFragment() {
        navigateToFragment(
            ac.iiitd.otmt.fragment_service.ServiceFragment(),
            R.id.fragment_container,
            R.id.navigation_services
        )
    }

    private fun navigateToAboutFragment() {
        navigateToFragment(
            AboutFragment(),
            R.id.fragment_container
        )
    }

    private fun navigateToTechnologiesFragment() {
        navigateToFragment(
            TechnologiesFragment(),
            R.id.fragment_container,
            R.id.navigation_technologies
        )
    }

    private fun navigateToTechnologyDetailFragment(technologyDocket: String) {
        if (context == null) return
        if (technologyDocket.isBlank()) {
            Toast.makeText(context,"Cannot open details.", Toast.LENGTH_SHORT).show()
            return
        }
        val detailFragment = TechnologyDetailFragment.newInstance(technologyDocket)
        navigateToFragment(
            detailFragment,
            R.id.fragment_container,
            R.id.navigation_technologies
        )
    }


    private fun setupSearch() {
        binding.searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val query = s.toString().trim().lowercase(Locale.getDefault())
                handleSearchScroll(query)
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun handleSearchScroll(query: String) {
        if (_binding == null) return

        val nestedScrollView = binding.root as? NestedScrollView ?: return

        if (query.isBlank()) {
            return
        }

        when {
            query.contains("tech") || query.contains("technolog") || query.contains("spotlight") -> {
                scrollToView(nestedScrollView, binding.tvSpotlightTechnologiesTitle)
            }
            query.contains("event") || query.contains("upcoming") -> {
                scrollToView(nestedScrollView, binding.tvUpcomingEventsTitle)
            }
            query.contains("service") || query.contains("our service") -> {
                scrollToView(nestedScrollView, binding.tvOurServicesTitle)
            }
            query.contains("meeting") || query.contains("schedule") || query.contains("contact") ||
                    query.contains("innovate") || query.contains("ready") || query.contains("cta") -> {
                scrollToView(nestedScrollView, binding.clCtaSection)
            }
            query.contains("transform") || query.contains("idea") || query.contains("hero") -> {
                scrollToView(nestedScrollView, binding.heroSectionLayout)
            }
            query.contains("search") -> {
                scrollToView(nestedScrollView, binding.searchEditText)
            }
        }
    }

    private fun scrollToView(scrollView: NestedScrollView, viewToScrollTo: View) {
        if (_binding == null) return
        val yPosition = viewToScrollTo.top
        scrollView.post {
            if (_binding != null) {
                scrollView.smoothScrollTo(0, yPosition)
            }
        }
    }

    private fun View.idToString(): String {
        return if (id != View.NO_ID) {
            try {
                resources.getResourceEntryName(id)
            } catch (e: Exception) {
                id.toString()
            }
        } else {
            "no_id"
        }
    }


    private fun setupUpcomingEventsRecyclerView() {
        homeEventAdapter = HomeEventAdapter(emptyList()) { url ->
            openRegistrationUrl(url)
        }
        binding.rvUpcomingEventsHome.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = homeEventAdapter
            setHasFixedSize(true)
        }
    }

    private fun setupTechnologiesRecyclerView() {
        homeTechnologyAdapter = HomeTechnologyAdapter { technology ->
            navigateToTechnologyDetailFragment(technology.docket ?: "")
        }
        binding.rvSpotlightTechnologiesHome.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = homeTechnologyAdapter
            setHasFixedSize(true)
        }
    }


    private fun observeUpcomingEventsViewModel() {
        upcomingEventsViewModel.uiState.observe(viewLifecycleOwner) { state ->
            if (_binding == null) return@observe

            binding.pbEventsLoading.isVisible = state is EventUiState.Loading
            binding.rvUpcomingEventsHome.isVisible = state is EventUiState.Success && state.events.isNotEmpty()
            binding.tvEventsError.isVisible = state is EventUiState.Error || (state is EventUiState.Success && state.events.isEmpty())

            val shouldShowHeader = state !is EventUiState.Idle
            binding.tvUpcomingEventsTitle.isVisible = shouldShowHeader
            binding.btnViewAllEvents.isVisible = shouldShowHeader && state !is EventUiState.Loading

            when (state) {
                is EventUiState.Loading -> {
                    binding.tvEventsError.text = ""
                }
                is EventUiState.Success -> {
                    val top3Events = state.events.take(3)
                    homeEventAdapter.updateData(top3Events)
                    if (top3Events.isEmpty()) {
                        binding.tvEventsError.text = "No upcoming events."
                        binding.tvEventsError.isVisible = true
                    } else {
                        binding.tvEventsError.text = ""
                        binding.tvEventsError.isVisible = false
                    }
                }
                is EventUiState.Error -> {
                    binding.tvEventsError.text = state.message
                    homeEventAdapter.updateData(emptyList())
                }
                is EventUiState.Idle -> {
                    binding.pbEventsLoading.isVisible = false
                    binding.rvUpcomingEventsHome.isVisible = false
                    binding.tvEventsError.isVisible = false
                    binding.tvUpcomingEventsTitle.isVisible = false
                    binding.btnViewAllEvents.isVisible = false
                }
            }
        }
    }

    private fun observeTechnologyViewModel() {
        technologyViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            if (_binding == null) return@observe
            binding.pbTechnologiesLoading.isVisible = isLoading
            if(isLoading) {
                binding.rvSpotlightTechnologiesHome.isVisible = false
                binding.tvTechnologiesError.isVisible = false
                binding.tvSpotlightTechnologiesTitle.isVisible = true
                binding.btnViewAllTechnologies.isVisible = false
            }
        }

        technologyViewModel.technologies.observe(viewLifecycleOwner) { technologies ->
            if (_binding == null) return@observe
            val isLoading = technologyViewModel.isLoading.value ?: false
            if (!isLoading) {
                val top3Technologies = technologies?.filter { !it.name.isNullOrBlank() }?.take(3) ?: emptyList()
                homeTechnologyAdapter.submitList(top3Technologies)

                val hasTechnologiesToShow = top3Technologies.isNotEmpty()
                binding.rvSpotlightTechnologiesHome.isVisible = hasTechnologiesToShow
                binding.tvSpotlightTechnologiesTitle.isVisible = hasTechnologiesToShow
                binding.btnViewAllTechnologies.isVisible = hasTechnologiesToShow

                if (!hasTechnologiesToShow && technologyViewModel.error.value == null) {
                    binding.tvTechnologiesError.text = "No spotlight technologies available."
                    binding.tvTechnologiesError.isVisible = true
                    binding.tvSpotlightTechnologiesTitle.isVisible = true
                    binding.btnViewAllTechnologies.isVisible = true
                } else if (technologyViewModel.error.value == null) {
                    binding.tvTechnologiesError.isVisible = false
                }
            }
        }

        technologyViewModel.error.observe(viewLifecycleOwner) { errorMsg ->
            if (_binding == null) return@observe
            val isLoading = technologyViewModel.isLoading.value ?: false
            if (!isLoading && errorMsg != null) {
                binding.tvTechnologiesError.text = "Failed to load technologies."
                binding.tvTechnologiesError.isVisible = true
                binding.rvSpotlightTechnologiesHome.isVisible = false
                binding.tvSpotlightTechnologiesTitle.isVisible = true
                binding.btnViewAllTechnologies.isVisible = true
                homeTechnologyAdapter.submitList(emptyList())
            } else if (!isLoading && errorMsg == null && homeTechnologyAdapter.itemCount == 0) {
                binding.tvTechnologiesError.text = "No spotlight technologies available."
                binding.tvTechnologiesError.isVisible = true
                binding.rvSpotlightTechnologiesHome.isVisible = false
                binding.tvSpotlightTechnologiesTitle.isVisible = true
                binding.btnViewAllTechnologies.isVisible = true
            } else if (!isLoading && errorMsg == null) {
                binding.tvTechnologiesError.isVisible = false
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        binding.rvUpcomingEventsHome.adapter = null
        binding.rvSpotlightTechnologiesHome.adapter = null
        _binding = null
    }
}