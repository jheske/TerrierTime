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
data class FlickrImageListResponse(@SerializedName("items")
                                   var imageList: ArrayList<FlickrImageItem>)