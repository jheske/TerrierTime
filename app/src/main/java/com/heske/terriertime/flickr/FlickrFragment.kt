/*
 * Created by Jill Heske on 8/19/2018
 * Copyright (c) All rights reserved
 */

package com.heske.terriertime.flickr

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.heske.terriertime.database.getDatabase
import com.heske.terriertime.databinding.FragmentFlickrBinding

/**
 * Retrieve the list of paths to Flickr images and display
 * them in a RecyclerView
 */
class FlickrFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val application = requireNotNull(this.activity).application
        val flickrDao = getDatabase(application).flickrTableDao
        val breedName = FlickrFragmentArgs.fromBundle(arguments!!).breedName
        val viewModelFactory = FlickrViewModelFactory(breedName, flickrDao, application)
        val flickrViewModel =
            ViewModelProviders.of(this, viewModelFactory)
                .get(FlickrViewModel::class.java)

        val binding = FragmentFlickrBinding.inflate(inflater).apply {
            viewModel = flickrViewModel
            setLifecycleOwner(this@FlickrFragment)
            photosRecycler.adapter = FlickrRvAdapter(
                FlickrRvAdapter.OnImageClickListener {
                    flickrViewModel.displayFullsizeImage(it.imageFilePath)
                })
        }

        flickrViewModel.navigateToFullsizeImage.observe(this, Observer {
            if (it != null) {
                this.findNavController()
                    .navigate(
                        FlickrFragmentDirections.actionFlickrToFullsize(it)
                    )
                //After the navigation set nav event to null.
                //!!!!Otherwise the app will crash when Back button is pressed
                // from destination Fragment!!!!
                flickrViewModel.displayFullsizeImageComplete()
            }
        })

        return binding.root
    }
}