package ac.iiitd.otmt.fragment_main_pager

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.widget.ViewPager2
import ac.iiitd.otmt.R
import ac.iiitd.otmt.ViewPagerAdapter

interface MainPagerListener {
    fun onViewPagerPageChanged(position: Int)
}

class MainPagerFragment : Fragment() {
    private lateinit var viewPager: ViewPager2
    private lateinit var pagerAdapter: ViewPagerAdapter
    private var listener: MainPagerListener? = null

    companion object {
        const val TAG = "MainPagerFragmentTag"
        fun newInstance() = MainPagerFragment()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is MainPagerListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement MainPagerListener")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_main_pager, container, false)
        viewPager = view.findViewById(R.id.main_view_pager_internal)

        pagerAdapter = ViewPagerAdapter(requireActivity() as FragmentActivity)
        viewPager.adapter = pagerAdapter
        viewPager.offscreenPageLimit = pagerAdapter.itemCount - 1


        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                listener?.onViewPagerPageChanged(position)
            }
        })
        return view
    }

    fun setCurrentPage(position: Int, smoothScroll: Boolean = true) {
        if (::viewPager.isInitialized && viewPager.currentItem != position) {
            viewPager.setCurrentItem(position, smoothScroll)
        }
    }

    fun getCurrentPage(): Int {
        return if (::viewPager.isInitialized) viewPager.currentItem else 0
    }

    fun getInternalAdapter(): ViewPagerAdapter {
        return pagerAdapter
    }


    override fun onDetach() {
        super.onDetach()
        listener = null
    }
}
