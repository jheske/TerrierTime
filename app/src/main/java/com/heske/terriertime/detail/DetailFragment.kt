package com.heske.terriertime.detail

import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.heske.terriertime.databinding.FragmentDetailBinding
import com.heske.terriertime.utils.toBreedFileName
import kotlinx.android.synthetic.main.fragment_detail.*
import java.io.IOException
import java.io.InputStream

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
        val binding = FragmentDetailBinding.inflate(inflater)
        val terrier = DetailFragmentArgs.fromBundle(arguments!!).terrier
        terrierBreedName = terrier.name

        val viewModelFactory = DetailViewModelFactory(terrier)
        val viewModel =
            ViewModelProviders.of(
                this, viewModelFactory
            ).get(DetailViewModel::class.java)

        viewModel.navigateToFlickrPix.observe(this, Observer {
            if (it != null) {
                this.findNavController()
                    .navigate(
                        DetailFragmentDirections.actionDetailToFlickr(it)
                    )
                //After the navigation has taken place, set nav event to null.
                //!!!!Otherwise the app will crash when Back button is pressed
                // from destination Fragment!!!!
                viewModel.displayFlickrPixComplete()
            }
        })

        binding.viewModel = viewModel
        binding.setLifecycleOwner(this)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupWithNavController(detail_toolbar, findNavController())
        detail_toolbar.title = terrierBreedName
        loadBackdropImage()
    }

    /* Retrieve image associated with [breedName] from assets and display it
     * int the toolbar's backdrop.
     */
    fun loadBackdropImage() {
        var inputStream: InputStream? = null

        try {
            val context = requireNotNull(this.context)

            inputStream = context.assets.open(terrierBreedName.toBreedFileName())
            val bitmap = BitmapFactory.decodeStream(inputStream)
            bitmap?.let {
                toolbar_image.setImageBitmap(bitmap)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            inputStream?.close()
        }
    }

}