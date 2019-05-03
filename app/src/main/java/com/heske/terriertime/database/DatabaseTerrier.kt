package com.heske.terriertime.database

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

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.heske.terriertime.terriers.Terrier
import kotlinx.android.parcel.Parcelize

/**
 * An DatabaseTerrier Entity object represents one row in the Terriers table.  Terriers names
 * in the database are treated as Wikipedia tags and are used to retrieve information about the
 * breed, such as summary text, so their exact spelling has to conform to Wiki counterparts.
 */
@Parcelize
@Entity
data class DatabaseTerrier constructor(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(index = true, name = "_id")
    var id: Long,
    @ColumnInfo(name = "name")
    var name: String,
    @ColumnInfo(name = "fact")
    var fact: String,
    @ColumnInfo(name = "summary")
    var summary: String? = null,
    // @ColumnInfo(name="main_url")
    // var mainImageUrl: String? = null,
    // The List of Flickr image urls will go here.
    @Ignore
    var imageList: ArrayList<String>? = null
) : Parcelable  {
    // The empty constructor is required
    constructor() : this(0, "", "", "", null)
}

fun List<DatabaseTerrier>.asDomainModel(): List<Terrier> {
    return map {
        Terrier(
            id = it.id,
            name = it.name,
            fact = it.fact,
            summary = it.summary,
            imageList = it.imageList)
    }
}

