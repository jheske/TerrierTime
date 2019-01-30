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

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

/**
 * Represents one row in the Breed table.  Breed names in the database are treated
 * as Wikipedia tags and are used to retrieve information about the
 * breed, such as summary text, so their exact spelling has to conform to Wiki.
 */
@Entity(tableName = "breed")
data class Breed(@PrimaryKey(autoGenerate = true)
                 @ColumnInfo(index = true, name = "_id")
                 var id: Long,
                 @ColumnInfo(name = "name")
                 var name: String,
                 @ColumnInfo(name = "fact")
                 var fact: String,
                 @ColumnInfo(name = "summary")
                 var summary: String? = null,
                 @Ignore
                 var imageList: ArrayList<String>? = null) {
    constructor() : this(0, "", "", "", null)

    /**
     * Return true if [guess] matches breed name, false otherwise.
     * Also, try appending " terrier" to the guess in case the
     * user got the breed correct but left it off, eg.,
     * "Airedale" and "Airedale Terrier" are both correct guesses.
     */
    fun correctAnswer(guess: String): Boolean {
        val breedName = name.toLowerCase()
        val guessPlusTerrier = "${guess} terrier"

        return guess.toLowerCase().equals(breedName)
                || guessPlusTerrier.toLowerCase().equals(breedName)
    }
}
