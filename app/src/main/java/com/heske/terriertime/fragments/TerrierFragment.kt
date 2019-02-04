package com.heske.terriertime.fragments

import android.media.SoundPool
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import com.heske.terriertime.adapters.TerriersRvAdapter
import com.heske.terriertime.database.Terrier
import com.heske.terriertime.database.TerriersDatabase
import com.heske.terriertime.databinding.FragmentTerriersBinding
import com.heske.terriertime.viewmodels.TerriersViewModel
import com.heske.terriertime.viewmodels.TerriersViewModelFactory

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

class TerrierFragment : Fragment() {
    private val TAG = TerrierFragment::class.java.simpleName
    private var lastPosition = 0
    private var sharedPreferences = "SharedPrefs"
    //lateinit var context: Context
    //    lateinit var mBreedListAdapter: TerriersRvAdapter
    private var breedMap = HashMap<String, Terrier>()
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
        //     inflater, R.layout.fragment_terriers, container, false)

        // TODO This binding provides access to terriers_recycler in fragment_terriers
        val binding = FragmentTerriersBinding.inflate(inflater)

        val application = requireNotNull(this.activity).application
        val dataSource = TerriersDatabase.getInstance(application).terriersDatabaseDao
        val viewModelFactory = TerriersViewModelFactory(dataSource, application)

        val viewModel =
            ViewModelProviders.of(
                this, viewModelFactory
            ).get(TerriersViewModel::class.java)

        binding.terriersViewModel = viewModel
        binding.terriersRecycler.adapter = TerriersRvAdapter()

        // TODO add observer to play sounds

        binding.setLifecycleOwner(this)
        return binding.root
    }
}