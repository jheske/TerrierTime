/*
 * Created by Jill Heske on 8/19/2018
 * Copyright (c) All rights reserved
 */

package com.heske.terriertime.flickr

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.heske.terriertime.databinding.FragmentFlickrBinding
import com.heske.terriertime.utils.setFullScreen

/**
 * Retrieve the list of paths to Flickr images and display
 * them in a RecyclerView
 */
class FlickrFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // This binding exposes access to photos_recycler in fragment_flickr
        val binding = FragmentFlickrBinding.inflate(inflater)
        val terrierName = FlickrFragmentArgs.fromBundle(arguments!!).breedName
        val viewModelFactory = FlickrViewModelFactory(terrierName)

        val flickrViewModel =
            ViewModelProviders.of(this, viewModelFactory).get(FlickrViewModel::class.java)

        binding.flickrViewModel = flickrViewModel
        binding.photosRecycler.adapter = FlickrRvAdapter()

        binding.setLifecycleOwner(this)
       // activity!!.setFullScreen()
        return binding.root
    }
}