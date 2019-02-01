/*
 * Created by Jill Heske on 8/21/2018
 * Copyright (c) All rights reserved
 */
package com.heske.terriertime.network.wiki

import com.google.gson.annotations.SerializedName

data class WikiImageItem (@SerializedName("title")
                          val imageFileName: String)