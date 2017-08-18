/*
 * Copyright 2016, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.architecture.blueprints.todoapp.addedittask

import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.android.architecture.blueprints.todoapp.DependencyProvider
import com.example.android.architecture.blueprints.todoapp.R
import com.example.android.architecture.blueprints.todoapp.databinding.AddtaskFragBinding
import com.example.android.architecture.blueprints.todoapp.framework.BaseReactFragment
import com.example.android.architecture.blueprints.todoapp.framework.FragmentCreator
import com.example.android.architecture.blueprints.todoapp.main.MainActivityDependencies
import com.example.android.architecture.blueprints.todoapp.util.SnackbarUtils
import kotlinx.android.synthetic.main.addtask_frag.*

/**
 * Main UI for the add task screen. Users can enter a task title and description.
 */
class AddEditTaskFragment : BaseReactFragment<Bundle, AddEditTaskViewData, AddViewModel>() {

    interface OnEditTextChanged {
        fun onTitleChanged(title : String)
        fun onDescriptionChanged(description: String)
    }

    private lateinit var mViewDataBinding: AddtaskFragBinding

    override fun getType(): Class<AddViewModel> = AddViewModel::class.java

    @Suppress("UNCHECKED_CAST")
    override fun createViewModel(): AddViewModel {
        val dependencies = (activity as DependencyProvider<MainActivityDependencies>).dependencies
        val charSequence = initParams.getCharSequence(ARGUMENT_EDIT_TASK_ID)

        if (charSequence != null) {
            return AddEditViewModel(charSequence.toString(), dependencies.taskRepository(), dependencies.addEditActions())
        }
        return AddViewModel(dependencies.taskRepository(), dependencies.addEditActions())
    }

    override fun applyViewData(viewData: AddEditTaskViewData) {
        mViewDataBinding.viewmodel = viewData
        SnackbarUtils.showMessage(view, viewData.message)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        mViewDataBinding.listener = viewModel
        setupFab()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.addtask_frag, container, false)
        mViewDataBinding = AddtaskFragBinding.bind(root)

        setHasOptionsMenu(true)
        retainInstance = false

        return mViewDataBinding.root
    }

    private fun setupFab() {
        val fab = activity.findViewById<View>(R.id.floating_action_button) as FloatingActionButton
        fab.setImageResource(R.drawable.ic_done)
        fab.setOnClickListener {
            viewModel.saveTask(add_task_title.text.toString(), add_task_description.text.toString())
        }
    }

    companion object : FragmentCreator<Bundle, AddEditTaskFragment>(AddEditTaskFragment::class.java) {
        private val ARGUMENT_EDIT_TASK_ID = "EDIT_TASK_ID"

        fun newInstance() = newInstance(Bundle())
        fun newInstance(taskId: String) = newInstance(Bundle().apply {
            putCharSequence(ARGUMENT_EDIT_TASK_ID, taskId)
        })
    }
}