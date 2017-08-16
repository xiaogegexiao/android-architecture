package com.example.android.architecture.blueprints.todoapp.taskdetail

import com.example.android.architecture.blueprints.todoapp.data.Task


data class TaskDetailViewData(val dataLoading : Boolean, val dataAvailable: Boolean, val task: Task)
