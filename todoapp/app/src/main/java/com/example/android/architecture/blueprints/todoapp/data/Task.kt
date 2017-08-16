package com.example.android.architecture.blueprints.todoapp.data

import java.util.*

data class Task(val title : String, val description: String, val id : String = UUID.randomUUID().toString(), val completed: Boolean = false) {
    fun getTitleForList() = if (!title.isEmpty()) title else description
    fun isActive() = !completed
}