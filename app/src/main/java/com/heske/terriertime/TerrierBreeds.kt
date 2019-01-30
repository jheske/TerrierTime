package com.heske.terriertime

import com.heske.terriertime.database.Breed

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
 * The breeds that will display in the RecyclerView on the main page, along
 * with a fact that helps the user guess the breed.
 *
 * Breed names correspond with Wiki tags which we use to download
 * summary text.  Breed data is stored in the Breed table.
 *
 * Flickr image filenames will be downloaded and added to the FlickrImages
 * table.  Those images are displayed by FlickrImageActivity.
 */
object TerrierBreeds {
    val app = TerrierApp.instance
    val breedMap: HashMap<String, Breed>
        get() = hashMapOf(
            (app.getString(R.string.airedale)
                    to Breed(
                0, app.getString(R.string.airedale),
                app.getString(R.string.airedale_fact)
            )),
            (app.getString(R.string.bull_terrier)
                    to Breed(
                0, app.getString(R.string.bull_terrier),
                app.getString(R.string.bull_terrier_fact)
            )),
            (app.getString(R.string.black_russian_terrier)
                    to Breed(
                0, app.getString(R.string.black_russian_terrier),
                app.getString(R.string.black_russian_terrier_fact)
            )),
            (app.getString(R.string.boston_terrier)
                    to Breed(
                0, app.getString(R.string.boston_terrier),
                app.getString(R.string.boston_terrier_fact)
            )),
            (app.getString(R.string.fox_terrier)
                    to Breed(
                0, app.getString(R.string.fox_terrier),
                app.getString(R.string.fox_terrier_fact)
            )),
            (app.getString(R.string.irish_terrier) to Breed(
                0,
                app.getString(R.string.irish_terrier),
                app.getString(R.string.irish_terrier_fact)
            )),
            (app.getString(R.string.manchester_terrier)
                    to Breed(
                0, app.getString(R.string.manchester_terrier),
                app.getString(R.string.manchester_terrier_fact)
            )),
            (app.getString(R.string.scottish_terrier)
                    to Breed(
                0, app.getString(R.string.scottish_terrier),
                app.getString(R.string.scottish_terrier_fact)
            )),
            (app.getString(R.string.skye_terrier)
                    to Breed(
                0, app.getString(R.string.skye_terrier),
                app.getString(R.string.skye_terrier_fact)
            )),
            (app.getString(R.string.schnauzer)
                    to Breed(
                0, app.getString(R.string.schnauzer),
                app.getString(R.string.schnauzer_fact)
            )),
            (app.getString(R.string.teddy_roosevelt_terrier)
                    to Breed(
                0, app.getString(R.string.teddy_roosevelt_terrier),
                app.getString(R.string.teddy_rossevelt_terrier_fact)
            )),
            (app.getString(R.string.welsh_terrier)
                    to Breed(
                0, app.getString(R.string.welsh_terrier),
                app.getString(R.string.welsh_terrier_fact)
            )),
            (app.getString(R.string.yorkshire_terrier)
                    to Breed(
                0, app.getString(R.string.yorkshire_terrier),
                app.getString(R.string.yorkshire_terrier_fact)
            ))
        )
}