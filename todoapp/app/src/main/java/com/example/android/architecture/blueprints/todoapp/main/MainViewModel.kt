package com.example.android.architecture.blueprints.todoapp.main

import com.example.android.architecture.blueprints.todoapp.addedittask.AddEditActions
import com.example.android.architecture.blueprints.todoapp.framework.BaseReactViewModel
import com.example.android.architecture.blueprints.todoapp.tasks.TasksActions

interface Data
object TasksData : Data
data class EditTaskData(val taskId: String) : Data
object NewTaskData : Data

class MainViewModel : BaseReactViewModel<Data>(), TasksActions, AddEditActions {

    override val initialViewData = TasksData

    override fun onSaveTask() {
        updateViewData(TasksData)
    }

    override fun onTaskDetails(taskId: String) {
        updateViewData(EditTaskData(taskId))
    }

    override fun onNewTask() {
        updateViewData(NewTaskData)
    }

    fun onBackPress(): Boolean {
        return when (viewData.value) {
            NewTaskData, is EditTaskData -> {
                updateViewData(TasksData)
                true
            }
            else -> false
        }
    }
}