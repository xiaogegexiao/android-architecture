package com.example.android.architecture.blueprints.todoapp.addedittask

import android.support.annotation.StringRes

data class AddEditTaskViewData(val dataLoading : Boolean, @StringRes val title: Int, @StringRes val description : Int)