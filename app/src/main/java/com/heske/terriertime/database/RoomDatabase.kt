/*
 * Copyright 2018 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.heske.terriertime.database

import android.content.Context
import androidx.room.*

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



/**
 * A database that stores Terriers information.
 * And a global method to get access to the database.
 *
 * This pattern is pretty much the same for any database,
 * so you can reuse it.
 */
@Database(entities = [Terrier::class,FlickrImage::class], version = 1, exportSchema = false)
abstract class TerriersDatabase : RoomDatabase() {
   abstract val terriersTableDao: TerriersDao
   abstract val flickrTableDao: FlickrDao
}

private lateinit var INSTANCE: TerriersDatabase

/**
 * INSTANCE will keep a reference to any database returned via getInstance.
 *
 * This will help us avoid repeatedly initializing the database, which is expensive.
 *
 *  The value of a volatile variable will never be cached, and all writes and
 *  reads will be done to and from the main memory. It means that changes made by one
 *  thread to shared data are visible to other threads.
 */

fun getDatabase(context: Context): TerriersDatabase {
    synchronized(TerriersDatabase::class) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room
                .databaseBuilder(
                    context.applicationContext,
                    TerriersDatabase::class.java,
                    "terriers_db"
                )
                .fallbackToDestructiveMigration()
                .build()
        }
    }
    return INSTANCE
}