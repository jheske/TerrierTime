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
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.NavHostFragment.findNavController
import androidx.navigation.fragment.findNavController
import com.heske.terriertime.R
import com.heske.terriertime.database.TerriersDatabase
import com.heske.terriertime.databinding.FragmentSplashBinding
import com.heske.terriertime.fragments.SplashFragmentDirections.actionSplashToTerriers
import com.heske.terriertime.viewmodels.SplashViewModel
import com.heske.terriertime.viewmodels.SplashViewModelFactory


/**
 * The splash screen is shown for this delay before MainActivity is called. The splash screen
 * should only be shown for as long as it takes for the app to download data.
 * However, display the screen for SPLASH_DISPLAY_LENGTH if there is no
 * network connection.
 *
 * If network dat load fails (probably the app doesn't have internet access),
 * go directly to the next activity and load data locally.
 * Hopefully the data is there from a prior run of the app with
 * the network available.
 *
 * A SettingsActivity could offer the opportunity to try to
 * download the data if the network becomes available,
 * without having to restart the app.
 */
class SplashFragment : Fragment() {
    private val TAG = SplashFragment::class.java.simpleName

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

//        val view = inflater.inflate(
//            R.layout.fragment_splash,
//            container, false
//        )

        val binding = FragmentSplashBinding.inflate(inflater)

        // Allows Data Binding to Observe LiveData with the lifecycle of this Fragment
        binding.setLifecycleOwner(this)

        val application = requireNotNull(this.activity).application

        val dataSource = TerriersDatabase.getInstance(application).terriersDatabaseDao
        val viewModelFactory = SplashViewModelFactory(application, dataSource)

        val viewModel =
            ViewModelProviders.of(
                this, viewModelFactory
            ).get(SplashViewModel::class.java)

        /**
         *  ViewModel emits the closeSplashScreen event when its timer times out,
         *  which means it's time to navigate to the TerrierFragment.
         */
        // TODO Test this event after viewModel finishes initializing the db.
        viewModel
            .eventCloseSplashScreen
            .observe(this, Observer { closeSplashScreen ->
                // Navigate to TerrierFragment
                if (closeSplashScreen) {
                    //SplashFragmenDirections and actionSplashFragmentToTerriersFragment are generated
                    //this.findNavController().navigate(actionSplashToTerriers())
                    //this.findNavController().navigate(SplashFragmentDirections.actionSplashToTerriers())
                    this.findNavController().navigate(SplashFragmentDirections.actionSplashToTerriers())
                    // Tell the ViewModel we've made the navigate call to prevent multiple navigation
                    // !!!!!Otherwise app will crash when Back button is clicked from destination fragment!!!!!
                    viewModel.onEventCloseSplashScreenComplete()
                }
            })

        return binding.root
    }
}
