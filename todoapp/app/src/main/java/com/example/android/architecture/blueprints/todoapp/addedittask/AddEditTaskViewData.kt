package com.example.android.architecture.blueprints.todoapp.addedittask

import com.example.android.architecture.blueprints.todoapp.util.ViewMessage

data class AddEditTaskViewData(
        val dataLoading : Boolean,
        var title: String,
        var description : String,
        val message: ViewMessage)