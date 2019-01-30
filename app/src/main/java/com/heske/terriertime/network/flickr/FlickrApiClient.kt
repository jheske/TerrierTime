/*
 * Created by Jill Heske on 8/20/2018
 * Copyright (c) All rights reserved
 */

package com.heske.terriertime.network.flickr

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Retrofit client for making Flickr requests. Include an HTTPClient for
 * setting timeouts and logging to use optionally for debugging.
 * Include debug options in debug flavor of the app only.
 *
 * Sample flickr request
 *  https://api.flickr.com/services/feeds/photos_public.gne?tags=airedale&format=json
 */
class FlickrApiClient {
    companion object {
        val baseURL = "https://api.flickr.com/services/feeds/"
        var retofit: Retrofit? = null

        val client: Retrofit
            get() {
                /** Interceptor is for debugging, doesn't belong in production */
                val interceptor = HttpLoggingInterceptor()
                interceptor.level = HttpLoggingInterceptor.Level.BODY

                /**
                 * Set timeout in case the service is unavailable or
                 * hangs for some reason
                 */
                val httpClient = OkHttpClient.Builder()
                        .connectTimeout(10, TimeUnit.SECONDS)
                        .readTimeout(10, TimeUnit.SECONDS)
                        .build()

                if (retofit == null) {
                    retofit = Retrofit.Builder()
                            .baseUrl(baseURL)
                            .addConverterFactory(GsonConverterFactory.create())
                            .client(httpClient)
                            .build()
                }
                return retofit!!
            }
    }
}