package com.heske.terriertime.flickr

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.heske.terriertime.database.FlickrDao
import com.heske.terriertime.database.FlickrTableEntity
import com.heske.terriertime.repositories.FlickrDataRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

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
class FlickrViewModel(
    breedName: String,
    flickrDao: FlickrDao,
    application: Application
) : AndroidViewModel(application) {

    private val _navigateToFullsizeImage = MutableLiveData<String>()
    val navigateToFullsizeImage: LiveData<String>
        get() = _navigateToFullsizeImage

    private val dataRepository = FlickrDataRepository(breedName,flickrDao)

    val listOfFlickrImageUrls = flickrDao.getImageUrls(breedName)

    init {
        launchDataLoad {
            dataRepository.refreshFlickrkData(breedName)
        }
    }

    private fun launchDataLoad(block: suspend () -> Unit): Job {
        return viewModelScope.launch {
            try {
                //_spinner.value = true
                block()
            } catch (error: FlickrDataRepository.RepositoryRefreshError) {
                // _snackBar.value = error.message
            } finally {
                //  _spinner.value = false
            }
        }
    }

    /**
     * When the terrier image is clicked,
     * set the [_navigateToFullsizeImage] [MutableLiveData]
     * @param terrier The [terrier] that was clicked on.
     */
    fun displayFullsizeImage(breed: String) {
        _navigateToFullsizeImage.value = breed
    }

    /**
     * After the navigation has taken place, make sure displayFullsizeImageComplete is set to null.
     * !!!!Otherwise the app will crash when Back button is pressed from destination Fragment!!!!
     */
    fun displayFullsizeImageComplete() {
        _navigateToFullsizeImage.value = null
    }

    /**
     * Sets the value of the response LiveData to the API status or the successful number of
     * objects retrieved.
     */
//    private fun getFlickrImages(breedName: String) {
//        uiScope.launch {
//            // Get the Deferred object for our Retrofit request
//            val getPropertiesDeferred
//                    = FlickrApi.flickrService.getFlickImageList(breedName)
//            try {
//                // Await the completion of our Retrofit request
//                val listResult = getPropertiesDeferred.await()
//
//                _status.value = "Success: ${listResult.imageList.size} Images retrieved"
//                if (listResult.imageList.size > 0) {
//                    _imageUrlList.value = getImageUrlList(listResult.imageList)
//                }
//            } catch (e: Exception) {
//                _status.value = "Failure: ${e.message}"
//            }
//        }
//    }
}