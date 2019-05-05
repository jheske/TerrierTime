package com.heske.terriertime.detail

import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.heske.terriertime.databinding.FragmentDetailBinding
import androidx.appcompat.app.AppCompatActivity
import com.heske.terriertime.R

/* Copyright (c) 2019 Jill Heske All rights reserved.
 * 
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 * 
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

class DetailFragment : Fragment() {
    var terrierBreedName: String = "Airedale Terrier"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val terrier = DetailFragmentArgs.fromBundle(arguments!!).terrier
        val viewModelFactory = DetailViewModelFactory(terrier)
        val terrierViewModel =
            ViewModelProviders.of(
                this, viewModelFactory
            ).get(DetailViewModel::class.java)

        val binding = DataBindingUtil.inflate<FragmentDetailBinding>(
            inflater, R.layout.fragment_detail, container, false).apply {
            viewModel = terrierViewModel
            setLifecycleOwner(this@DetailFragment)
        }

        terrierBreedName = terrier.name
        // TODO use Databinding
        (activity as AppCompatActivity).supportActionBar!!.title = terrierBreedName

        terrierViewModel.navigateToFlickrPix.observe(this, Observer {
            if (it != null) {
                this.findNavController()
                    .navigate(
                        DetailFragmentDirections.actionDetailToFlickr(it)
                    )
                //After the navigation has taken place, set nav event to null.
                //!!!!Otherwise the app will crash when Back button is pressed
                // from destination Fragment!!!!
                terrierViewModel.displayFlickrPixComplete()
            }
        })
        return binding.root
    }
}