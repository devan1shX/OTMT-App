package ac.iiitd.otmt

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import ac.iiitd.otmt.fragment_about.AboutFragment
import ac.iiitd.otmt.fragment_main_pager.MainPagerFragment
import ac.iiitd.otmt.fragment_main_pager.MainPagerListener
import com.google.android.material.button.MaterialButton
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity(), MainPagerListener {

    private lateinit var bottomNavView: BottomNavigationView

    private val HOME_POSITION = 0
    private val ABOUT_FRAGMENT_TAG = "AboutFragmentTag"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val headerView: View = findViewById(R.id.app_toolbar)
        val backArrowIcon: ImageView = headerView.findViewById(R.id.logo_image)
        val collaborateButton: MaterialButton = headerView.findViewById(R.id.collaborate_button)

        bottomNavView = findViewById(R.id.bottom_nav_view)

        backArrowIcon.setOnClickListener {
            handleBackArrowClick()
        }

        collaborateButton.setOnClickListener {
            handleCollaborateClick()
        }

        if (savedInstanceState == null) {
            val pagerFrag = ensureMainPagerFragment(isReplacing = true)
            pagerFrag.setCurrentPage(HOME_POSITION, false)
        } else {
            val mainPager = supportFragmentManager.findFragmentByTag(MainPagerFragment.TAG) as? MainPagerFragment
            mainPager?.let {
                onViewPagerPageChanged(it.getCurrentPage())
            }
        }

        bottomNavView.setOnItemSelectedListener { menuItem ->
            val mainPagerForAdapter = supportFragmentManager.findFragmentByTag(MainPagerFragment.TAG) as? MainPagerFragment
            val adapter = mainPagerForAdapter?.getInternalAdapter() ?: ViewPagerAdapter(this)
            val targetPagePosition = adapter.getPositionForItemId(menuItem.itemId)

            if (targetPagePosition != -1) {
                val currentFragmentInContainer = supportFragmentManager.findFragmentById(R.id.fragment_container)

                if (currentFragmentInContainer !is MainPagerFragment) {
                    if (currentFragmentInContainer is AboutFragment) {
                        supportFragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
                    }
                    val activePager = ensureMainPagerFragment(isReplacing = true)
                    activePager.setCurrentPage(targetPagePosition, false)
                } else {
                    currentFragmentInContainer.setCurrentPage(targetPagePosition)
                }
                true
            } else {
                false
            }
        }
    }

    private fun ensureMainPagerFragment(isReplacing: Boolean = false): MainPagerFragment {
        val currentFragmentInContainer = supportFragmentManager.findFragmentById(R.id.fragment_container)

        if (currentFragmentInContainer is MainPagerFragment && currentFragmentInContainer.tag == MainPagerFragment.TAG) {
            return currentFragmentInContainer
        }

        val newPagerFragment = MainPagerFragment.newInstance()
        val transaction = supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, newPagerFragment, MainPagerFragment.TAG)

        if (isReplacing || supportFragmentManager.isStateSaved) {
            transaction.commitNowAllowingStateLoss()
        } else {
            transaction.commit()
        }
        return newPagerFragment
    }

    override fun onViewPagerPageChanged(position: Int) {
        val mainPager = supportFragmentManager.findFragmentByTag(MainPagerFragment.TAG) as? MainPagerFragment
        val adapter = mainPager?.getInternalAdapter() ?: ViewPagerAdapter(this)

        val itemId = adapter.getItemIdForPosition(position)
        if (itemId != View.NO_ID && bottomNavView.selectedItemId != itemId) {
            bottomNavView.selectedItemId = itemId
        }
    }

    private fun handleBackArrowClick() {
        val currentFragment = supportFragmentManager.findFragmentById(R.id.fragment_container)
        if (currentFragment is AboutFragment) {
            supportFragmentManager.popBackStack()
            val pager = ensureMainPagerFragment(isReplacing = true)
            pager.setCurrentPage(HOME_POSITION)
        } else {
            val pager = ensureMainPagerFragment(isReplacing = currentFragment !is MainPagerFragment)
            if (pager.getCurrentPage() != HOME_POSITION) {
                pager.setCurrentPage(HOME_POSITION)
            }
        }
    }

    private fun handleCollaborateClick() {
        val currentFragment = supportFragmentManager.findFragmentById(R.id.fragment_container)
        if (currentFragment !is AboutFragment) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, AboutFragment(), ABOUT_FRAGMENT_TAG)
                .addToBackStack(ABOUT_FRAGMENT_TAG)
                .commit()
        }
    }

    override fun onBackPressed() {
        val currentFragment = supportFragmentManager.findFragmentById(R.id.fragment_container)

        if (currentFragment is AboutFragment) {
            super.onBackPressed()
            val mainPager = supportFragmentManager.findFragmentByTag(MainPagerFragment.TAG) as? MainPagerFragment
            mainPager?.let { onViewPagerPageChanged(it.getCurrentPage()) }
            return
        }

        if (currentFragment is MainPagerFragment) {
            if (currentFragment.getCurrentPage() != HOME_POSITION) {
                currentFragment.setCurrentPage(HOME_POSITION)
            } else {
                super.onBackPressed()
            }
        } else {
            if (supportFragmentManager.backStackEntryCount > 0) {
                super.onBackPressed()
                val mainPager = supportFragmentManager.findFragmentByTag(MainPagerFragment.TAG) as? MainPagerFragment
                mainPager?.let { onViewPagerPageChanged(it.getCurrentPage()) }
            } else {
                val pager = ensureMainPagerFragment(isReplacing = true)
                if (pager.getCurrentPage() != HOME_POSITION) {
                    pager.setCurrentPage(HOME_POSITION)
                } else {
                    super.onBackPressed()
                }
            }
        }
    }
}