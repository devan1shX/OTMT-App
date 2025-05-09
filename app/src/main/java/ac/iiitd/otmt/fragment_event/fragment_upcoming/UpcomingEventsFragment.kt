package ac.iiitd.otmt.fragment_event.fragment_upcoming

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import ac.iiitd.otmt.databinding.FragmentUpcomingEventsBinding
import ac.iiitd.otmt.fragment_event.adapter.EventAdapter

class UpcomingEventsFragment : Fragment() {

    private var _binding: FragmentUpcomingEventsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: UpcomingEventsViewModel by viewModels()

    private lateinit var eventAdapter: EventAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUpcomingEventsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        observeViewModel()

        viewModel.loadUpcomingEvents()
    }

    private fun setupRecyclerView() {
        eventAdapter = EventAdapter(emptyList())

        binding.rvUpcomingEvents.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = eventAdapter
        }
    }

    private fun observeViewModel() {
        viewModel.uiState.observe(viewLifecycleOwner) { state ->
            binding.progressBar.isVisible = state is UiState.Loading
            binding.rvUpcomingEvents.isVisible = state is UiState.Success && state.events.isNotEmpty()
            binding.tvErrorMessage.isVisible = state is UiState.Error || (state is UiState.Success && state.events.isEmpty())

            when (state) {
                is UiState.Loading -> {
                    binding.tvErrorMessage.visibility = View.GONE
                }
                is UiState.Success -> {
                    if (state.events.isEmpty()) {
                        binding.tvErrorMessage.text = "No upcoming active events found."
                    } else {
                        eventAdapter.updateData(state.events)
                    }
                }
                is UiState.Error -> {
                    binding.tvErrorMessage.text = state.message
                }
                is UiState.Idle -> {
                    binding.progressBar.visibility = View.GONE
                    binding.rvUpcomingEvents.visibility = View.GONE
                    binding.tvErrorMessage.visibility = View.GONE
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.rvUpcomingEvents.adapter = null
        _binding = null
    }
}
