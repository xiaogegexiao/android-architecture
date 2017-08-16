package com.example.android.architecture.blueprints.todoapp.tasks

import com.example.android.architecture.blueprints.todoapp.data.Task
import com.example.android.architecture.blueprints.todoapp.framework.BaseReactViewModel

class TasksViewModel : BaseReactViewModel(), TasksItemActions {
    override fun onTaskChecked(task: Task, checked: Boolean) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onTaskClicked(task: Task) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}