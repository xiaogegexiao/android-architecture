package com.example.android.architecture.blueprints.todoapp.framework

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModel
import android.os.Bundle
import android.os.Parcelable
import com.here.jumper.extensions.MutableSafeLiveData
import com.here.jumper.extensions.SafeLiveData

private const val KEY_VIEW_DATA = "KEY_VIEW_DATA"

abstract class BaseReactViewModel<T> : ViewModel() {

    private class Attacher<A>(val lifeData: LiveData<A>, val observer: Observer<A>) {
        fun attach() {
            lifeData.observeForever(observer)
        }

        fun detach() {
            lifeData.removeObserver(observer)
        }
    }

    abstract val initialViewData: T
    private val attachedLifeData: MutableMap<LiveData<*>, Attacher<*>> = mutableMapOf()

    val viewData: SafeLiveData<T> by lazy {
        MutableSafeLiveData(initialViewData)
    }

    protected fun updateViewData(newViewData: T) {
        (viewData as MutableSafeLiveData<T>).value = newViewData
    }

    fun <D, L : LiveData<D>, O : Observer<D>> attachObserverToLifecycle(liveData: L, observer: O) {
        if (attachedLifeData.containsKey(liveData)) {
            detachObserverFromLifecycle(liveData)
        }
        attachedLifeData.put(liveData, Attacher(liveData, observer))
        attachedLifeData[liveData]!!.attach()
    }

    fun <D, L : LiveData<D>> detachObserverFromLifecycle(liveData: L) {
        attachedLifeData.remove(liveData)?.detach()
    }


    override fun onCleared() {
        attachedLifeData.forEach({ (_, attacher) ->
            attacher.detach()
        })
        attachedLifeData.clear()
        super.onCleared()
    }

    open fun onSave(outBundle: Bundle) {
        if (viewData.value is Parcelable) {
            outBundle.putParcelable(KEY_VIEW_DATA, viewData.value as Parcelable)
        }
    }

    open fun onRestore(inBundle: Bundle?) {
        inBundle?.let {
            val parcelable = it.getParcelable<Parcelable>(KEY_VIEW_DATA)
            parcelable?.let {
                @Suppress("UNCHECKED_CAST")
                updateViewData(it as T)
            }
        }
    }
}