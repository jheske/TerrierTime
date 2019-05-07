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
import com.heske.terriertime.databinding.FragmentFlickrBinding
import com.heske.terriertime.utils.InjectorUtils

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

        val factory
                = InjectorUtils.provideFlickrViewModelFactory(requireContext(),application)

        val flickrViewModel = ViewModelProviders.of(this, factory)
            .get(FlickrViewModel::class.java)

        val flickrRvAdapter = FlickrRvAdapter()

        val binding = FragmentFlickrBinding.inflate(inflater).apply {
            viewModel = flickrViewModel
            setLifecycleOwner(this@FlickrFragment)
            flickrRecycler.adapter = FlickrRvAdapter()
        }

        return binding.root
    }
}