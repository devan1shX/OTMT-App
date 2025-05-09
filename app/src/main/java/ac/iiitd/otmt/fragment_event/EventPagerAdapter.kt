package ac.iiitd.otmt.fragment_event

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import ac.iiitd.otmt.fragment_event.fragment_past.PastHighlightsFragment
import ac.iiitd.otmt.fragment_event.fragment_upcoming.UpcomingEventsFragment

private const val NUM_TABS = 2

class EventPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int {
        return NUM_TABS
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> UpcomingEventsFragment()
            1 -> PastHighlightsFragment()
            else -> throw IllegalStateException("Invalid position: $position")
        }
    }
}
