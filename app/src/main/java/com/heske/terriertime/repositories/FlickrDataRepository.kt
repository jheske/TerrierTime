package com.heske.terriertime.repositories

import com.heske.terriertime.database.FlickrDao
import com.heske.terriertime.network.FlickrApi
import com.heske.terriertime.network.asDatabaseModel
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
class FlickrDataRepository(
    val breedName: String,
    val flickrDao: FlickrDao
) {
    var rowCount = 0L

    class RepositoryRefreshError(cause: Throwable) : Throwable(cause.message, cause)

    companion object {
        // For Singleton instantiation
        @Volatile
        private var instance: FlickrDataRepository? = null

        fun getInstance(breedName: String, flickrDao: FlickrDao) =
            instance ?: synchronized(this) {
                instance ?: FlickrDataRepository(breedName, flickrDao).also { instance = it }
            }
    }

    fun getFlickrImageUrls() = flickrDao.getImageUrls(breedName)


    suspend fun deleteAll() {
        withContext(Dispatchers.IO) {
            flickrDao.deleteAll()
            val rowCount = flickrDao.getRowCount()
            Timber.d("There are $rowCount rows")
        }
    }

    suspend fun getRowCount() {
        withContext(Dispatchers.IO) {
             rowCount = flickrDao.getRowCount()
            Timber.d("There are $rowCount rows")
        }
    }

    // TODO Retrieve refresh in a Worker
    //val request= OneTimeWorkRequestBuilder<DatabaseWorker>().build()
    //WorkManager.getInstance(context).enqueue(request)
    suspend fun refreshFlickrkData() {
        withContext(Dispatchers.IO) {
            try {
                // Request a list of Urls from Flickr
                // Await the completion of Retrofit request
                val resultsList = FlickrApi
                    .flickrService
                    .getFlickImageList(breedName)
                    .await()

                val flickrUrls = resultsList.asDatabaseModel(breedName)

                if (resultsList.imageList.size > 0) {
                    flickrDao.insertAll(*flickrUrls)
                }
            } catch (e: Exception) {
                // _status.value = "Failure: ${e.message}"
            }
        }
    }
}