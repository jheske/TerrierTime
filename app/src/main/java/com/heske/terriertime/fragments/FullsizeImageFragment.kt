package com.heske.terriertime.fragments

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.heske.terriertime.R
import kotlinx.android.synthetic.main.fragment_activity_fullsize_image.*

const val ARG_BREED_NAME = "breed_name"

/**
 * Retrieve the breed name and image path and display the image
 */
class FullsizeImageActivityFragment : Fragment() {
    private val TAG = FullsizeImageActivityFragment::class.java.simpleName
    private var mBreedName: String? = null
    private var mImagePath: String? = null
}
