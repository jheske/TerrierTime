package com.heske.terriertime.utils

import android.widget.ImageView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.heske.terriertime.R
import com.heske.terriertime.database.Terrier
import com.heske.terriertime.flickr.FlickrRvAdapter
import com.heske.terriertime.terriers.TerriersRvAdapter

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
 *
 * When there is no data (data is null), hide the [RecyclerView],
 * otherwise show it.
 */
@BindingAdapter("listData")
fun bindRecyclerView(recyclerView: RecyclerView, data: List<String>?) {
    val adapter = recyclerView.adapter as FlickrRvAdapter
    adapter.submitList(data)
}

/**
 * Uses the Glide library to load an image by URL into an [ImageView]
 * See flickr_image_listitem dog_image app:imageUrl attribute
 */
@BindingAdapter("imageUrl")
fun bindImage(imgView: ImageView, imgUrl: String?) {
    imgUrl?.let {
        val imgUri = imgUrl.toUri().buildUpon().scheme("https").build()
        Glide.with(imgView.context)
            .load(imgUri)
            .apply(
                RequestOptions()
                    .placeholder(R.drawable.terrier_placeholder)
                    .error(R.drawable.terrier_placeholder)
            )
            .into(imgView)
    }
}

/**
 * Adapter for the RecyclerView that displays the list of terriers,
 * a photo for each as well as a fun fact and UI buttons allowing the
 * user to guess the breed, and then display its DetailActivity.
 *
 * Maintains and displays a list of objects containing data downloaded from Wiki.
 *
 * Define button click callbacks: [moreBtnClick], [guessBtnClick], [giveUpBtnClick],
 * and [mainImageClick] to respond to user button clicks.
 */

/**
 * This class implements a [RecyclerView] [ListAdapter] which uses Data Binding to present [List]
 * data, including computing diffs between lists.
 * @param onClick a lambda that takes the
 */

/**
 * This is connected to the RecyclerView in fragment_terriers.
 * The RecyclerView has a data binding to TerriersViewModel.
 * Whenever TerriersViewModel.listOfTerriers LiveData changes,
 * this gets called to refresh the recycler's data.
 * When there is no data (data is null), hide the [RecyclerView],
 * otherwise show it.
 */
@BindingAdapter("terrierRecyclerListData")
fun bindTerriersRecyclerView(recyclerView: RecyclerView, data: List<Terrier>?) {
    val adapter = recyclerView.adapter as TerriersRvAdapter
    adapter.submitList(data)
}


/**
 * Bind the image in one row of terriers_recycler (fragment_terriers.xml).
 * terrierImageUrl -> is an attribute defined in the img_photo ImageView
 * found in terrier_listitem.xml.
 *
 * Use the Glide library to load an image from assets folder into
 * an [ImageView] See terrier_listitem app:terrierImageUrl="@{terrier.name}"
 */
@BindingAdapter("terrierImageUrl")
fun bindTerrierImage(imgView: ImageView, terrierBreedName: String?) {
    terrierBreedName?.let {
        val assetPath = it.toAssetPath()
        Glide.with(imgView.context)
            .load(assetPath)
            .apply(
                RequestOptions()
                    .placeholder(R.drawable.terrier_placeholder)
                    .error(R.drawable.terrier_placeholder)
            )
            .into(imgView)
    }
}

/*
 * Bind a fullsize image (its jpg is in the assets folder).
 * fullsizeImageUrl -> is an attribute defined in
 * the img_fullsize ImageView in fragment_fullsize_image.xml
 */
@BindingAdapter("fullsizeImageUrl")
fun bindFullsizeImage(imgView: ImageView, breedName: String?) {
    breedName?.let {
        val assetPath = it.toAssetPath()
        Glide.with(imgView.context)
            .load(assetPath)
            .apply(
                RequestOptions()
                    .placeholder(R.drawable.terrier_placeholder)
                    .error(R.drawable.terrier_placeholder)
            )
            .into(imgView)
    }
}

@BindingAdapter("detailImageUrl")
fun bindDetailImage(imgView: ImageView, breedName: String?) {
    breedName?.let {
        val assetPath = it.toAssetPath()
        Glide.with(imgView.context)
            .load(assetPath)
            .apply(
                RequestOptions()
                    .placeholder(R.drawable.terrier_placeholder)
                    .error(R.drawable.terrier_placeholder)
            )
            .into(imgView)
    }
}


