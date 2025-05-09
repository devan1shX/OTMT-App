package ac.iiitd.otmt.fragment_technology.ui.tabs

import android.graphics.Color
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import ac.iiitd.otmt.R
import ac.iiitd.otmt.fragment_technology.data.model.ImageInfo
import ac.iiitd.otmt.fragment_technology.data.model.Technology
import ac.iiitd.otmt.fragment_technology.ui.viewmodel.TechnologyViewModel

class TabMediaFragment : Fragment() {

    private val technologyViewModel: TechnologyViewModel by activityViewModels()

    private var imagesTitle: TextView? = null
    private var imagesContainer: LinearLayout? = null
    private var noImagesText: TextView? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_tab_media, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        imagesTitle = view.findViewById(R.id.tv_tab_images_title)
        imagesContainer = view.findViewById(R.id.ll_tab_images_container)
        noImagesText = view.findViewById(R.id.tv_tab_no_images)

        observeViewModel()
    }

    private fun observeViewModel() {
        technologyViewModel.selectedTechnology.observe(viewLifecycleOwner) { technology ->
            technology?.let { populateUi(it) } ?: clearUi()
        }
    }

    private fun populateUi(tech: Technology) {
        populateImages(tech.images)
    }

    private fun clearUi() {
        imagesContainer?.removeAllViews()
        imagesTitle?.isVisible = false
        imagesContainer?.isVisible = false
        noImagesText?.isVisible = true
    }


    private fun populateImages(images: List<ImageInfo>?) {
        imagesContainer?.removeAllViews()
        val hasImages = !images.isNullOrEmpty()

        imagesContainer?.isVisible = hasImages
        imagesTitle?.isVisible = hasImages
        noImagesText?.isVisible = !hasImages

        if (hasImages) {
            images?.forEachIndexed { index, imageInfo ->
                if (!imageInfo.url.isNullOrBlank()) {
                    val context = context ?: return@forEachIndexed

                    val imageView = ImageView(context).apply {
                        layoutParams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                        ).apply {
                            bottomMargin = dpToPx(16)
                        }
                        adjustViewBounds = true
                        scaleType = ImageView.ScaleType.FIT_CENTER
                        contentDescription = imageInfo.caption ?: "Technology Image ${index + 1}"
                    }

                    Glide.with(this@TabMediaFragment)
                        .load(imageInfo.url)
                        .placeholder(R.drawable.ic_placeholder_image)
                        .error(R.drawable.ic_error_image)
                        .into(imageView)

                    imagesContainer?.addView(imageView)

                    if (!imageInfo.caption.isNullOrBlank()) {
                        val captionTextView = TextView(context).apply {
                            layoutParams = LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                            ).apply {
                                topMargin = dpToPx(4)
                                bottomMargin = dpToPx(16)
                            }
                            text = imageInfo.caption
                            setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f)
                            setTextColor(Color.DKGRAY)
                            setTextIsSelectable(true)
                        }
                        imagesContainer?.addView(captionTextView)
                    }
                }
            }
        }
    }

    private fun dpToPx(dp: Int): Int {
        return (dp * resources.displayMetrics.density).toInt()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        imagesTitle = null
        imagesContainer?.removeAllViews()
        imagesContainer = null
        noImagesText = null
    }

    companion object {
        fun newInstance() = TabMediaFragment()
    }
}
