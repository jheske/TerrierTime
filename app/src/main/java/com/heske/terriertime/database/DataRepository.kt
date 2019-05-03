package com.heske.terriertime.database

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.heske.terriertime.network.WikiApi
import com.heske.terriertime.network.asDatabaseModel
import com.heske.terriertime.terriers.Terrier
import com.heske.terriertime.utils.TerrierBreeds
import com.heske.terriertime.utils.buildBreedTagString
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
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

/**
 * The repository the intermediary between the Network and the UI and the Database and the UI.
 * The UI tells the repository to retrieve data from either the Network or the Database, and return it
 * in a UI-friendly format.
 *
 * Network Request:
 *   Request data from a Service (Wiki or Flickr).
 *   Convert the response data to a format that can be added to the database.
 *   Add data to the database.
 *
 * Database Request:
 *   Query database for data. A Transformations.map maps the LiveData results to a "DomainModel",
 *   which is a UI-friendly list of pojos.
 */
class DataRepository(val terriersDao: TerriersDao) {
    private var terrierMap = TerrierBreeds.terriersMap
    private val terrierList = ArrayList(terrierMap.values).toTypedArray()
    private val terrierListSize = terrierList.size.toLong()
    private var rowCount: Long = 0

    class RepositoryRefreshError(cause: Throwable) : Throwable(cause.message, cause)

    /**
     * Map LiveData<List<DatabaseTerrier>> results from the database into
     * a list of Terrier pojos (List<Terrier>) for display.
     */
    val terriers: LiveData<List<Terrier>> =
        Transformations.map(terriersDao.getAllTerriers()) {
            it.asDomainModel()
        }

    suspend fun refreshData() {
        refreshLocalData()
        if (rowCount < terrierListSize) {
            refreshWikiData()
        }
    }

    /**
     * Initialize the database with the hardcoded data in the local TerrierBreeds HashMap.
     * If it's already there, then don't add it again.
     * This doesn't include summaries b/c those will requested from the Network later.
     */
    private suspend fun refreshLocalData() {
        withContext(Dispatchers.IO) {
            // How many terriers in the db?
            rowCount = terriersDao.getRowCount()
            // If there are fewer terriers than in the hardcoded list, then
            // re-initialize from the hardcoded data.
            if (rowCount != terrierListSize) {
                terriersDao.insertAll(*terrierList)
                Timber.d("There are ${terriersDao.getRowCount()} rows")
            }
        }
    }

    /**
     *
     * Get the latest data from the network and insert it into the database.
     * For now, check whether local data is already there.
     *
     * This function uses the IO dispatcher to ensure the database insert database operation
     * happens on the IO dispatcher. By switching to the IO dispatcher using `withContext` this
     * function is now safe to call from any thread including the Main thread.
     *
     * To actually load the videos for use, observe [videos]
     */
    private suspend fun refreshWikiData() {
        val terrierMap = TerrierBreeds.terriersMap
        val terrierList = ArrayList(terrierMap.values)

        /**
         * Network delivers a WikiSummaryListResponse. The database
         * expects a list of DatabaseTerrier objects.
         */
        withContext(Dispatchers.IO) {
            var summaryCount = terriersDao.getSummaryCount()
            if (summaryCount < rowCount) {
                refreshWikiData()
            }
            val resultList = WikiApi
                .wikiService
                .getWikiBreedSummaryList(buildBreedTagString(terrierList))
                .await()

            val summaries = resultList.asDatabaseModel()

            summaries.forEach {
                terriersDao.updateSummary(it.breed, it.summary)
            }
            summaryCount = terriersDao.getSummaryCount()
            Timber.d("There are ${summaryCount} after inserting summaries")
        }
    }

    suspend fun clearData() {
        withContext(Dispatchers.IO) {
            terriersDao.deleteAll()
            Timber.d("Database cleared")
        }
    }
}
