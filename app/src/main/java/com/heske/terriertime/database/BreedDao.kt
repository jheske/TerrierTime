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
@Dao
interface BreedDao {
    @Insert
    fun insertAll(breeds: ArrayList<Breed>)

    @Insert(onConflict = REPLACE)
    fun insert(breed: Breed) : Long

    @Query("select summary from breed where name = :breedName")
    fun selectSummary(breedName: String) : String

    @Query("select * from breed  ORDER BY name ASC")
    fun getAllBreeds(): LiveData<List<Breed>>

    @Query("select count(*) from breed")
    fun getCount(): LiveData<Int>

    @Query("select count(*) from breed where summary is not null or summary != '' ")
    fun getSummaryCount(): Int

    @Query("delete from breed")
    fun deleteAll(): Int

    @Query("select * from breed where name = :breedName")
    fun selectBreed(breedName: String): Breed

    @Query("UPDATE breed SET summary = :summary WHERE name = :breedName")
    fun updateSummary(breedName: String, summary: String?): Long
}

