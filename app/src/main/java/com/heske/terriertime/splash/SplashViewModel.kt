package com.heske.terriertime.splash

import android.app.Application
import android.os.Handler
import android.util.Log
import androidx.core.os.postDelayed
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.heske.terriertime.database.Terrier
import com.heske.terriertime.database.TerriersDao
import com.heske.terriertime.network.WikiApi
import com.heske.terriertime.network.wiki.WikiBreedSummaryItem
import com.heske.terriertime.utils.TerrierBreeds
import com.heske.terriertime.utils.buildBreedTagString
import com.heske.terriertime.utils.isNetworkConnected
import kotlinx.coroutines.*
import android.widget.Toast
import com.heske.terriertime.MainActivity




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
 * The splash screen displays before MainActivity is called.
 * It should only be shown for as long as it takes for the app to download data.
 * However, if there is no network connection, keep the Splash screen visible
 * anyway for SPLASH_DISPLAY_LENGTH seconds.
 *
 * If network data load fails (likely b/c the app doesn't have internet access),
 * go directly to the next activity and load data from local storage.
 * Hopefully it's all there from a prior run of the app with
 * the network available. If not, load data from a hardcoded local object.
 *
 * A SettingsActivity could offer the opportunity to try to
 * download the data if the network becomes available,
 * without having to restart the app.
 */
class SplashViewModel(
    application: Application,
    val terrierTableDao: TerriersDao
) : AndroidViewModel(application) {

    private val TAG = SplashViewModel::class.java.simpleName

    companion object {
        private val SPLASH_DISPLAY_LENGTH = 1000L
    }

    private var handler: Handler? = Handler()
    private val closeSplashScreen = Runnable { closeSplashScreen() }

    private var terrierMap = TerrierBreeds.terriersMap
    private val terrierList = ArrayList(terrierMap.values)
    private val terrierListSize = terrierList.size

    /** Coroutines vars **/

    // Allows us to cancel coroutines
    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    // Event for closing the Splash screen
    private val _eventCloseSplashScreen = MutableLiveData<Boolean>()
    val eventCloseSplashScreen: LiveData<Boolean>
        get() = _eventCloseSplashScreen

    // Internally, we use a MutableLiveData to handle navigation to the selected property
    private val _navigateToTerriers = MutableLiveData<Terrier>()

    // The external immutable LiveData for the navigation property
    val navigateToTerriers: LiveData<Terrier>
        get() = _navigateToTerriers

    init {
        if (application.isNetworkConnected()) {
            //Display Splash screen for a few seconds before
            //navigating to TerriersFragment
            uiScope.launch {
                //Clear the db just for testing
                dbDeleteAll()
                initializeDatabase(dbGetTableRowCount(), dbGetSummaryCount())
                handler?.postDelayed(closeSplashScreen,
                    SPLASH_DISPLAY_LENGTH
                )
                //closeSplashScreen()
            }
        } else {
            handler?.postDelayed(closeSplashScreen,
                SPLASH_DISPLAY_LENGTH
            )
        }
    }

    /**
     * Hide the progress wheel and move on to MainActivity.
     * This can probably be handled by ??? navigation ???
     */
    private fun closeSplashScreen() {
        _eventCloseSplashScreen.value = true
    }

    // Called from Fragment to tell the ViewModel we've made the navigate call,
    // to prevent multiple navigation.
    // !!!!!Otherwise app will crash when Back button is clicked from destination fragment!!!!!
    fun onEventCloseSplashScreenComplete() {
        _eventCloseSplashScreen.value = false
    }

    private suspend fun dbGetTableRowCount(): Int {
        return withContext(Dispatchers.IO) {
            terrierTableDao.getRowCount()
        }
    }

    private suspend fun dbGetSummaryCount(): Int {
        return withContext(Dispatchers.IO) {
            terrierTableDao.getSummaryCount()
        }
    }

    /**
     * Clear the database, used for testing only.
     */
    private suspend fun dbDeleteAll() {
        withContext(Dispatchers.IO) {
            terrierTableDao.deleteAll()
            Log.d(TAG, "Database cleared")
        }
    }

    /**
     * Verify that all breeds and their Wiki summaries are in the database.
     * If they aren't, then (retrofit) download from wiki.
     */
    private suspend fun initializeDatabase(tableRowCount: Int, tableSummaryCount: Int) {
        /**
         * If all the breeds are not in the database, then initialize the
         * database with the hardcoded data in the TerrierBreeds HashMap
         */
        if (tableRowCount != terrierListSize) {
            dbInsertAll()
        }

        /**
         * Now check whether all the summaries were inserted into the database
         * (would have been done during a prior run of the app).
         * If rowCount is null or 0, then download data from Wiki and insert them.
         */
        if (tableSummaryCount < terrierListSize) {
            downloadWikiSummaries()
        }
    }

    private suspend fun dbInsertAll() {
        withContext(Dispatchers.IO) {
            terrierTableDao.insertAll(terrierList)
        }
    }

    /**
     * Download summaries and add them to the database.
     *
     * Retrofit already does all its work on a background thread by design, we can
     * launch the job from uiScope, which is the the Main thread.
     *
     * The retrofitService.getWikiBreedSummaryList call returns a "Deferred",
     * which means it returns immediately, and lets
     * retrofit continue its work on the background thread without blocking.
     * A Deferred is a type of Coroutine Job that can directly return a result.
     * Deferred has to be run inside a Coroutine (see initializeDatabase).
     * Because it's a Job, we can cancel it at any time.
     *
     * Call await() on the Deferred return value.
     * This allows us to await a result "synchronously",
     * but still avoid blocking the main thread.
     */
    private suspend fun downloadWikiSummaries() {
        // Get the Deferred object for our Retrofit request
        Log.d(TAG, "[loadWikiSummaries] " + "Download summaries")

        val getPropertiesDeferred = WikiApi.retrofitService
            .getWikiBreedSummaryList(buildBreedTagString(terrierList))
        try {
            // Await the completion of our Retrofit request.
            val listResult = getPropertiesDeferred.await()
            val listSize = listResult.query.summaryList.size

            if (listSize > 0) {
                val wikiBreedSummaryList = listResult.query.summaryList
                Log.d(TAG, "[loadWikiSummaries] " + "listSize = $listSize")
                dbUpdateSummaries(ArrayList(wikiBreedSummaryList.values))
            }
            //_status.value = "Success: ${listSize} items retrieved"
            if (listSize > 0) {
                Log.d(TAG, "[loadWikiSummaries] " + "There are $listSize summaries")
            }
        } catch (e: Exception) {
            //_status.value = "Failure: ${e.message}"
        }
    }

    /**
     * Insert the summary from each terrierBreed in wikiSummaryList
     * into the database
     */
    private suspend fun dbUpdateSummaries(wikiSummaryList: ArrayList<WikiBreedSummaryItem>) {
        val terriersMap = TerrierBreeds.terriersMap
        wikiSummaryList.forEachIndexed { index, wikiListItem ->
            if (terriersMap.contains(wikiListItem.breed)) {
                Log.d(TAG, "Updating ${wikiListItem.breed}")
                withContext(Dispatchers.IO) {
                    val rowCount = terrierTableDao.updateSummary(
                        breedName = wikiListItem.breed,
                        summary = wikiListItem.summary
                    )
                    Log.d(TAG, "RowCount = $rowCount")
                }
            } else {
                Log.d(TAG, "Can't find ${wikiListItem.breed}")
            }
        }
    }

}