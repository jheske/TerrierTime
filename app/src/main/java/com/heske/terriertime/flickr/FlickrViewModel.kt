package com.heske.terriertime.flickr

import androidx.lifecycle.*
import com.heske.terriertime.network.FlickrApi
import com.heske.terriertime.network.flickr.FlickrImageItem
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
class FlickrViewModel(breedName: String): ViewModel() {

    /** Coroutine vals for running db operations off UI thread. */

    // Allows us to cancel coroutines
    private var viewModelJob = Job()

    // the Coroutine runs using the Main (UI) dispatcher
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    /** Response backing properties and their external vals */

    // The internal MutableLiveData String that stores the most recent response
    private val _status = MutableLiveData<String>()
    // The external immutable LiveData for the response String
    val status: LiveData<String>
        get() = _status

    private val _imageUrlList = MutableLiveData<List<String>>()
    val imageUrlList: LiveData<List<String>>
        get() = _imageUrlList

    init {
        uiScope.launch {
            // List of pix on Flickr can change at any time.
            getFlickrImages(breedName)
        }
    }

    /**
     * Sets the value of the response LiveData to the Mars API status or the successful number of
     * Mars properties retrieved.
     */
    private fun getFlickrImages(breedName: String) {
        uiScope.launch {
            // Get the Deferred object for our Retrofit request
            val getPropertiesDeferred
                    = FlickrApi.retrofitService.getFlickImageList(breedName)
            try {
                // Await the completion of our Retrofit request
                val listResult = getPropertiesDeferred.await()

                _status.value = "Success: ${listResult.imageList.size} Images retrieved"
                if (listResult.imageList.size > 0) {
                    _imageUrlList.value = getImageUrlList(listResult.imageList)
                }
            } catch (e: Exception) {
                _status.value = "Failure: ${e.message}"
            }
        }
    }

    /***
     * Return an ArrayList containing string values from [flickrImageList] item
     * media field.
     */
    fun getImageUrlList(flickrImageList: ArrayList<FlickrImageItem>): ArrayList<String> {
        val imageList = ArrayList<String>()

        flickrImageList.forEachIndexed { index, flickrListItem ->
            val imageFileName = flickrListItem.media.getValue("m")
            imageList.add(imageFileName)
        }
        return imageList
    }
}