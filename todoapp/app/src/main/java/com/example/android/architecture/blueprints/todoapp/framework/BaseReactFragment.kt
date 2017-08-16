package com.example.android.architecture.blueprints.todoapp.framework

import android.arch.lifecycle.*
import android.os.Bundle
import android.os.Parcelable

open class FragmentCreator<T : Parcelable, F : BaseReactFragment<T, *, *>>(val fragmentClass : Class<F>) {
    fun newInstance(params : T) : F {
        return fragmentClass.newInstance().apply {
            initParams = params
        }
    }
}

abstract class BaseReactFragment<I : Parcelable, D, T : BaseReactViewModel<D>> : LifecycleFragment() {

    companion object {
        val INIT_PARAMS = "INIT_PARAMS"
    }

    protected lateinit var viewModel: T

    abstract protected fun applyViewData(viewData: D)

    var initParams : I
    get() = arguments.getParcelable<I>(INIT_PARAMS)
    set(value) {
        val bundle = Bundle()
        bundle.putParcelable(INIT_PARAMS, value)
        arguments = bundle
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this, object : ViewModelProvider.Factory {
            override fun <K : ViewModel?> create(modelClass: Class<K>?): K {
                @Suppress("UNCHECKED_CAST")
                return createViewModel() as K
            }
        }).get(getType())
        viewModel.onRestore(savedInstanceState)
        viewModel.viewData.observe(this, Observer {
            applyViewData(it!!)
        })
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    abstract fun createViewModel(): T

    abstract fun getType(): Class<T>

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        viewModel.onSave(outState)
    }
}