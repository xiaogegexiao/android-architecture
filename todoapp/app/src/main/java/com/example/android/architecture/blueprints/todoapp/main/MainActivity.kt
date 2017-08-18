package com.example.android.architecture.blueprints.todoapp.main

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.view.MenuItem
import com.example.android.architecture.blueprints.todoapp.Dependencies
import com.example.android.architecture.blueprints.todoapp.DependencyProvider
import com.example.android.architecture.blueprints.todoapp.LifecycleAppCompatActivity
import com.example.android.architecture.blueprints.todoapp.R
import com.example.android.architecture.blueprints.todoapp.addedittask.AddEditActions
import com.example.android.architecture.blueprints.todoapp.addedittask.AddEditTaskFragment
import com.example.android.architecture.blueprints.todoapp.data.source.TasksRepository
import com.example.android.architecture.blueprints.todoapp.data.source.local.TasksLocalDataSource
import com.example.android.architecture.blueprints.todoapp.data.source.remote.TasksRemoteDataSource
import com.example.android.architecture.blueprints.todoapp.tasks.TasksActions
import com.example.android.architecture.blueprints.todoapp.tasks.TasksFragment
import com.example.android.architecture.blueprints.todoapp.util.ActivityUtils

class MainActivityDependencies(private val activity: FragmentActivity) : Dependencies {

    override fun taskRepository(): TasksRepository {
        return TasksRepository.getInstance(TasksRemoteDataSource.instance, TasksLocalDataSource.getInstance(activity))
    }

    fun mainViewModel(): MainViewModel = ViewModelProviders.of(activity).get(MainViewModel::class.java)

    fun tasksActions(): TasksActions = mainViewModel()

    fun addEditActions(): AddEditActions = mainViewModel()
}

class MainActivity : LifecycleAppCompatActivity(), DependencyProvider<MainActivityDependencies> {

    override val dependencies: MainActivityDependencies by lazy {
        MainActivityDependencies(this)
    }

    private lateinit var drawerLayout: DrawerLayout

    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.tasks_act)

        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        viewModel.viewData.observe(this, Observer { data ->
            handleViewData(data!!)
        })

        setupToolbar()
        setupNavigationDrawer()
    }

    private fun handleViewData(data: Data) {
        when (data) {
            TasksData -> {
                supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_menu)
                supportActionBar!!.title = ""
                setupViewFragment(TasksFragment.newInstance())
            }
            is EditTaskData -> {
                supportActionBar!!.setHomeAsUpIndicator(R.drawable.abc_ic_ab_back_material)
                supportActionBar!!.setTitle(R.string.edit_task)
                setupViewFragment(AddEditTaskFragment.newInstance(data.taskId))
            }
            NewTaskData -> {
                supportActionBar!!.setHomeAsUpIndicator(R.drawable.abc_ic_ab_back_material)
                supportActionBar!!.setTitle(R.string.add_task)
                setupViewFragment(AddEditTaskFragment.newInstance())
            }
        }
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
        supportActionBar!!.setDisplayShowHomeEnabled(true)
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
            }
            // Do nothing, we're already on that screen
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
                if (!viewModel.onBackPress()) {
                    drawerLayout.openDrawer(GravityCompat.START)
                }
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        if (!viewModel.onBackPress()) {
            super.onBackPressed()
        }
    }
}