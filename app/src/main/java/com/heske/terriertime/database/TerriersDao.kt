package com.heske.terriertime.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query

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
 * A Terrier object represents one item in the hard-coded
 * TerrierBreeds.terriersMap, plus a url for the main image (stored in assets),
 * and a list of Flickr image urls.
 */
@Dao
interface TerriersDao {
    @Insert
    fun insertAll(terriers: List<Terrier>)

    @Insert(onConflict = REPLACE)
    fun insert(terriers: Terrier) : Long

    @Query("select summary from breed where name = :breedName")
    fun selectSummary(breedName: String) : String

    @Query("select * from breed  ORDER BY name ASC")
    fun getAllTerriers(): LiveData<List<Terrier>>

    @Query("select count(*) from breed")
    fun getRowCount(): Int

    @Query("select count(*) from breed where summary is not null or summary != '' ")
    fun getSummaryCount(): Int

    @Query("delete from breed")
    fun deleteAll(): Int

    @Query("select * from breed where name = :breedName")
    fun selectBreed(breedName: String): Terrier

    @Query("UPDATE breed SET summary = :summary WHERE name = :breedName")
    fun updateSummary(breedName: String, summary: String?): Int
}

