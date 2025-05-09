package ac.iiitd.otmt.fragment_home.EventAdapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ac.iiitd.otmt.fragment_event.model.ApiEvent
import ac.iiitd.otmt.databinding.CardHomeEventItemBinding
import java.text.SimpleDateFormat
import java.util.Locale

class HomeEventAdapter(
    private var eventList: List<ApiEvent>,
    private val onRegisterClick: (url: String) -> Unit
) : RecyclerView.Adapter<HomeEventAdapter.HomeEventViewHolder>() {

    fun updateData(newEvents: List<ApiEvent>) {
        eventList = newEvents
        notifyDataSetChanged()
    }

    private fun abbreviateMonth(month: String): String {
        return try {
            val monthFormat = SimpleDateFormat("MMMM", Locale.ENGLISH)
            val shortMonthFormat = SimpleDateFormat("MMM", Locale.ENGLISH)
            val date = monthFormat.parse(month)
            if (date != null) shortMonthFormat.format(date) else month.take(3)
        } catch (e: Exception) {
            month.take(3)
        }
    }

    inner class HomeEventViewHolder(private val binding: CardHomeEventItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(event: ApiEvent) {
            val abbreviatedMonth = abbreviateMonth(event.month)
            val displayDate = "$abbreviatedMonth ${event.day}"

            binding.tvEventDateHeader.text = displayDate
            binding.tvEventTitleHome.text = event.title
            binding.tvEventLocationHome.text = event.location ?: "Location TBD"
            binding.tvEventTimeHome.text = event.time ?: "Time TBD"

            val urlString = event.registrationUrl

            if (!urlString.isNullOrBlank()) {
                binding.btnEventRegisterHome.visibility = View.VISIBLE
                binding.btnEventRegisterHome.setOnClickListener {
                    onRegisterClick(urlString)
                }
            } else {
                binding.btnEventRegisterHome.visibility = View.INVISIBLE
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeEventViewHolder {
        val binding = CardHomeEventItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return HomeEventViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HomeEventViewHolder, position: Int) {
        holder.bind(eventList[position])
    }

    override fun getItemCount(): Int {
        return eventList.size
    }
}