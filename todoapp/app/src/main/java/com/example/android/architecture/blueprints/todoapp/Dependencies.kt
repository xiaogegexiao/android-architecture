package com.example.android.architecture.blueprints.todoapp

import com.example.android.architecture.blueprints.todoapp.data.source.TasksRepository

interface DependencyProvider<T : Dependencies> {
    val dependencies : T
}

interface Dependencies {
    fun taskRepository(): TasksRepository
}