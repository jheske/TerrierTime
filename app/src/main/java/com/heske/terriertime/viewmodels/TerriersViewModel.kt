package com.heske.terriertime.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bumptech.glide.Glide.init
import com.heske.terriertime.database.Terrier
import com.heske.terriertime.database.TerriersDao
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
 * ViewModel for TerriersFragment.
 */
class TerriersViewModel(
    terriersTableDao: TerriersDao,
    application: Application
) : AndroidViewModel(application) {

 //   private val _listOfTerriers = MutableLiveData<List<Terrier>>()

    /**
     * terrier_recycler listOfTerriers attribute has a data binding to
     * this LiveData, so it gets reset whenever the LiveData changes.
     */
    val listOfTerriers = terriersTableDao.getAllTerriers()
}