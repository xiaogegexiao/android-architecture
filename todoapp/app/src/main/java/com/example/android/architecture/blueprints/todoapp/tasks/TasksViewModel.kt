package com.example.android.architecture.blueprints.todoapp.tasks

import com.example.android.architecture.blueprints.todoapp.R
import com.example.android.architecture.blueprints.todoapp.data.Task
import com.example.android.architecture.blueprints.todoapp.data.source.TasksDataSource
import com.example.android.architecture.blueprints.todoapp.data.source.TasksRepository
import com.example.android.architecture.blueprints.todoapp.framework.BaseReactViewModel
import com.example.android.architecture.blueprints.todoapp.tasks.TasksFilterType.*

interface TasksActions {
    fun onTaskDetails(taskId: String)
    fun onNewTask()
}

class TasksViewModel(private val tasksRepository: TasksRepository, private val tasksActions: TasksActions) : BaseReactViewModel<TasksViewData>(), TasksItemActions, TasksViewActions {

    private var currentFiltering: TasksFilterType = ALL_TASKS

    override fun onActiveView() {
        loadTasks(false)
    }

    override fun onRefreshPull() {
        loadTasks(true)
    }

    override fun onAddNewTaskClick() {
        tasksActions.onNewTask()
    }

    override val initialViewData = TasksViewData(
            false,
            false,
            R.string.label_all,
            R.drawable.ic_assignment_turned_in_24dp, R.string.no_tasks_all,
            true,
            emptyList())

    override fun onTaskChecked(task: Task, checked: Boolean) {
        if (checked) {
            tasksRepository.completeTask(task)
        } else {
            tasksRepository.activateTask(task)
        }
        loadTasks(false, false)
    }

    override fun onTaskClicked(task: Task) {
        tasksActions.onTaskDetails(task.id)
    }

    fun setFiltering(requestType: TasksFilterType) {
        currentFiltering = requestType
        // Depending on the filter type, set the filtering label, icon drawables, etc.
        updateViewData(
                when (requestType) {
                    ALL_TASKS -> viewData.value.copy(
                            currentFilteringLabel = R.string.label_all,
                            noTasksLabel = R.string.no_tasks_all,
                            noTaskIconRes = R.drawable.ic_assignment_turned_in_24dp,
                            tasksAddViewVisible = true
                    )

                    ACTIVE_TASKS -> viewData.value.copy(
                            currentFilteringLabel = R.string.label_active,
                            noTasksLabel = R.string.no_tasks_active,
                            noTaskIconRes = R.drawable.ic_check_circle_24dp,
                            tasksAddViewVisible = false
                    )

                    COMPLETED_TASKS -> viewData.value.copy(
                            currentFilteringLabel = R.string.label_completed,
                            noTasksLabel = R.string.no_tasks_completed,
                            noTaskIconRes = R.drawable.ic_verified_user_24dp,
                            tasksAddViewVisible = false
                    )
                })

        loadTasks(false)
    }

    fun onMenuRefresh() {
        loadTasks(true)
    }

    fun onMenuClear() {
        tasksRepository.clearCompletedTasks();
        //mSnackbarText.setValue(R.string.completed_tasks_cleared);
        loadTasks(true, false)
    }

    /**
     * @param forceUpdate   Pass in true to refresh the data in the [TasksDataSource]
     * *
     * @param showLoadingUI Pass in true to display a loading icon in the UI
     */
    private fun loadTasks(forceUpdate: Boolean, showLoadingUI: Boolean = true) {
        if (showLoadingUI) {
            updateViewData(viewData.value.copy(dataLoading = true))
        }
        if (forceUpdate) {
            tasksRepository.refreshTasks()
        }

        tasksRepository.getTasks(object : TasksDataSource.LoadTasksCallback {
            override fun onTasksLoaded(tasks: List<Task>) {
                val tasksToShow = ArrayList<Task>()

                // We filter the tasks based on the requestType
                for (task in tasks) {
                    when (currentFiltering) {
                        ALL_TASKS -> tasksToShow.add(task)
                        ACTIVE_TASKS -> if (task.isActive()) {
                            tasksToShow.add(task)
                        }
                        COMPLETED_TASKS -> if (task.completed) {
                            tasksToShow.add(task)
                        }
                    }
                }

                updateViewData(viewData.value.copy(
                        items = ArrayList(tasksToShow),
                        empty = tasksToShow.isEmpty(),
                        dataLoading = false
                ))
            }

            override fun onDataNotAvailable() {
            }
        })
    }
}