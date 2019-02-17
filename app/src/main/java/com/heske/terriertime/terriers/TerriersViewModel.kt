package com.heske.terriertime.terriers

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.heske.terriertime.database.Terrier
import com.heske.terriertime.database.TerriersDao

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
 * ViewModel for TerriersFragment.
 */
class TerriersViewModel(
    terriersTableDao: TerriersDao,
    application: Application
) : AndroidViewModel(application) {

    private val _correctGuess = MutableLiveData<Terrier>()
    val correctGuess: LiveData<Terrier>
        get() = _correctGuess

    private val _incorrectGuess = MutableLiveData<String>()
    val incorrectGuess: LiveData<String>
        get() = _incorrectGuess

    private val _navigateToFullsizeImage = MutableLiveData<Terrier>()
    val navigateToFullsizeImage: LiveData<Terrier>
        get() = _navigateToFullsizeImage

    private val _navigateToDetailScreen = MutableLiveData<Terrier>()
    val navigateToDetailScreen: LiveData<Terrier>
        get() = _navigateToDetailScreen

    /**
     * terrier_recycler listOfTerriers attribute has a data binding to
     * this LiveData, so it gets reset whenever
     * the LiveData changes (when getAllTerriers() retrieves it from the database).
     */
    val listOfTerriers = terriersTableDao.getAllTerriers()

    /**
     * When the terrier image is clicked,
     * set the [_navigateToFullsizeImage] [MutableLiveData]
     * @param terrier The [Terrier] that was clicked on.
     */
    fun displayFullsizeImage(terrier: Terrier) {
        _navigateToFullsizeImage.value = terrier
    }

    /**
     * After the navigation has taken place, make sure displayFullsizeImageComplete is set to null.
     * !!!!Otherwise the app will crash when Back button is pressed from destination Fragment!!!!
     */
    fun displayFullsizeImageComplete() {
        _navigateToFullsizeImage.value = null
    }

    /**
     * When the terrier image is clicked,
     * set the [_navigateToFullsizeImage] [MutableLiveData]
     * @param terrier The [Terrier] that was clicked on.
     */
    fun displayDetailScreen(terrier: Terrier) {
        _navigateToDetailScreen.value = terrier
    }

    /**
     * After the navigation has taken place, make sure displayFullsizeImageComplete is set to null.
     * !!!!Otherwise the app will crash when Back button is pressed from destination Fragment!!!!
     */
    fun displayDetailScreenComplete() {
        _navigateToDetailScreen.value = null
        _correctGuess.value = null
    }

    fun guessComplete() {
        _incorrectGuess.value = null
    }

    /**
     * If user's guess is correct, then send the [Terrier] back for display.
     * Otherwise send null so [Observer] can provide user feedback.
     */
    fun processGuess(terrier: Terrier,guessText: String) {
        // Everything lower case for easy comparison
        val breedName = terrier.name.toLowerCase()
        val guess = guessText.toLowerCase()
        // Append "terrier" to breedName in case user forgot to type it
        val guessPlusTerrier = "${guess} terrier"
        if ((guess.equals(breedName)) || (guessPlusTerrier.equals(breedName))) {
            _correctGuess.value = terrier
        } else {
            _incorrectGuess.value = guessText
        }
    }
}