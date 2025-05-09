package ac.iiitd.otmt.fragment_technology

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ac.iiitd.otmt.R
import ac.iiitd.otmt.fragment_technology.ui.adapter.TechnologyAdapter
import ac.iiitd.otmt.fragment_technology.ui.viewmodel.TechnologyViewModel

class TechnologiesFragment : Fragment() {

    private val technologyViewModel: TechnologyViewModel by activityViewModels()

    private lateinit var technologyAdapter: TechnologyAdapter
    private var recyclerView: RecyclerView? = null
    private var progressBar: ProgressBar? = null
    private var searchEditText: EditText? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_technologies, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        try {
            recyclerView    = view.findViewById(R.id.recycler_view_technologies)
            progressBar     = view.findViewById(R.id.progress_bar_loading)
            searchEditText  = view.findViewById(R.id.et_search_technologies)

            if (recyclerView == null || progressBar == null || searchEditText == null) {
                throw NullPointerException("One or more required views not found in layout.")
            }
        } catch (e: Exception) {
            Toast.makeText(requireContext(), "Layout Error.", Toast.LENGTH_LONG).show()
            return
        }

        setupRecyclerView()
        setupObservers()
        setupSearchListener()

        if (technologyViewModel.isLoading.value != true) {
            technologyViewModel.fetchTechnologies()
        }
    }

    private fun setupRecyclerView() {
        recyclerView?.let { rv ->
            technologyAdapter = TechnologyAdapter { technology ->
                val docketToNavigate = technology.docket
                if (!docketToNavigate.isNullOrBlank()) {
                    navigateToDetailFragment(docketToNavigate)
                } else {
                    Toast.makeText(requireContext(), "Missing identifier.", Toast.LENGTH_SHORT).show()
                }
            }
            rv.layoutManager = LinearLayoutManager(requireContext())
            rv.adapter = technologyAdapter
        }
    }

    private fun navigateToDetailFragment(technologyDocket: String) {
        val detailFragment = TechnologyDetailFragment.newInstance(technologyDocket)
        parentFragmentManager.commit {
            val containerId = R.id.fragment_container
            replace(containerId, detailFragment)
            addToBackStack(null)
            setReorderingAllowed(true)
        }
    }

    private fun setupObservers() {
        technologyViewModel.technologies.observe(viewLifecycleOwner) { list ->
            technologyAdapter.submitList(list)
        }
        technologyViewModel.isLoading.observe(viewLifecycleOwner) { loading ->
            progressBar?.isVisible = loading == true
        }
        technologyViewModel.error.observe(viewLifecycleOwner) { err ->
            err?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
                technologyViewModel.onErrorShown()
            }
        }
    }

    private fun setupSearchListener() {
        searchEditText?.addTextChangedListener { editable ->
            val query = editable?.toString()?.trim() ?: ""
            technologyViewModel.filterTechnologies(query)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        recyclerView?.adapter = null
        recyclerView = null
        progressBar = null
        searchEditText = null
    }
}
