package ac.iiitd.otmt.fragment_event.adapter

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ac.iiitd.otmt.databinding.CardEventItemBinding
import android.widget.Toast
import ac.iiitd.otmt.fragment_event.model.ApiEvent

class EventAdapter(private var eventList: List<ApiEvent>) :
    RecyclerView.Adapter<EventAdapter.EventViewHolder>() {

    fun updateData(newEvents: List<ApiEvent>) {
        eventList = newEvents
        notifyDataSetChanged()
    }

    inner class EventViewHolder(private val binding: CardEventItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(event: ApiEvent) {
            binding.tvEventTitle.text = event.title
            binding.tvEventDate.text = event.getFormattedDate()
            binding.tvEventTime.text = event.time ?: "Time TBD"
            binding.tvEventLocation.text = event.location ?: "Location TBD"
            binding.tvEventDescription.text = event.description ?: ""

            binding.tvEventFeatured.visibility = View.GONE

            if (!event.registrationUrl.isNullOrBlank()) {
                binding.btnEventRegister.visibility = View.VISIBLE
                binding.btnEventRegister.setOnClickListener {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(event.registrationUrl))
                    try {
                        itemView.context.startActivity(intent)
                    } catch (e: Exception) {
                        Toast.makeText(itemView.context, "Could not open link", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                binding.btnEventRegister.visibility = View.GONE
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val binding = CardEventItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return EventViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        holder.bind(eventList[position])
    }

    override fun getItemCount(): Int {
        return eventList.size
    }
}
