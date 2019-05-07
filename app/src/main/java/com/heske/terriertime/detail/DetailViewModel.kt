package com.heske.terriertime.detail

import android.app.Application
import androidx.lifecycle.*
import com.heske.terriertime.database.TerriersTableEntity
import com.heske.terriertime.repositories.FlickrDataRepository
import com.heske.terriertime.repositories.WikiDataRepository
import com.heske.terriertime.terriers.Terrier
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
class DetailViewModel(
    private val terrierEntity: TerriersTableEntity,
    private val wikiRepository: WikiDataRepository,
    private val flickrRepository: FlickrDataRepository
) : ViewModel() {

    private val _terrier = MutableLiveData<TerriersTableEntity>()
        // This is bound to binding variable in fragment_detail.xml.
    val terrier: LiveData<TerriersTableEntity>
        get() = _terrier

    private val _terrierBreedName = MutableLiveData<String>()
    val terrierBreedName: LiveData<String>
        get() = _terrierBreedName

    val morePixString = Transformations.map(terrierBreedName) {
        "More " + it + "s"
    }

    private val _navigateToFlickrPix = MutableLiveData<String>()
    val navigateToFlickrPix: LiveData<String>
        get() = _navigateToFlickrPix

    init {
        _terrier.value = terrierEntity
        _terrierBreedName.value = terrierEntity.name
        // Populate the database with Flickr Urls.
        viewModelScope.launch {
            flickrRepository.deleteAll()
            flickrRepository.refreshFlickrkData()
        }
    }

    /**
     * When the more button is clicked,
     * set the [_navigateToFlickrPix] [MutableLiveData]
     * @param terrierBreedName The breed of the terrier [String].
     */
    fun displayFlickrPix(breedName: String) {
        _navigateToFlickrPix.value = breedName
    }

    fun displayFlickrPixComplete() {
        _navigateToFlickrPix.value = null
    }
}