package ac.iiitd.otmt.fragment_about

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import ac.iiitd.otmt.R
import com.google.android.material.button.MaterialButton

class AboutFragment : Fragment() {

    private lateinit var btnScheduleMeeting: MaterialButton
    private lateinit var btnSubmitDetails: MaterialButton
    private lateinit var btnSendMessage: MaterialButton
    private lateinit var etFullName: EditText
    private lateinit var etEmailAddress: EditText
    private lateinit var etPhoneNumber: EditText
    private lateinit var etSubject: EditText
    private lateinit var etYourMessage: EditText

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_about, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnScheduleMeeting = view.findViewById(R.id.btn_schedule_meeting)
        btnSubmitDetails = view.findViewById(R.id.btn_submit_details)
        btnSendMessage = view.findViewById(R.id.btn_send_message)
        etFullName = view.findViewById(R.id.et_full_name)
        etEmailAddress = view.findViewById(R.id.et_email_address)
        etPhoneNumber = view.findViewById(R.id.et_phone_number)
        etSubject = view.findViewById(R.id.et_subject)
        etYourMessage = view.findViewById(R.id.et_your_message)

        btnScheduleMeeting.setOnClickListener {
            val calendarUrl = "https://calendar.google.com/calendar/u/0/r/eventedit?add=alok@iiitd.ac.in&cls=0&hl=en&pli=1"
            openUrl(calendarUrl)
        }

        btnSubmitDetails.setOnClickListener {
            val googleFormUrl = "https://docs.google.com/forms/d/e/1FAIpQLSfKpIvwOYj2-a5-ocXptLMyd1tPHH0ABw5z6txjsFhXMEtu-g/viewform?pli=1"
            openUrl(googleFormUrl)
        }

        btnSendMessage.setOnClickListener {
            sendEmailViaGmail()
        }
    }

    private fun openUrl(url: String) {
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(requireContext(), "Cannot open link. No application found.", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Toast.makeText(requireContext(), "Error opening link.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun sendEmailViaGmail() {
        val recipient = "anishdev7050@gmail.com"
        val fullName = etFullName.text.toString().trim()
        val emailAddress = etEmailAddress.text.toString().trim()
        val phoneNumber = etPhoneNumber.text.toString().trim()
        val subject = etSubject.text.toString().trim()
        val message = etYourMessage.text.toString().trim()

        if (emailAddress.isEmpty() || subject.isEmpty() || message.isEmpty()) {
            Toast.makeText(requireContext(), "Please fill in all required (*) fields.", Toast.LENGTH_LONG).show()
            return
        }

        val emailBody = """
            $message
        """.trimIndent()

        val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_EMAIL, arrayOf(recipient))
            putExtra(Intent.EXTRA_SUBJECT, subject)
            putExtra(Intent.EXTRA_TEXT, emailBody)
            setPackage("com.google.android.gm")
        }

        try {
            startActivity(emailIntent)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(
                requireContext(),
                "Gmail app not found. Please install Gmail or use another method to contact us.",
                Toast.LENGTH_LONG
            ).show()
        } catch (e: Exception) {
            Toast.makeText(requireContext(), "Error sending email.", Toast.LENGTH_SHORT).show()
        }
    }
}