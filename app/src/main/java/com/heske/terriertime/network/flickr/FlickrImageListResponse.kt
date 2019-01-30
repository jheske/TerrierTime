/*
 * Created by Jill Heske on 8/19/2018
 * Copyright (c) All rights reserved
 */
package com.heske.terriertime.network.flickr

import com.google.gson.annotations.SerializedName

/**
 * Pojo used by Gson for extracting an [imageList] of Flickr image paths for one breed.
 * Expects an object called parse.
 */
//
data class FlickrImageListResponse(@SerializedName("items")
                                   var imageList: ArrayList<FlickrImageItem>)

/**
 * Gets Mars real estate property information from the Mars API Retrofit service and updates the
 * [MarsProperty] and [MarsApiStatus] [LiveData]. The Retrofit service returns a coroutine
 * Deferred, which we await to get the result of the transaction.
 * @param filter the [MarsApiFilter] that is sent as part of the web server request
 */
/*
@Parcelize
data class MarsProperty(
    val id: String,
    // used to map img_src from the JSON to imgSrcUrl in our class
    @Json(name = "img_src") val imgSrcUrl: String,
    val type: String,
    val price: Double) : Parcelable {
    val isRental
        get() = type == "rent"
}*/