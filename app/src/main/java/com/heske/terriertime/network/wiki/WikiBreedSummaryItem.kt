/*
 * Created by Jill Heske on 8/19/2018
 * Copyright (c) All rights reserved
 */
package com.heske.terriertime.model

import com.google.gson.annotations.SerializedName

data class WikiBreedSummaryItem(@SerializedName("title") var breed: String,
                                @SerializedName("extract") var summary: String)
