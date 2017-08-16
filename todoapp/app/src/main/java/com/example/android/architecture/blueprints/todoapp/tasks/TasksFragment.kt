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

import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.content.ContextCompat
import android.support.v7.widget.PopupMenu
import android.view.*
import com.example.android.architecture.blueprints.todoapp.R
import com.example.android.architecture.blueprints.todoapp.data.Task
import com.example.android.architecture.blueprints.todoapp.databinding.TasksFragBinding
import com.example.android.architecture.blueprints.todoapp.framework.BaseReactFragment
import com.example.android.architecture.blueprints.todoapp.framework.FragmentCreator
import java.util.*

interface TasksViewActions {
    fun onAddNewTaskClick()
    fun onRefreshPull()
}

/**
 * Display a grid of [Task]s. User can choose to view all, active or completed tasks.
 */
class TasksFragment : BaseReactFragment<Bundle, TasksViewData, TasksViewModel>() {

    companion object : FragmentCreator<Bundle, TasksFragment>(TasksFragment::class.java)

    private lateinit var mTasksFragBinding: TasksFragBinding
    private lateinit var mListAdapter: TasksAdapter

    override fun createViewModel() = (activity as ViewModelProvider).obtainViewModel(getType())
    override fun getType(): Class<TasksViewModel> = TasksViewModel::class.java

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        mTasksFragBinding = TasksFragBinding.inflate(inflater, container, false)

        setHasOptionsMenu(true)

        return mTasksFragBinding.root
    }

    override fun applyViewData(viewData: TasksViewData) {
        mTasksFragBinding.viewmodel = viewData
        mListAdapter.tasks = viewData.items
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            R.id.menu_clear -> viewModel.onMenuClear()
            R.id.menu_filter -> showFilteringPopUpMenu()
            R.id.menu_refresh -> viewModel.onMenuRefresh()
        }
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater) {
        inflater.inflate(R.menu.tasks_fragment_menu, menu)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        mTasksFragBinding.listener = viewModel

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
            viewModel.setFiltering(
                    when (item.itemId) {
                        R.id.active -> TasksFilterType.ACTIVE_TASKS
                        R.id.completed -> TasksFilterType.COMPLETED_TASKS
                        else -> TasksFilterType.ALL_TASKS
                    })
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
                viewModel
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
