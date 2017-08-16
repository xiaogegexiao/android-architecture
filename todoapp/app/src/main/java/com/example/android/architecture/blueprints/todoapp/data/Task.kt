package com.example.android.architecture.blueprints.todoapp.data;

import java.util.*

data class Task(val id : String = UUID.randomUUID().toString(), val title : String, val description: String, val completed: Boolean) {
    fun getTitleForList() = if (!title.isEmpty()) title else description
}