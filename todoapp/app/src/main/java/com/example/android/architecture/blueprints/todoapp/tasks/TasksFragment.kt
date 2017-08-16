/*
 * Copyright 2016, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.architecture.blueprints.todoapp.tasks

import android.arch.lifecycle.LifecycleFragment
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.content.ContextCompat
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.PopupMenu
import android.view.*
import com.example.android.architecture.blueprints.todoapp.R
import com.example.android.architecture.blueprints.todoapp.data.Task
import com.example.android.architecture.blueprints.todoapp.databinding.TasksFragBinding
import java.util.*

interface TasksActions {
    fun onAddNewTaskClick()
    fun onRefresh()
}

/**
 * Display a grid of [Task]s. User can choose to view all, active or completed tasks.
 */
class TasksFragment : LifecycleFragment() {

    companion object {
        fun newInstance(): TasksFragment {
            return TasksFragment()
        }
    }

    private lateinit var mTasksFragBinding: TasksFragBinding

    private var mListAdapter: TasksAdapter? = null

    override fun onResume() {
        super.onResume()
//        mTasksViewModel!!.start()
    }

    private lateinit var mTasksViewModel: TasksViewModel

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        mTasksFragBinding = TasksFragBinding.inflate(inflater, container, false)

        mTasksViewModel = (activity as ViewModelProvider).obtainViewModel(TasksViewModel::class.java)

        mTasksFragBinding.viewmodel = TasksViewData(true, true, R.string.label_all, R.string.add_task, R.string.no_tasks_active, true, emptyList())



        setHasOptionsMenu(true)

        return mTasksFragBinding.root
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) {
//            R.id.menu_clear -> mTasksViewModel!!.clearCompletedTasks()
            R.id.menu_filter -> showFilteringPopUpMenu()
//            R.id.menu_refresh -> mTasksViewModel!!.loadTasks(true)
        }
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater!!.inflate(R.menu.tasks_fragment_menu, menu)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setupSnackbar()

        setupFab()

        setupListAdapter()

        setupRefreshLayout()
    }

    private fun setupSnackbar() {
//        mTasksViewModel!!.getSnackbarMessage().observe(this, object : SnackbarMessage.SnackbarObserver() {
//            fun onNewMessage(@StringRes snackbarMessageResourceId: Int) {
//                SnackbarUtils.showSnackbar(view, getString(snackbarMessageResourceId))
//            }
//        })
    }

    private fun showFilteringPopUpMenu() {
        val popup = PopupMenu(context, activity.findViewById<View>(R.id.menu_filter))
        popup.menuInflater.inflate(R.menu.filter_tasks, popup.menu)

        popup.setOnMenuItemClickListener { item ->
            when (item.itemId) {
//                R.id.active -> mTasksViewModel!!.setFiltering(TasksFilterType.ACTIVE_TASKS)
//                R.id.completed -> mTasksViewModel!!.setFiltering(TasksFilterType.COMPLETED_TASKS)
//                else -> mTasksViewModel!!.setFiltering(TasksFilterType.ALL_TASKS)
            }
//            mTasksViewModel!!.loadTasks(false)
            true
        }

        popup.show()
    }

    private fun setupFab() {
        val fab = activity.findViewById<View>(R.id.fab_add_task) as FloatingActionButton

        fab.setImageResource(R.drawable.ic_add)
//        fab.setOnClickListener { mTasksViewModel!!.addNewTask() }
    }

    private fun setupListAdapter() {
        val listView = mTasksFragBinding.tasksList

        mListAdapter = TasksAdapter(
                ArrayList<Task>(0),
                mTasksViewModel
        )
        listView.adapter = mListAdapter
    }

    private fun setupRefreshLayout() {
        val listView = mTasksFragBinding.tasksList
        val swipeRefreshLayout = mTasksFragBinding.refreshLayout
        swipeRefreshLayout.setColorSchemeColors(
                ContextCompat.getColor(activity, R.color.colorPrimary),
                ContextCompat.getColor(activity, R.color.colorAccent),
                ContextCompat.getColor(activity, R.color.colorPrimaryDark)
        )
        // Set the scrolling view in the custom SwipeRefreshLayout.
        swipeRefreshLayout.setScrollUpChild(listView)
    }


}
