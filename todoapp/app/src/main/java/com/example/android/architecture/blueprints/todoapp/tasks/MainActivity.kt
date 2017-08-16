package com.example.android.architecture.blueprints.todoapp.tasks

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModel
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.view.MenuItem
import com.example.android.architecture.blueprints.todoapp.LifecycleAppCompatActivity
import com.example.android.architecture.blueprints.todoapp.R
import com.example.android.architecture.blueprints.todoapp.util.ActivityUtils

interface ViewModelProvider {
    fun <V : ViewModel> obtainViewModel(viewModelClass: Class<V>): V
}

class MainActivity : LifecycleAppCompatActivity(), ViewModelProvider {

    private lateinit var drawerLayout: DrawerLayout

    @Suppress("UNCHECKED_CAST")
    override fun <V : ViewModel> obtainViewModel(viewModelClass: Class<V>): V {

        // TODO use factory and view ModelProvider for injection
        return when(viewModelClass) {
            TasksViewModel::class.java -> TasksViewModel() as V
            MainViewModel::class.java -> MainViewModel() as V
            else ->  throw RuntimeException("Unknow viewModelClass " + viewModelClass)
        }
    }

    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.tasks_act)

        viewModel = obtainViewModel(MainViewModel::class.java)
        viewModel.viewData.observe(this, Observer { data ->
            when(data) {
               TasksData -> setupViewFragment(TasksFragment.newInstance())
            }
        })

        setupToolbar()
        setupNavigationDrawer()
    }


    private fun setupViewFragment(fragment: Fragment) {
        if (supportFragmentManager.findFragmentByTag(fragment.javaClass.name) == null) {
            // Create the fragment
            ActivityUtils.replaceFragmentInActivity(
                    supportFragmentManager, fragment, R.id.contentFrame, fragment.javaClass.name)
        }
    }

    private fun setupToolbar() {
        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_menu)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    private fun setupNavigationDrawer() {
        drawerLayout = findViewById<DrawerLayout>(R.id.drawer_layout)
        drawerLayout.setStatusBarBackground(R.color.colorPrimaryDark)
        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        if (navigationView != null) {
            setupDrawerContent(navigationView)
        }
    }

    private fun setupDrawerContent(navigationView: NavigationView) {
        navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.list_navigation_menu_item -> {
                }
                R.id.statistics_navigation_menu_item -> {
//                    val intent = Intent(this@TasksActivity, StatisticsActivity::class.java)
//                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
//                    startActivity(intent)
                }
                else -> {
                }
            }// Do nothing, we're already on that screen
            // Close the navigation drawer when an item is selected.
            menuItem.isChecked = true
            drawerLayout.closeDrawers()
            true
        }
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                // Open the navigation drawer when the home icon is selected from the toolbar.
                drawerLayout.openDrawer(GravityCompat.START)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }


}