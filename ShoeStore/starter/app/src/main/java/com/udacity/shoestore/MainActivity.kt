package com.udacity.shoestore

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.udacity.shoestore.databinding.ActivityMainBinding
import timber.log.Timber

class MainActivity : AppCompatActivity() {

    //12. In MainActivity, setup the nav controller with the toolbar and an AppBarConfiguration
    //13. In MainActivity, add a Logout menu to return to the login screen

    lateinit var binding:ActivityMainBinding

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.plant(Timber.DebugTree())
        binding = DataBindingUtil.setContentView<ActivityMainBinding>(this,R.layout.activity_main)
        setSupportActionBar(binding.toolbar2)

        drawerLayout = binding.drawerLayout


        val navHostFragment = supportFragmentManager.findFragmentById(R.id.loginLayout) as NavHostFragment
        val navController = navHostFragment.navController

        NavigationUI.setupActionBarWithNavController(this, navController, drawerLayout)
        appBarConfiguration = AppBarConfiguration(navController.graph, drawerLayout)
        NavigationUI.setupWithNavController(binding.navView, navController)
        binding.navView.setNavigationItemSelectedListener { menuItem ->
            val id = menuItem.itemId
            //it's possible to do more actions on several items, if there is a large amount of items I prefer switch(){case} instead of if()
            if (id == R.id.loginFragment) {
                navController.popBackStack(R.id.loginFragment, false)
//                navController.navigate(R.id.fragment_company);
            }
            //This is for maintaining the behavior of the Navigation view
            NavigationUI.onNavDestinationSelected(menuItem, navController)
            //This is for closing the drawer after acting on it
            drawerLayout.closeDrawer(GravityCompat.START)
            true
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = this.findNavController(R.id.loginLayout)
        return NavigationUI.navigateUp(navController,drawerLayout)
    }
}
