package com.example.android.architecture.blueprints.todoapp.util

import android.support.annotation.StringRes

data class ViewMessage(@StringRes val messageResId: Int, private var shown: Boolean = false) {
    fun markInvalid() {
        shown = true
    }

    fun isValid() = !shown
}

val InvalidMessage = ViewMessage(0, true)