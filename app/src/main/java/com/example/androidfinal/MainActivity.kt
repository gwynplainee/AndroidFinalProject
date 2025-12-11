package com.example.androidfinal

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.androidfinal.core.ui.fragments.HomeFragment

class MainActivity : AppCompatActivity() {
    private lateinit var navHostFragment: NavHostFragment
    private lateinit var navController: NavController
    private lateinit var bottomNavContainer: View

    private lateinit var navHome: View
    private lateinit var navMore: View
    private lateinit var imgHome: ImageView
    private lateinit var txtHome: TextView
    private lateinit var imgMore: ImageView
    private lateinit var txtMore: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        bottomNavContainer = findViewById(R.id.bottom_nav_container)
        navHome = findViewById(R.id.nav_home)
        navMore = findViewById(R.id.nav_more)
        imgHome = findViewById(R.id.img_home)
        txtHome = findViewById(R.id.txt_home)
        imgMore = findViewById(R.id.img_more)
        txtMore = findViewById(R.id.txt_more)

        navHome.setOnClickListener {
            handleHomeNavigation()
        }

        navMore.setOnClickListener {
            navController.navigate(R.id.moreFragment)
        }

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.homeFragment -> {
                    bottomNavContainer.visibility = View.VISIBLE
                    updateNavState(isHomeSelected = true)
                }
                R.id.moreFragment -> {
                    bottomNavContainer.visibility = View.VISIBLE
                    updateNavState(isHomeSelected = false)
                }
                else -> {
                    bottomNavContainer.visibility = View.GONE
                }
            }
        }
    }

    private fun updateNavState(isHomeSelected: Boolean) {
        val activeColor = ContextCompat.getColor(this, R.color.active_nav)
        val inactiveColor = ContextCompat.getColor(this, R.color.inactive_nav)

        if (isHomeSelected) {
            imgHome.setColorFilter(activeColor)
            txtHome.setTextColor(activeColor)

            imgMore.setColorFilter(inactiveColor)
            txtMore.setTextColor(inactiveColor)
        } else {
            imgHome.setColorFilter(inactiveColor)
            txtHome.setTextColor(inactiveColor)

            imgMore.setColorFilter(activeColor)
            txtMore.setTextColor(activeColor)
        }
    }

    private fun handleHomeNavigation(){
        val currentDestination = navController.currentDestination?.id

        if(currentDestination == R.id.homeFragment){
            val currentFragment = navHostFragment.childFragmentManager.fragments.firstOrNull()
            (currentFragment as? HomeFragment)?.requestScrollToTop()
        }else{
            navController.navigate(
                R.id.homeFragment,
                null,
                androidx.navigation.navOptions {
                    popUpTo(R.id.homeFragment){
                        inclusive = true
                    }
                    launchSingleTop = true
                }
                )
        }
    }
}
