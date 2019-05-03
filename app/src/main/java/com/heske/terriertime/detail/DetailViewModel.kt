package com.heske.terriertime.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.heske.terriertime.database.DatabaseTerrier

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
class DetailViewModel(terrierDetails: DatabaseTerrier) : ViewModel() {
    private val _terrier = MutableLiveData<DatabaseTerrier>()

    val terrier: LiveData<DatabaseTerrier>
        get() = _terrier

    private val _terrierBreedName = MutableLiveData<String>()
    val terrierBreedName: LiveData<String>
        get() = _terrierBreedName

    val morePixString = Transformations.map(terrierBreedName) {
        "More " + it +"s"
    }

    private val _navigateToFlickrPix = MutableLiveData<String>()
    val navigateToFlickrPix: LiveData<String>
        get() = _navigateToFlickrPix

    init {
        _terrier.value = terrierDetails
        _terrierBreedName.value = terrierDetails.name
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