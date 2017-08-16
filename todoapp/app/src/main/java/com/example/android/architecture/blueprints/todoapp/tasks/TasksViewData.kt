package com.example.android.architecture.blueprints.todoapp.tasks

import android.support.annotation.DrawableRes
import android.support.annotation.StringRes
import com.example.android.architecture.blueprints.todoapp.data.Task

data class TasksViewData(val dataLoading : Boolean,
                         val empty: Boolean,
                         @StringRes val currentFilteringLabel : Int,
                         @DrawableRes val noTaskIconRes: Int,
                         @StringRes val noTasksLabel : Int,
                         val tasksAddViewVisible : Boolean,
                         val items: List<Task>)
