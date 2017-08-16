package com.example.android.architecture.blueprints.todoapp.tasks

import com.example.android.architecture.blueprints.todoapp.framework.BaseReactViewModel

interface Data
object TasksData : Data

class MainViewModel : BaseReactViewModel<Data>() {
    override val initialViewData = TasksData
}