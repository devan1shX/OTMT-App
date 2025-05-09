package ac.iiitd.otmt.fragment_technology.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ac.iiitd.otmt.fragment_technology.data.model.Technology
import ac.iiitd.otmt.databinding.ItemTechnologyCardBinding

typealias OnTechnologyClickListener = (Technology) -> Unit

class TechnologyAdapter(
    private val clickListener: OnTechnologyClickListener
) : ListAdapter<Technology, TechnologyAdapter.TechnologyViewHolder>(TechnologyDiffCallback()) {

    inner class TechnologyViewHolder(private val binding: ItemTechnologyCardBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(technology: Technology, listener: OnTechnologyClickListener) {
            binding.tvTechName.text = technology.name
            binding.tvTechOverview.text = technology.overview ?: "No overview available"

            if (technology.trl != null && technology.trl > 0) {
                binding.tvTechTrl.text = "TRL-${technology.trl}"
                binding.tvTechTrl.visibility = View.VISIBLE
            } else {
                binding.tvTechTrl.visibility = View.GONE
            }

            binding.root.setOnClickListener {
                listener(technology)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TechnologyViewHolder {
        val binding = ItemTechnologyCardBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return TechnologyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TechnologyViewHolder, position: Int) {
        val technology = getItem(position)
        holder.bind(technology, clickListener)
    }
}

class TechnologyDiffCallback : DiffUtil.ItemCallback<Technology>() {
    override fun areItemsTheSame(oldItem: Technology, newItem: Technology): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Technology, newItem: Technology): Boolean {
        return oldItem == newItem
    }
}
