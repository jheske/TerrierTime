package com.heske.terriertime.utils

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.heske.terriertime.database.Terrier

/**
 * Return boolean indicating network availability.
 */
fun Context.isNetworkConnected(): Boolean {
    var isOnline = false
    try {
        val cm = getSystemService(Context.CONNECTIVITY_SERVICE)
                as ConnectivityManager
        val netInfo = cm.activeNetworkInfo
        //should check null because in airplane mode it will be null
        isOnline = netInfo != null && netInfo.isConnected
    } catch (ex: Exception) {
        ex.printStackTrace()
    }
    return isOnline
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
 * Used to hide the keyboard after user input
 */
fun Context.hideKeyboard(view: View) {
    val inputMethodManager =
        getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}

/**
 * To avoid multiple network calls, build one string chaining
 * together all the breed names so Wiki can download summaries
 * in one batch.
 */
fun buildBreedTagString(terrierBreedList: ArrayList<Terrier>): String {
    val sb = StringBuilder()
    val listSize = terrierBreedList.size
    for (i in 0..listSize - 1) {
        sb.append(terrierBreedList[i].name.replace(" ", "_"))
        if (i != listSize - 1) {
            sb.append('|')
        }
    }
    return sb.toString()
}

