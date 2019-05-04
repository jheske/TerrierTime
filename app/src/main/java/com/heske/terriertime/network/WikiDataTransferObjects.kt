package com.heske.terriertime.network

import com.google.gson.annotations.SerializedName
import com.heske.terriertime.database.DatabaseTerrier
import com.heske.terriertime.terriers.Summary

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
data class WikiSummaryListResponse(var query: WikiBreedSummaryList)

data class WikiBreedSummaryList(
    @SerializedName("pages")
    var summaryList: HashMap<Int, WikiBreedSummaryItem>
)

data class WikiBreedSummaryItem(
    @SerializedName("title") var breed: String,
    @SerializedName("extract") var summary: String
)

//data class WikiImageItem (@SerializedName("title")
//                          val imageFileName: String)

/**
 *
 * Create simple List of breed/summary pairs from  ArrayList<WikiBreedSummaryItem>.
 */
fun WikiSummaryListResponse.asDatabaseModel(): Array<Summary> {
    val listOfSummaries = ArrayList(query.summaryList.values)

    return listOfSummaries.map {
        Summary(
            breed = it.breed,
            summary = it.summary
        )
    }.toTypedArray()
}