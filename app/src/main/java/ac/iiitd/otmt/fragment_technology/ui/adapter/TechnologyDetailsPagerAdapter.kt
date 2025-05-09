package ac.iiitd.otmt.fragment_technology.ui.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import ac.iiitd.otmt.fragment_technology.ui.tabs.TabFeaturesFragment
import ac.iiitd.otmt.fragment_technology.ui.tabs.TabLinksPeopleFragment
import ac.iiitd.otmt.fragment_technology.ui.tabs.TabMediaFragment
import ac.iiitd.otmt.fragment_technology.ui.tabs.TabOverviewFragment

private const val NUM_TABS = 4

class TechnologyDetailsPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int {
        return NUM_TABS
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> TabOverviewFragment.newInstance()
            1 -> TabFeaturesFragment.newInstance()
            2 -> TabLinksPeopleFragment.newInstance()
            3 -> TabMediaFragment.newInstance()
            else -> throw IllegalArgumentException("Invalid tab position: $position")
        }
    }
}
