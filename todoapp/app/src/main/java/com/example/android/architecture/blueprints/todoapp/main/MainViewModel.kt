package com.example.android.architecture.blueprints.todoapp.main

import com.example.android.architecture.blueprints.todoapp.framework.BaseReactViewModel
import com.example.android.architecture.blueprints.todoapp.tasks.TasksActions

interface Data
object TasksData : Data
data class TaskDetailsData(val taskId: String) : Data
object NewTaskData : Data

class MainViewModel : BaseReactViewModel<Data>(), TasksActions {

    override val initialViewData = TasksData

    override fun onTaskDetails(taskId: String) {
        updateViewData(TaskDetailsData(taskId))
    }

    override fun onNewTask() {
        updateViewData(NewTaskData)
    }
}