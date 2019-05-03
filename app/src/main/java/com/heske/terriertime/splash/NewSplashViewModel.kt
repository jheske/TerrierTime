package com.heske.terriertime.splash

import android.app.Application
import androidx.lifecycle.*
import com.heske.terriertime.database.DataRepository
import com.heske.terriertime.database.getDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import timber.log.Timber


/* Copyright (c) 2019 Jill Heske All rights reserved.
 *
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
class NewSplashViewModel(application: Application) : AndroidViewModel(application) {
    enum class ApiStatus { LOADING, ERROR, DONE }

    // The internal MutableLiveData that stores the status of the most recent request
    private val _status = MutableLiveData<ApiStatus>()

    /**
     * Request a snackbar to display a string.
     */
    private var _snackBar = MutableLiveData<String>()
    val snackBar: LiveData<String>
        get() = _snackBar

    private val _spinner = MutableLiveData<Boolean>()
    val spinner: LiveData<Boolean>
        get() = _spinner

    // The external immutable LiveData for the request status
    val status: LiveData<ApiStatus>
        get() = _status

    private val database = getDatabase(application)
    private val dataRepository = DataRepository(database.terriersDatabaseDao)

    init {
        launchDataLoad {
            dataRepository.clearData()
            dataRepository.refreshData()

        }
    }

    private fun launchDataLoad(block: suspend () -> Unit): Job {
        return viewModelScope.launch {
            try {
                _spinner.value = true
                block()
            } catch (error: DataRepository.RepositoryRefreshError) {
                _snackBar.value = error.message
            } finally {
                _spinner.value = false
            }
        }
    }

    /**
     * Called immediately after the UI shows the snackbar.
     */
    fun onSnackbarShown() {
        _snackBar.value = null
    }

//    suspend fun makeNetworkRequest() {
//        // save is a regular function and will block this thread
//        database.save(slow, another)
//        // viewModelScope is built into ViewModel. It is bound to
//        // Dispatchers.Main and handles onCleared().
//        viewModelScope.launch(Dispatchers.IO) {
//            val slow = slowFetch()
//            val another = anotherFetch()
//        }
//    }
//
//    suspend fun slowFetch(): Get { ... }
//    suspend fun anotherFetch(): AnotherResult { ... }
}