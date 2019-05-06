package com.heske.terriertime.fullsize

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.heske.terriertime.databinding.FragmentFullsizeImageBinding

const val ARG_BREED_NAME = "breed_name"

/**
 * Retrieve the breed name and image path and display the image
 */
class FullsizeImageFragment : Fragment() {
    private val TAG = FullsizeImageFragment::class.java.simpleName

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentFullsizeImageBinding.inflate(inflater)

        val viewModelFactory = FullsizeImageViewModelFactory(
            FullsizeImageFragmentArgs
                .fromBundle(arguments!!).breedName
        )
        val viewModel =
            ViewModelProviders.of(
                this, viewModelFactory
            ).get(FullsizeImageViewModel::class.java)

        binding.viewModel = viewModel
        binding.setLifecycleOwner(this)
        return binding.root
    }
}
