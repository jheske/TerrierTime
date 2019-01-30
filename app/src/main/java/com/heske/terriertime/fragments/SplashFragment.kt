/*
 * Created by Jill Heske on 8/19/2018
 * Copyright (c) All rights reserved
 */

package com.heske.terriertime.fragments

//import android.arch.lifecycle.Observer
//import android.arch.lifecycle.ViewModelProviders
import androidx.fragment.app.Fragment

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
}
