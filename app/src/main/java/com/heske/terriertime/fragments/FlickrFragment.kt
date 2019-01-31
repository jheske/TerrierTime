/*
 * Created by Jill Heske on 8/19/2018
 * Copyright (c) All rights reserved
 */

package com.heske.terriertime.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.heske.terriertime.adapters.PhotoGridAdapter
import com.heske.terriertime.databinding.FragmentFlickrBinding
import com.heske.terriertime.viewmodels.FlickrViewModel
import com.heske.terriertime.viewmodels.FlickrViewModelFactory

/**
 * Retrieve the list of paths to Flickr images and display
 * them in a RecyclerView
 */
class FlickrFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // This one only helps with binding the TextView to LiveData in the view model
        // It doesn't help with binding list items in the recycler
        // val binding: FragmentBreedBinding = DataBindingUtil.inflate(
        //     inflater, R.layout.fragment_breed, container, false)

        // This binding exposes access to photos_recycler in fragment_flickr
        val binding
                = FragmentFlickrBinding.inflate(inflater)

        val application = requireNotNull(this.activity).application

        // ??? DO I REALLY NEED application ???
        val viewModelFactory = FlickrViewModelFactory(application)

        val flickrViewModel =
            ViewModelProviders.of(
                this, viewModelFactory
            ).get(FlickrViewModel::class.java)

        binding.flickrViewModel = flickrViewModel
        binding.photosRecycler.adapter = PhotoGridAdapter()

        binding.setLifecycleOwner(this)
        return binding.root
    }
}