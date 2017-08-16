package com.example.android.architecture.blueprints.todoapp.tasks

import com.example.android.architecture.blueprints.todoapp.framework.BaseReactViewModel

interface Data
object TasksData : Data
data class TaskDetailsData(val taskId: String) : Data
object NewTaskData : Data

class MainViewModel : BaseReactViewModel<Data>(), TasksActions {

    override fun onTaskDetails(taskId: String) {
        updateViewData(TaskDetailsData(taskId))
    }

    override fun onNewTask() {
        updateViewData(NewTaskData)
    }

    override val initialViewData = TasksData
}