/*
 * Copyright 2017 Google, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package dadoufi.musicmanager.extensions

import android.os.Handler
import androidx.core.os.postDelayed
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer
import java.util.*

inline fun <T> LiveData<T>.observeK(owner: LifecycleOwner, crossinline observer: (T?) -> Unit) {
    this.observe(owner, Observer { observer(it) })
}

inline fun <T> LiveData<T>.observeForeverK(crossinline observer: (T?) -> Unit) {
    this.observeForever { observer(it) }
}


fun <T> LiveData<T>.reObserveForever(observer: Observer<T>) {
    removeObserver(observer)
    observeForever(observer)

}

fun <T> LiveData<T>.reObserve(owner: LifecycleOwner, observer: Observer<T>) {
    removeObserver(observer)
    observe(owner, observer)

}


inline fun <T> LiveData<T>.observeNotNull(
    owner: LifecycleOwner,
    crossinline observer: (T) -> Unit
) {
    this.observe(owner, Observer { observer(it!!) })
}

fun <T> LiveData<T>.distinctUntilChanged(): LiveData<T> {
    val distinctLiveData = MediatorLiveData<T>()
    distinctLiveData.addSource(this, object : Observer<T> {
        private var initialized = false
        private var lastObj: T? = null

        override fun onChanged(obj: T?) {
            if (!initialized) {
                initialized = true
                lastObj = obj
                distinctLiveData.postValue(lastObj)
            } else if (!Objects.equals(lastObj, obj)) {
                lastObj = obj
                distinctLiveData.postValue(lastObj)
            }
        }
    })
    return distinctLiveData
}


fun <T> LiveData<T>.distinctUntilChangedNonEmptyList(): LiveData<T> {
    val distinctLiveData = MediatorLiveData<T>()
    distinctLiveData.addSource(this, object : Observer<T> {
        private var initialized = false
        private var lastObj: T? = null

        override fun onChanged(obj: T?) {
            if (obj is List<*> && obj.isNotEmpty()) {
                if (!initialized) {
                    initialized = true
                    lastObj = obj
                    distinctLiveData.postValue(lastObj)
                } else if (!Objects.equals(lastObj, obj)) {
                    lastObj = obj
                    distinctLiveData.postValue(lastObj)
                }
            }
        }
    })
    return distinctLiveData
}

fun <T> LiveData<T>.distinctUntilChangedDebounceEmpty(delay: Long): LiveData<T> {
    val distinctLiveData = MediatorLiveData<T>()
    distinctLiveData.addSource(this, object : Observer<T> {
        private var initialized = false
        private var lastObj: T? = null

        private val handler = Handler()
        private var runnable: Runnable? = null

        override fun onChanged(obj: T?) {
            if (runnable != null) {
                handler.removeCallbacks(runnable)
            }
            if (!initialized) {

                initialized = true
                lastObj = obj
                if (obj is List<*> && obj.isEmpty()) {
                    runnable = Runnable { distinctLiveData.postValue(lastObj) }
                    handler.postDelayed(delay) {
                        //runnable
                        distinctLiveData.postValue(lastObj)
                    }
                } else {
                    distinctLiveData.postValue(lastObj)
                }


                //distinctLiveData.postValue(lastObj)
            } else if (!Objects.equals(lastObj, obj)) {
                lastObj = obj
                distinctLiveData.postValue(lastObj)
            }
        }
    })
    return distinctLiveData
}






