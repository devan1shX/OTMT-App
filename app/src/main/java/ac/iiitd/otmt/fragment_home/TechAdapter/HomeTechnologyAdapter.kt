package ac.iiitd.otmt.fragment_home.TechAdapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ac.iiitd.otmt.fragment_technology.data.model.Technology
import ac.iiitd.otmt.databinding.CardHomeTechnologyItemBinding

class HomeTechnologyAdapter(
    private val onItemClicked: (Technology) -> Unit
) : ListAdapter<Technology, HomeTechnologyAdapter.TechnologyViewHolder>(TechnologyDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TechnologyViewHolder {
        val binding = CardHomeTechnologyItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return TechnologyViewHolder(binding, onItemClicked)
    }

    override fun onBindViewHolder(holder: TechnologyViewHolder, position: Int) {
        val technology = getItem(position)
        holder.bind(technology)
    }

    class TechnologyViewHolder(
        private val binding: CardHomeTechnologyItemBinding,
        private val onItemClicked: (Technology) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(technology: Technology) {
            binding.tvTechTitle.text = technology.name
            binding.tvTechOverview.text = technology.overview ?: "No overview available."

            if (technology.trl != null && technology.trl > 0) {
                binding.tvTechTrl.text = "TRL ${technology.trl}"
                binding.tvTechTrl.isVisible = true
            } else {
                binding.tvTechTrl.isVisible = false
            }

            binding.root.setOnClickListener {
                onItemClicked(technology)
            }
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
}