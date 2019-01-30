package com.heske.terriertime.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.heske.terriertime.TerrierBreeds
import com.heske.terriertime.database.BreedDao
import com.heske.terriertime.network.FlickrApi
import kotlinx.coroutines.*

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
/**
 * Model for asynchronously downloading Json data from the network using Retrofit2
 * Provides functions for calling Wikipedia and Flickr APIs
 */
/**
 * ViewModel for SleepTrackerFragment.
 */
class BreedViewModel(
    val breedTableDao: BreedDao,
    application: Application) : AndroidViewModel(application) {

    val breedList = ArrayList(TerrierBreeds.breedMap.values)
    private val breedListSize = breedList.size
    var breedCount = breedTableDao.getCount()

    /**
     * Coroutine vals for running db operations off UI thread.
     */

    // Allows us to cancel coroutines
    private var viewModelJob = Job()

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    // the Coroutine runs using the Main (UI) dispatcher
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main )

    /**
     * Response backing property plus its external val
     */
    // The internal MutableLiveData String that stores the most recent response
    private val _response = MutableLiveData<String>()
    // The external immutable LiveData for the response String
    val response: LiveData<String>
        get() = _response

    // BreedDao call returns a LiveData
    val breedCountString = Transformations.map(breedCount) {count ->
        formatBreedCount(count)
    }

    //val breeds = breedTableDao.getAllBreeds()

    init {
        uiScope.launch {
          //  getBreedCount()
            getFlickrImages()
            //  insertAll()
        }
    }

    private fun formatBreedCount(breedCount: Int?): String {
        Log.d("ViewModel","count = ${breedCount.toString()}")
        return (breedCount.toString())
    }

    /**
     * Sets the value of the response LiveData to the Mars API status or the successful number of
     * Mars properties retrieved.
     */
    private fun getFlickrImages() {
        coroutineScope.launch {
            // Get the Deferred object for our Retrofit request
            val getPropertiesDeferred
                    = FlickrApi.retrofitService.getFlickImageList("Airedale")
            try {
                // Await the completion of our Retrofit request
                val listResult = getPropertiesDeferred.await()
                _response.value = "Success: ${listResult.imageList.size} Images retrieved"
            } catch (e: Exception) {
                _response.value = "Failure: ${e.message}"
            }
        }
    }


//    private suspend fun insertAll() {
//        withContext(Dispatchers.IO) {
//            val rowCount = breedTableDao.getCount()
//            if (rowCount != breedListSize) {
//                breedTableDao.insertAll(breedList)
//            }
//        }
//    }

    private suspend fun getBreedCount(): Int {
        return withContext(Dispatchers.IO) {
            val count = breedTableDao.getCount()
            Log.d("ViewModel","count = $count")
        }
    }

    /**
     * A [CoroutineScope] keeps track of all coroutines started by this ViewModel.
     *
     * Because we pass it [viewModelJob], any coroutine started in this uiScope can be cancelled
     * by calling `viewModelJob.cancel()`
     *
     * By default, all coroutines started in uiScope will launch in [Dispatchers.Main] which is
     * the main thread on Android. This is a sensible default because most coroutines started by
     * a [ViewModel] update the UI after performing some processing.
     */
//   private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

//   private var tonight = MutableLiveData<Breed?>()

//    init {
//        initializeTonight()
//    }

//    private fun initializeTonight() {
//        uiScope.launch {
//            tonight.value = getTonightFromDatabase()
//        }
//    }

    /**
     *  Handling the case of the stopped app or forgotten recording,
     *  the start and end times will be the same.j
     *
     *  If the start time and end time are not the same, then we do not have an unfinished
     *  recording.
     */
//    private suspend fun getBreedFromDatabase(): Breed? {
//        return withContext(Dispatchers.IO) {
//            var night = breedTable.getTonight()
//
//            night
//        }
//    }

//    private suspend fun insert(night: SleepNight) {
//        withContext(Dispatchers.IO) {
//            breedTable.insert(night)
//        }
//    }
//
//    private suspend fun update(night: SleepNight) {
//        withContext(Dispatchers.IO) {
//            breedTable.update(night)
//        }
//    }

//    private suspend fun clear() {
//        withContext(Dispatchers.IO) {
//            breedTable.clear()
//        }
//    }

    /**
     * Executes when the CLEAR button is clicked.
     */
//    fun onClear() {
//        uiScope.launch {
//            // Clear the database table.
//            clear()
//
//            // And clear tonight since it's no longer in the database
//            tonight.value = null
//        }
//    }

    /**
     * Called when the ViewModel is dismantled.
     * At this point, we want to cancel all coroutines;
     * otherwise we end up with processes that have nowhere to return to
     * using memory and resources.
     */
    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }


}