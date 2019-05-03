/*
 * Created by Jill Heske on 8/19/2018
 * Copyright (c) All rights reserved
 */

package com.heske.terriertime.splash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.snackbar.Snackbar
import com.heske.terriertime.databinding.FragmentSplashBinding
import kotlinx.android.synthetic.main.fragment_splash.*
import kotlinx.android.synthetic.main.fragment_terriers.*

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
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentSplashBinding.inflate(inflater)

        // Allows Data Binding to Observe LiveData with the lifecycle of this Fragment
        binding.setLifecycleOwner(this)

        val application = requireNotNull(this.activity).application

        //val dataSource = getDatabase(application).terriersDatabaseDao
        val viewModelFactory = NewSplashViewModelFactory(application)

        val viewModel =
            ViewModelProviders.of(
                this, viewModelFactory
            ).get(NewSplashViewModel::class.java)

        viewModel.spinner.observe(viewLifecycleOwner, Observer { value ->
            value?.let { show ->
                if (show)
                    pv_circular.visibility = VISIBLE
                else
                    pv_circular.visibility = GONE

            }
        })

        viewModel.snackBar.observe(viewLifecycleOwner, Observer { text ->
            text?.let {
                Snackbar.make(layout_container, text, Snackbar.LENGTH_SHORT)
                    .show()
                viewModel.onSnackbarShown()
            }
        })

        return binding.root
    }
}
