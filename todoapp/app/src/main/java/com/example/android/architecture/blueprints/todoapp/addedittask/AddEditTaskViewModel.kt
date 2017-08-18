package com.example.android.architecture.blueprints.todoapp.addedittask

import com.example.android.architecture.blueprints.todoapp.R
import com.example.android.architecture.blueprints.todoapp.data.source.TasksRepository
import com.example.android.architecture.blueprints.todoapp.framework.BaseReactViewModel
import com.example.android.architecture.blueprints.todoapp.util.InvalidMessage
import com.example.android.architecture.blueprints.todoapp.data.Task
import com.example.android.architecture.blueprints.todoapp.data.source.TasksDataSource
import com.example.android.architecture.blueprints.todoapp.util.ViewMessage

interface AddEditActions {
    fun onSaveTask()
}

open class AddViewModel(val tasksRepository: TasksRepository, val addEditActions: AddEditActions) : BaseReactViewModel<AddEditTaskViewData>(), AddEditTaskFragment.OnEditTextChanged {

    override fun onTitleChanged(title: String) {
        viewData.value.title = title
    }

    override fun onDescriptionChanged(description: String) {
        viewData.value.description = description
    }

    override val initialViewData = AddEditTaskViewData(false, "", "", InvalidMessage)

    fun saveTask(title: String, description: String) {
        val task = createTask(title, description)
        if (task.isEmpty()) {
            showViewMessage(R.string.empty_task_message)
            return
        }
        tasksRepository.saveTask(task)
        addEditActions.onSaveTask()
    }

    open internal fun createTask(title: String, description: String) = Task(title, description)

    private fun showViewMessage(message: Int) {
        updateViewData(viewData.value.copy(message = ViewMessage(message)))
    }
}

class AddEditViewModel(private val taskId: String, tasksRepository: TasksRepository, addEditActions: AddEditActions) : AddViewModel(tasksRepository, addEditActions) {

    override val initialViewData = AddEditTaskViewData(true, "", "", InvalidMessage)

    init {
        //taskId is present load the task
        tasksRepository.getTask(taskId, object : TasksDataSource.GetTaskCallback {
            override fun onTaskLoaded(task: Task) {
                updateViewData(viewData.value.copy(title = task.title, description = task.description, dataLoading = false))
            }

            override fun onDataNotAvailable() {
                updateViewData(viewData.value.copy(dataLoading = false))
            }
        })
    }

    override fun createTask(title: String, description: String): Task {
        return Task(title, description, taskId)
    }
}