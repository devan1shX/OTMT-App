package ac.iiitd.otmt.fragment_service.expandableCard

import android.content.Context
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import ac.iiitd.otmt.R
import ac.iiitd.otmt.databinding.ItemExpandableCardBinding

const val PAYLOAD_EXPAND = "PAYLOAD_EXPAND"

class ExpandableCardAdapter(
    private val context: Context,
    private val items: MutableList<ExpandableCardItem>,
    private val onLearnMoreClicked: (ExpandableCardItem) -> Unit
) : RecyclerView.Adapter<ExpandableCardAdapter.ViewHolder>() {

    private fun dpToPx(dp: Int, context: Context): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp.toFloat(),
            context.resources.displayMetrics
        ).toInt()
    }

    inner class ViewHolder(val binding: ItemExpandableCardBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemExpandableCardBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, payloads: MutableList<Any>) {
        if (payloads.contains(PAYLOAD_EXPAND)) {
            val item = items[position]
            bindExpansionState(holder, item)
        } else {
            super.onBindViewHolder(holder, position, payloads)
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.binding.apply {
            ivIcon.setImageResource(item.iconResId)
            tvCardTitle.text = item.title
            tvCardSubtitle.text = item.subtitle

            bindExpansionState(holder, item)

            llServicesList.removeAllViews()
            item.services.forEach { serviceText ->
                val textView = TextView(context).apply {
                    text = "• $serviceText"
                    layoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    ).apply {
                        val verticalPaddingPx = dpToPx(4, context)
                        setMargins(0, verticalPaddingPx, 0, verticalPaddingPx)
                    }
                    setTextAppearance(com.google.android.material.R.style.TextAppearance_MaterialComponents_Body2)
                    setTextColor(
                        ContextCompat.getColor(
                            context,
                            R.color.collaborate_text_secondary
                        )
                    )
                }
                llServicesList.addView(textView)
            }

            headerLayout.setOnClickListener {
                toggleExpansion(holder.bindingAdapterPosition)
            }

            btnLearnMore.setOnClickListener {
                onLearnMoreClicked(item)
            }
        }
    }

    private fun bindExpansionState(holder: ViewHolder, item: ExpandableCardItem) {
        val isExpanded = item.isExpanded
        holder.binding.contentLayout.isVisible = isExpanded
        rotateExpandIndicator(holder.binding.ivExpandIndicator, isExpanded)
    }

    private fun toggleExpansion(position: Int) {
        if (position == RecyclerView.NO_POSITION) return

        val item = items[position]
        item.isExpanded = !item.isExpanded

        notifyItemChanged(position, PAYLOAD_EXPAND)
    }

    private fun rotateExpandIndicator(imageView: ImageView, isExpanded: Boolean) {
        val targetRotation = if (isExpanded) 180f else 0f
        imageView.animate().rotation(targetRotation).setDuration(200).start()
    }

    override fun getItemCount(): Int = items.size
}