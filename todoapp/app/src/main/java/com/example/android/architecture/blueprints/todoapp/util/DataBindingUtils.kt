package com.example.android.architecture.blueprints.todoapp.util

import android.databinding.BindingAdapter
import android.widget.ImageView

@BindingAdapter("android:src")
fun setImageViewResource(view: ImageView, resId : Int) {
    view.setImageResource(resId)
}