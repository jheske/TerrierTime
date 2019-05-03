package com.heske.terriertime.network

import com.heske.terriertime.network.flickr.FlickrImageListResponse
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.Deferred
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

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

private const val FLICKR_BASE_URL = "https://api.flickr.com/services/feeds/"

/**
 * A public interface that exposes the [getProperties] method
 */
interface FlickrApiService {
    /**
     * Returns a Coroutine [Deferred] [List] which can be fetched with await() if
     * in a Coroutine scope.
     */

    /**
     * Download list of image paths related to Flickr search [tags]
     *  Sample  request
     *    https://api.flickr.com/services/feeds/photos_public.gne?tags=airedale&format=json
     */
    @GET("photos_public.gne?&format=json&nojsoncallback=1")
    fun getFlickImageList(@Query("tags") tags: String):
            Deferred<FlickrImageListResponse>
}

/**
 * A public Api object that exposes the lazy-initialized Retrofit service
 */
object FlickrApi {
    private val retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .baseUrl(FLICKR_BASE_URL)
        .build()

    val flickrService: FlickrApiService by lazy {
        retrofit.create(FlickrApiService::class.java)
    }
}

