/*
 * Created by Jill Heske on 8/25/2018
 * Copyright (c) All rights reserved
 */

package com.heske.terriertime.utils

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.heske.terriertime.R


/**
 * Return boolean indicating network availability.
 */

fun Context.isNetworkAvailable(): Boolean {
    val connectivityMgr
            = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val activeNetwork = connectivityMgr.activeNetworkInfo

    val available = {
         if (activeNetwork == null)
             false
         else if (activeNetwork.type == ConnectivityManager.TYPE_WIFI) {
             true
         } else activeNetwork.type == ConnectivityManager.TYPE_MOBILE
     }
     return available()
}

/**
 * Create a valid lowercase filename from breed string by replacing
 * spaces with underscores
 * Airdale Terrier --> airedale_terrier.jpg
 */
fun String.toBreedFileName(): String {
    return "$this.jpg".replace(" ", "_").toLowerCase()
}

/**
 * Create a valid assets filename from breed string
 * Airdale Terrier --> file:///android_asset/airedale_terrier.jpg
 */
fun String.toAssetPath(): String {
    return "file:///android_asset/$this.jpg".replace(" ", "_").toLowerCase()
}

/**
 * Called by fragments to get the current Activity
 */
val Fragment.currentActivity: FragmentActivity
    get() = this.activity
            ?: throw IllegalStateException(context?.getString(R.string.txt_no_null_activity))

inline fun FragmentManager.inTransaction(func: FragmentTransaction.() -> Unit) {
    val fragmentTransaction = beginTransaction()
    fragmentTransaction.func()
    fragmentTransaction.commit()
}

/**
 * Used to hide the keyboard after user input
 */
fun Context.hideKeyboard(view: View) {
    val inputMethodManager =
            getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}

