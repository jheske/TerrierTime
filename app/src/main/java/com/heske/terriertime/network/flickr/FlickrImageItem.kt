/*
 * Created by Jill Heske on 8/19/2018
 * Copyright (c) All rights reserved
 */
package com.heske.terriertime.network.flickr

import com.google.gson.annotations.SerializedName

data class FlickrImageItem(@SerializedName("media")
                           val media: HashMap<String,String>)