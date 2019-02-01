/*
 * Created by Jill Heske on 8/19/2018
 * Copyright (c) All rights reserved
 */
package com.heske.terriertime.network.wiki

import com.google.gson.annotations.SerializedName
import com.heske.terriertime.network.wiki.WikiBreedSummaryItem

data class WikiBreedSummaryList(@SerializedName("pages")
                                var summaryList: HashMap<Int, WikiBreedSummaryItem>)
