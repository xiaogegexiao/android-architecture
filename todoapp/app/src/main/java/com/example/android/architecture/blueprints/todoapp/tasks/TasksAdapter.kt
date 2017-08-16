/*
 *  Copyright 2017 Google Inc.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.example.android.architecture.blueprints.todoapp.tasks

import android.databinding.DataBindingUtil
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.CheckBox

import com.example.android.architecture.blueprints.todoapp.data.Task
import com.example.android.architecture.blueprints.todoapp.databinding.TaskItemBinding


interface TasksItemActions {
    fun onTaskChecked(task: Task, checked: Boolean)
    fun onTaskClicked(task: Task)
}

class TasksAdapter(tasks: List<Task>,
                   private val mTasksItemActions: TasksItemActions) : BaseAdapter() {

    var tasks: List<Task> = tasks
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getCount(): Int {
        return tasks.count()
    }

    override fun getItem(position: Int): Task {
        return tasks[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, view: View?, viewGroup: ViewGroup): View {
        val binding: TaskItemBinding
        if (view == null) {
            // Inflate
            val inflater = LayoutInflater.from(viewGroup.context)

            // Create the binding
            binding = TaskItemBinding.inflate(inflater, viewGroup, false)
        } else {
            // Recycling view
            binding = DataBindingUtil.getBinding<TaskItemBinding>(view)
        }

        val userActionsListener = object : TaskItemUserActionsListener {
            override fun onCompleteChanged(task: Task, v: View) {
                val checked = (v as CheckBox).isChecked
                mTasksItemActions.onTaskChecked(task, checked)
            }

            override fun onTaskClicked(task: Task) {
                mTasksItemActions.onTaskClicked(task)
            }
        }

        binding.task = tasks[position]

        binding.listener = userActionsListener

        binding.executePendingBindings()
        return binding.root
    }
}
