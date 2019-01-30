/*
 * Created by Jill Heske on 8/19/2018
 * Copyright (c) All rights reserved
 */
package com.heske.terriertime.model

import com.google.gson.annotations.SerializedName

data class WikiBreedSummaryList(@SerializedName("pages")
                                var summaryList: HashMap<Int, WikiBreedSummaryItem>)
