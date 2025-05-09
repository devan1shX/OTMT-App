package ac.iiitd.otmt.fragment_service.faq

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ac.iiitd.otmt.R

class FaqAdapter(private val faqList: List<FaqItem>) :
    RecyclerView.Adapter<FaqAdapter.FaqViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FaqViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_faq, parent, false)
        return FaqViewHolder(view)
    }

    override fun onBindViewHolder(holder: FaqViewHolder, position: Int) {
        val currentItem = faqList[position]
        holder.bind(currentItem)
    }

    override fun getItemCount() = faqList.size

    inner class FaqViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val questionTextView: TextView = itemView.findViewById(R.id.tv_faq_question)
        private val answerTextView: TextView = itemView.findViewById(R.id.tv_faq_answer)
        private val toggleImageView: ImageView = itemView.findViewById(R.id.iv_faq_toggle)
        private val headerLayout: View = itemView.findViewById(R.id.faq_header)

        fun bind(faqItem: FaqItem) {
            questionTextView.text = faqItem.question
            answerTextView.text = faqItem.answer

            val isExpanded = faqItem.isExpanded
            answerTextView.visibility = if (isExpanded) View.VISIBLE else View.GONE
            toggleImageView.setImageResource(if (isExpanded) R.drawable.ic_remove else R.drawable.ic_plus)

            headerLayout.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    val currentItem = faqList[adapterPosition]
                    currentItem.isExpanded = !currentItem.isExpanded
                    notifyItemChanged(adapterPosition)
                }
            }
        }
    }
}