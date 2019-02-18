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
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import com.heske.terriertime.utils.TERRIERS_DATA_FILENAME

class DatabaseWorker(
    context: Context,
    workerParams: WorkerParameters
) : Worker(context, workerParams) {

    private val TAG by lazy { DatabaseWorker::class.java.simpleName }

    override fun doWork(): Result {
        return try {
            applicationContext.assets.open(TERRIERS_DATA_FILENAME).use {
                    inputStream ->
                JsonReader(inputStream.reader()).use { jsonReader ->
                    val terrierType = object : TypeToken<List<Terrier>>() {}.type
                    val terrierList: List<Terrier> = Gson().fromJson(jsonReader, terrierType)

                    val database
                            = TerriersDatabase.getInstance(applicationContext)
                    database.terriersDatabaseDao.insertAll(terrierList)

                    Result.success()
                }
            }
        } catch (ex: Exception) {
            Log.e(TAG, "Error seeding database", ex)
            Result.failure()
        }
    }
}