package com.heske.terriertime.utils

import android.app.Application
import android.content.Context
import com.heske.terriertime.TerrierApp
import com.heske.terriertime.database.TerriersTableEntity
import com.heske.terriertime.database.getDatabase
import com.heske.terriertime.detail.DetailViewModelFactory
import com.heske.terriertime.flickr.FlickrViewModelFactory
import com.heske.terriertime.repositories.FlickrDataRepository
import com.heske.terriertime.repositories.WikiDataRepository
import com.heske.terriertime.terriers.Terrier

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
object InjectorUtils {
    private fun getFlickrRepository(context: Context): FlickrDataRepository {
        return FlickrDataRepository.getInstance(
            "Airedale Terrier",
            getDatabase(context.applicationContext).flickrTableDao
        )
    }

    fun provideFlickrViewModelFactory(
        context: Context,
        application: Application
    ): FlickrViewModelFactory {
        val repository = getFlickrRepository(context)
        return FlickrViewModelFactory(repository, application)
    }

    private fun getWikiRepository(context: Context): WikiDataRepository {
        return WikiDataRepository.getInstance(
            getDatabase(context.applicationContext).terriersTableDao
        )
    }

    fun provideDetailViewModelFactory(
        terrierEntity: TerriersTableEntity,
        context: Context
    ): DetailViewModelFactory {
        val wikiRepository = getWikiRepository(context)
        val flickrRepository = getFlickrRepository(context)
        return DetailViewModelFactory(terrierEntity,wikiRepository,flickrRepository)
    }
}