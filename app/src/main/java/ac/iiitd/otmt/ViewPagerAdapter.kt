package ac.iiitd.otmt

import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import ac.iiitd.otmt.fragment_home.HomeFragment
import ac.iiitd.otmt.fragment_technology.TechnologiesFragment
import ac.iiitd.otmt.fragment_event.EventFragment
import ac.iiitd.otmt.fragment_service.ServiceFragment

class ViewPagerAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {

    private val fragmentCreators: List<() -> Fragment> = listOf(
        { HomeFragment() },
        { TechnologiesFragment() },
        { EventFragment() },
        { ServiceFragment() }
    )

    override fun getItemCount(): Int = fragmentCreators.size

    override fun createFragment(position: Int): Fragment = fragmentCreators[position]()

    fun getPositionForItemId(itemId: Int): Int {
        return when (itemId) {
            R.id.navigation_home -> 0
            R.id.navigation_technologies -> 1
            R.id.navigation_events -> 2
            R.id.navigation_services -> 3
            else -> -1
        }
    }

    fun getItemIdForPosition(position: Int): Int {
        return when (position) {
            0 -> R.id.navigation_home
            1 -> R.id.navigation_technologies
            2 -> R.id.navigation_events
            3 -> R.id.navigation_services
            else -> View.NO_ID
        }
    }
}