package ac.iiitd.otmt.fragment_event

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ac.iiitd.otmt.R
import ac.iiitd.otmt.fragment_event.adapter.EventAdapter
import ac.iiitd.otmt.fragment_event.fragment_upcoming.UiState
import ac.iiitd.otmt.fragment_event.fragment_upcoming.UpcomingEventsViewModel

class EventFragment : Fragment() {

    private lateinit var rvEvents: RecyclerView
    private lateinit var progressBarEvents: ProgressBar
    private lateinit var tvErrorMessageEvents: TextView

    private val viewModel: UpcomingEventsViewModel by viewModels()
    private lateinit var eventAdapter: EventAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_event, container, false)
        rvEvents = view.findViewById(R.id.rv_events)
        progressBarEvents = view.findViewById(R.id.progress_bar_events)
        tvErrorMessageEvents = view.findViewById(R.id.tv_error_message_events)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        observeViewModel()

        viewModel.loadUpcomingEvents()
    }

    private fun setupRecyclerView() {
        eventAdapter = EventAdapter(emptyList())
        rvEvents.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = eventAdapter
        }
    }

    private fun observeViewModel() {
        viewModel.uiState.observe(viewLifecycleOwner) { state ->
            progressBarEvents.isVisible = state is UiState.Loading
            rvEvents.isVisible = state is UiState.Success && state.events.isNotEmpty()
            tvErrorMessageEvents.isVisible = state is UiState.Error || (state is UiState.Success && state.events.isEmpty())

            when (state) {
                is UiState.Loading -> {
                    tvErrorMessageEvents.visibility = View.GONE
                }
                is UiState.Success -> {
                    if (state.events.isEmpty()) {
                        tvErrorMessageEvents.text = "No upcoming active events found."
                        eventAdapter.updateData(emptyList())
                    } else {
                        eventAdapter.updateData(state.events)
                    }
                }
                is UiState.Error -> {
                    tvErrorMessageEvents.text = state.message
                    eventAdapter.updateData(emptyList())
                }
                is UiState.Idle -> {
                    progressBarEvents.visibility = View.GONE
                    rvEvents.visibility = View.GONE
                    tvErrorMessageEvents.visibility = View.GONE
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        rvEvents.adapter = null
    }
}
