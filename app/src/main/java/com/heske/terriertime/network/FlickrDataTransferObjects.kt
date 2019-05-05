package com.heske.terriertime.network

import com.google.gson.annotations.SerializedName
import com.heske.terriertime.database.FlickrTableEntity
import com.heske.terriertime.flickr.FlickrImage

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
 * Pojo used by Gson for extracting an [imageList] of Flickr image paths for one breed.
 * Expects an object called parse.
 */
data class FlickrImageListResponse(
    @SerializedName("items")
    var imageList: ArrayList<FlickrImageItem>
)

data class FlickrImageItem(
    @SerializedName("media")
    val media: HashMap<String, String>
)

/***
 * Return an ArrayList containing string values from [flickrImageList] item
 * media field.
 */
//fun FlickrImageListResponse
//        .asSimpleList(flickrImageList: ArrayList<FlickrImageItem>): ArrayList<String> {
fun FlickrImageListResponse.asDatabaseModel(breedName: String): Array<FlickrTableEntity> {
    return imageList.map {
        FlickrTableEntity(
            id=0,
            breedName = breedName,
            imageFilePath = it.media.getValue("m"))
    }.toTypedArray()
}
