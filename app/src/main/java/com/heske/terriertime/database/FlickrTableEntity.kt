/*
 * Created by Jill Heske on 8/21/2018
 * Copyright (c) All rights reserved
 */
package com.heske.terriertime.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.heske.terriertime.flickr.FlickrImage

@Entity
data class FlickrTableEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(index = true, name = "_id")
    var id: Long,
    @ColumnInfo(name = "breed_name")
    var breedName: String,
    @ColumnInfo(name = "image_file_path")
    var imageFilePath: String
)

fun List<FlickrTableEntity>.asDomainModel(): List<FlickrImage> {
    return map {
        FlickrImage(
            breedName = it.breedName,
            imageUrl = it.imageFilePath
        )
    }
}