package com.heske.terriertime.fragments

import android.media.SoundPool
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import com.heske.terriertime.adapters.PhotoGridAdapter
import com.heske.terriertime.database.Breed
import com.heske.terriertime.database.BreedDatabase
import com.heske.terriertime.databinding.FragmentBreedBinding
import com.heske.terriertime.viewmodels.BreedViewModel
import com.heske.terriertime.viewmodels.BreedViewModelFactory

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
 *
 */

class BreedFragment : Fragment() {
    private val TAG = BreedFragment::class.java.simpleName
    //    private var mBreedList = ArrayList<Breed>()
    private var lastPosition = 0
    private var sharedPreferences = "SharedPrefs"
    //lateinit var context: Context
    //    lateinit var mBreedListAdapter: BreedListAdapter
    private var breedMap = HashMap<String, Breed>()
    lateinit var breedViewModel: ViewModel
    lateinit var soundPool: SoundPool
    var growlSound: Int? = null
    var barkSound: Int? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // This one only helps with binding the TextView to LiveData in the view model
        // It doesn't help with binding list items in the recycler
        // val binding: FragmentBreedBinding = DataBindingUtil.inflate(
        //     inflater, R.layout.fragment_breed, container, false)

        // This binding provides access to photos_recycler in fragment_breed
        val binding = FragmentBreedBinding.inflate(inflater)

        val application = requireNotNull(this.activity).application
        val dataSource = BreedDatabase.getInstance(application).breedDatabaseDao
        val viewModelFactory = BreedViewModelFactory(dataSource, application)

        val breedViewModel =
            ViewModelProviders.of(
                this, viewModelFactory
            ).get(BreedViewModel::class.java)

        binding.breedViewModel = breedViewModel
        binding.photosRecycler.adapter = PhotoGridAdapter()

        binding.setLifecycleOwner(this)
        return binding.root
    }
}