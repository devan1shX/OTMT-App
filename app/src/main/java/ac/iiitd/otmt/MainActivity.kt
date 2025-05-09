package ac.iiitd.otmt

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import ac.iiitd.otmt.fragment_about.AboutFragment
import ac.iiitd.otmt.fragment_event.EventFragment
import com.google.android.material.button.MaterialButton
import com.google.android.material.bottomnavigation.BottomNavigationView
import ac.iiitd.otmt.fragment_home.HomeFragment
import ac.iiitd.otmt.fragment_technology.TechnologiesFragment

class MainActivity : AppCompatActivity() {

    private lateinit var bottomNavView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val headerView: View = findViewById(R.id.app_toolbar)

        val backArrowIcon: ImageView = headerView.findViewById(R.id.logo_image)
        val collaborateButton: MaterialButton = headerView.findViewById(R.id.collaborate_button)

        bottomNavView = findViewById(R.id.bottom_nav_view)

        backArrowIcon.setOnClickListener {
            val currentFragment = supportFragmentManager.findFragmentById(R.id.fragment_container)
            if (currentFragment !is HomeFragment) {
                loadFragment(HomeFragment())
                bottomNavView.selectedItemId = R.id.navigation_home
            }
        }

        collaborateButton.setOnClickListener {
            val currentFragment = supportFragmentManager.findFragmentById(R.id.fragment_container)
            if (currentFragment !is AboutFragment) {
                loadFragment(AboutFragment())
            }
        }

        if (savedInstanceState == null) {
            loadFragment(HomeFragment())
            bottomNavView.selectedItemId = R.id.navigation_home
        }

        bottomNavView.setOnItemSelectedListener { menuItem ->
            val currentFragment = supportFragmentManager.findFragmentById(R.id.fragment_container)
            val selectedFragment: Fragment? = when (menuItem.itemId) {
                R.id.navigation_home -> if (currentFragment is HomeFragment) null else HomeFragment()
                R.id.navigation_technologies -> if (currentFragment is TechnologiesFragment) null else TechnologiesFragment()
                R.id.navigation_events -> if (currentFragment is EventFragment) null else EventFragment()
                R.id.navigation_services -> if (currentFragment is ac.iiitd.otmt.fragment_service.ServiceFragment) null else ac.iiitd.otmt.fragment_service.ServiceFragment()
                else -> null
            }

            if (selectedFragment != null) {
                loadFragment(selectedFragment)
            }
            true
        }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }
}