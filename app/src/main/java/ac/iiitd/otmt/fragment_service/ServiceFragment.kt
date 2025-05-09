package ac.iiitd.otmt.fragment_service

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import ac.iiitd.otmt.fragment_service.expandableCard.ExpandableCardAdapter
import ac.iiitd.otmt.fragment_service.expandableCard.ExpandableCardItem
import ac.iiitd.otmt.R
import ac.iiitd.otmt.databinding.FragmentServiceBinding
import ac.iiitd.otmt.fragment_service.faq.FaqAdapter
import ac.iiitd.otmt.fragment_service.faq.FaqItem

class ServiceFragment : Fragment() {

    private var _binding: FragmentServiceBinding? = null
    private val binding get() = _binding!!

    private lateinit var expandableCardAdapter: ExpandableCardAdapter
    private val cardData = mutableListOf<ExpandableCardItem>()

    private lateinit var faqAdapter: FaqAdapter
    private val faqData = mutableListOf<FaqItem>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentServiceBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupServiceRecyclerView()
        setupFaqRecyclerView()

        loadCardData()
        loadFaqData()

        binding.btnScheduleMeeting.setOnClickListener {
            val calendarUrl = "https://calendar.google.com/calendar/u/0/r/eventedit?add=alok@iiitd.ac.in&cls=0&hl=en&pli=1"
            openUrl(calendarUrl)
        }
    }

    private fun openUrl(url: String?) {
        if (url.isNullOrEmpty()) {
            Toast.makeText(requireContext(), "No URL provided for this action.", Toast.LENGTH_SHORT).show()
            return
        }
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(requireContext(), "Cannot open link. No application found.", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Toast.makeText(requireContext(), "Error opening link.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupServiceRecyclerView() {
        expandableCardAdapter = ExpandableCardAdapter(requireContext(), cardData) { clickedItem ->
            openUrl(clickedItem.learnMoreUrl)
        }

        binding.rvExpandableCards.apply {
            adapter = expandableCardAdapter
            layoutManager = LinearLayoutManager(context)
            itemAnimator = DefaultItemAnimator()
            isNestedScrollingEnabled = false
        }
    }

    private fun loadCardData() {
        if (cardData.isNotEmpty()) {
            if (::expandableCardAdapter.isInitialized) {
                expandableCardAdapter.notifyDataSetChanged()
            }
            return
        }
        cardData.clear()

        cardData.add(
            ExpandableCardItem(
                id = 1,
                iconResId = R.drawable.ic_lightbulb,
                title = "Facilitate Innovation and Awareness",
                subtitle = "Building a dynamic ecosystem that turns ideas into impactful innovations.",
                services = listOf("Workshops", "Innovation Events", "Expert Consultations"),
                isExpanded = false,
                learnMoreUrl = "http://otmt.iiitd.edu.in/Services/Facilitate_Innovation"
            )
        )
        cardData.add(
            ExpandableCardItem(
                id = 2,
                iconResId = R.drawable.ic_tech_lisencing,
                title = "Technology Licensing",
                subtitle = "Helping you bring new ideas to market through strategic partnerships.",
                services = listOf("Access Innovation", "Strategic Partnerships", "Intellectual Property Management", "Commercialisation Support", "Entrepreneurial Ecosystem"),
                isExpanded = false,
                learnMoreUrl = "http://otmt.iiitd.edu.in/Services/Tech_Licensing"
            )
        )
        cardData.add(
            ExpandableCardItem(
                id = 3,
                iconResId = R.drawable.ic_ipr,
                title = "IPR Management",
                subtitle = "Legally protecting your inventions so only you control their use.",
                services = listOf("IPR Protection", "IPR Royalty", "Supports Business Growth"),
                isExpanded = false,
                learnMoreUrl = "http://otmt.iiitd.edu.in/Services/IPR_Management"
            )
        )
        cardData.add(
            ExpandableCardItem(
                id = 4,
                iconResId = R.drawable.ic_tech_maturity_assessment,
                title = "Technology Maturity Assessment",
                subtitle = "Evaluating readiness and outlining steps to commercialize your technology.",
                services = listOf("Streamline Focus", "Innovation Tracking", "Market Edge", "Smart Resourcing"),
                isExpanded = false,
                learnMoreUrl = "http://otmt.iiitd.edu.in/Services/Tech_Assessment"
            )
        )

        if (::expandableCardAdapter.isInitialized) {
            expandableCardAdapter.notifyDataSetChanged()
        }
    }

    private fun setupFaqRecyclerView() {
        if (!::faqAdapter.isInitialized) {
            faqAdapter = FaqAdapter(faqData)
            binding.rvFaqs.apply {
                adapter = faqAdapter
                layoutManager = LinearLayoutManager(context)
                isNestedScrollingEnabled = false
                itemAnimator = DefaultItemAnimator()
            }
        }
    }

    private fun loadFaqData() {
        if (faqData.isNotEmpty()) {
            if (::faqAdapter.isInitialized) {
                faqAdapter.notifyDataSetChanged()
            }
            return
        }
        faqData.clear()

        faqData.add(FaqItem("Who can participate in the workshops and events?", "Workshops and events are open to students, faculty members, and external entrepreneurs interested in fostering innovation."))
        faqData.add(FaqItem("How can I schedule a one-on-one innovation consultation?", "Contact our team through the 'Schedule Meeting' button above or other provided channels to schedule a personalized consultation with our innovation experts."))
        faqData.add(FaqItem("What is IPR, and why is it important for innovation?", "Intellectual Property Rights (IPR) are legal protections for intellectual creations, crucial for securing and maximizing the value of groundbreaking ideas during technology transfer."))
        faqData.add(FaqItem("What is a Technology Maturity Assessment (using TRL)?", "It's an evaluation to determine a technology's readiness (using Technology Readiness Levels - TRL) for real-world applications, guiding development and commercialization."))
        faqData.add(FaqItem("Is there a cost associated with participating in workshops and events?", "Most workshops and events are free. Specific events might have registration fees, which will be clearly communicated in the event details."))
        faqData.add(FaqItem("What support is available for commercialization, startup, and funding needs?", "We offer guidance on transforming ideas into market-ready products/services, including advice on product development, market research, business strategies, and funding avenues."))
        faqData.add(FaqItem("How can I get incubation support?", "Reach out to the IITDIC team through the provided channels to inquire about incubation support and the application process."))
        faqData.add(FaqItem("What resources does the Innovation Hub (IHUB) provide?", "IHUB offers a collaborative workspace, access to prototyping tools, and a community environment conducive to ideation and experimentation for startups."))
        faqData.add(FaqItem("How can I access licensed technology or software?", "Explore our portfolio of intellectual assets ready for licensing and collaboration. Details can be found on our platform or by contacting the licensing team."))
        faqData.add(FaqItem("How can I stay updated on innovation events and competitions?", "Regularly check our platform for announcements, subscribe to our newsletter, and follow us on social media for real-time updates."))
        faqData.add(FaqItem("What topics are typically covered in workshops?", "Workshops cover a range, including Technology Readiness, intellectual property rights, design thinking, and entrepreneurship basics."))
        faqData.add(FaqItem("How does the Technology Licensing program work?", "It facilitates turning innovative ideas into real-world solutions through strategic partnerships, IP management, commercialization support, and access to funding sources."))

        if (::faqAdapter.isInitialized) {
            faqAdapter.notifyDataSetChanged()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.rvExpandableCards.adapter = null
        binding.rvFaqs.adapter = null
        _binding = null
    }
}