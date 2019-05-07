package com.heske.terriertime.utils

import android.widget.ImageView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.heske.terriertime.R
import com.heske.terriertime.database.FlickrTableEntity
import com.heske.terriertime.database.TerriersTableEntity
import com.heske.terriertime.flickr.FlickrImage
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
 * Binding for the RecyclerView in fragment_terriers.
 * fragment_terriers has a data binding to TerriersViewModel.
 * When TerriersViewModel.listOfTerriers LiveData changes,
 * this gets called to refresh the recycler's data.
 * When there is no data (data is null), hide the [RecyclerView],
 * otherwise show it.
 */
@BindingAdapter("rvTerriersListItems")
fun bindRvTerriers(recyclerView: RecyclerView, data: List<TerriersTableEntity>?) {
    val adapter = recyclerView.adapter as TerriersRvAdapter
    adapter.submitList(data)
}

/**
 * Binding for the ImageView in listitem_terrier.xml.
 *
 * Use the Glide library to load an image from assets folder into
 * an [ImageView] See listitem_terrier app:terrierImageUrl="@{terrier.name}"
 */
@BindingAdapter("rvTerriersListItemImageUrl")
fun bindRvTerriersItemImage(imgView: ImageView, terrierBreedName: String?) {
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

/**
 * Binding for DetailFragment's backdrop image.
 */
@BindingAdapter("backdropImageUrl")
fun bindBackdropImage(imgView: ImageView, terrierBreedName: String?) {
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

/**
 * Binding for the RecyclerView in fragment_flickr.
 * fragment_flickr has a data binding to FlickrViewModel.
 * When image list LiveData changes, this gets called to refresh the recycler's data.
 * When there is no data (data is null), hide the [RecyclerView],
 * otherwise show it.
 */
@BindingAdapter("rvFlickrListItems")
fun bindRvFlickr(recyclerView: RecyclerView, data: List<FlickrTableEntity>?) {
    val adapter = recyclerView.adapter as FlickrRvAdapter
    adapter.submitList(data)
}

/**
 * Uses the Glide library to load an image by URL into an [ImageView]
 * See flickr_image_listitem  app:imageUrl attribute
 */
@BindingAdapter("rvFlickrListItemImageUrl")
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

/*
 * Bind for ImageView in fragment_fullsize_image.xml.
 * Fullsize image jpgs are in the assets folder).
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
