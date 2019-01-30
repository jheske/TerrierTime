package com.heske.terriertime.network.flickr

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

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
private const val BASEURL = "https://m ars.udacity.com/"

// A public interface that exposes the api call methods needed
interface WikiApiService {

}

// Use the Retrofit builder to build a retrofit object
// using a Moshi converter and a Coroutine adapter
// pointing to the desired url
private val retrofit = Retrofit.Builder()
    .addConverterFactory(GsonConverterFactory.create())
    //.addCallAdapterFactory(CoroutineCallAdapterFactory())
    .baseUrl(BASEURL)
    .build()

// A public object expression that exposes the retrofit object called "mars"
// using a delayed initialization that creates the retrofit object only when needed
object Api {
    val mars by lazy { retrofit.create(WikiApiService::class.java) }
}




/**
 * Retrofit client for making wikipedia.org requests. Include an HTTPClient for
 * setting timeouts and logging to use optionally for debugging.
 * Include debug options in debug flavor of the app only.
 */
//class WikiApiClient {
//    companion object {
//        val baseURL = "https://en.wikipedia.org/w/"
//        var retrofit: Retrofit? = null
//
//        val wikiClient: Retrofit
//            get() {
//                /** Interceptor is for debug only */
//                val interceptor = HttpLoggingInterceptor()
//                interceptor.level = HttpLoggingInterceptor.Level.BODY
//
//                /**
//                 * Set timeout in case the service is unavailable or
//                 * hangs for some reason
//                 */
//                val httpClient = OkHttpClient.Builder()
//                    .connectTimeout(10, TimeUnit.SECONDS)
//                    .readTimeout(10, TimeUnit.SECONDS)
//                    .build()
//
//                if (retrofit == null) {
//                    retrofit = Retrofit.Builder()
//                        .baseUrl(baseURL)
//                        .addConverterFactory(GsonConverterFactory.create())
//                        .client(httpClient)
//                        .build()
//                }
//                return retrofit!!
//            }
//    }
//}