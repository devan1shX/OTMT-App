package ac.iiitd.otmt.fragment_event.fragment_past

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import ac.iiitd.otmt.R
import ac.iiitd.otmt.fragment_event.adapter.EventAdapter

class PastHighlightsFragment : Fragment() {

    private val viewModel: PastEventsViewModel by viewModels()

    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var errorTextView: TextView
    private lateinit var eventAdapter: EventAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_past_highlights, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.rv_past_events)
        progressBar = view.findViewById(R.id.progressBar)
        errorTextView = view.findViewById(R.id.tv_error_message)

        setupRecyclerView()
        observeViewModel()
    }

    private fun setupRecyclerView() {
        eventAdapter = EventAdapter(emptyList())
        recyclerView.adapter = eventAdapter
    }

    private fun observeViewModel() {
        viewModel.uiState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiState_PastEvent.Loading -> {
                    progressBar.visibility = View.VISIBLE
                    recyclerView.visibility = View.GONE
                    errorTextView.visibility = View.GONE
                }
                is UiState_PastEvent.Success -> {
                    progressBar.visibility = View.GONE
                    if (state.events.isEmpty()) {
                        errorTextView.visibility = View.VISIBLE
                        errorTextView.text = "No upcoming active events found."
                        recyclerView.visibility = View.GONE
                    } else {
                        errorTextView.visibility = View.GONE
                        recyclerView.visibility = View.VISIBLE
                        eventAdapter.updateData(state.events)
                    }
                }
                is UiState_PastEvent.Error -> {
                    progressBar.visibility = View.GONE
                    recyclerView.visibility = View.GONE
                    errorTextView.visibility = View.VISIBLE
                    errorTextView.text = state.message
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        recyclerView.adapter = null
    }
}
