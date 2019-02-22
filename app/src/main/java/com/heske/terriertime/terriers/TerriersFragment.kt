package com.heske.terriertime.terriers

import android.app.AlertDialog
import android.media.AudioManager
import android.media.SoundPool
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavHost
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.NavigationUI.setupActionBarWithNavController
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.heske.terriertime.R
import com.heske.terriertime.database.TerriersDatabase
import com.heske.terriertime.databinding.FragmentTerriersBinding
import kotlinx.android.synthetic.main.fragment_terriers.*
import kotlinx.android.synthetic.main.listitem_terriers.*

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

class TerriersFragment : Fragment() {
    private val TAG = TerriersFragment::class.java.simpleName
    lateinit var soundPool: SoundPool
    var growlSound: Int? = null
    var barkSound: Int? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // This binding provides access to terriers_recycler in fragment_terriers
    //    val binding = FragmentTerriersBinding.inflate(inflater)
        val binding = FragmentTerriersBinding
            .inflate(inflater, container, false)

        val application = requireNotNull(this.activity).application
        val dataSource = TerriersDatabase.getInstance(application).terriersDatabaseDao
        val viewModelFactory = TerriersViewModelFactory(dataSource, application)

        val viewModel =
            ViewModelProviders.of(
                this, viewModelFactory
            ).get(TerriersViewModel::class.java)

        binding.terriersViewModel = viewModel

        setupSoundPool()

        // TODO Can I replace the onClickListeners with DataBindings in the xml file?
        //    as in android:onClick=@{() -> terriersViewModel.displayFullsizeImage???
        // Sets the adapter of the photosGrid RecyclerView with clickHandler lambda that
        // tells the viewModel when our property is clicked
        binding.terriersRecycler.adapter =
            TerriersRvAdapter(TerriersRvAdapter.OnImageClickListener {
                viewModel.displayFullsizeImage(it)
            }, TerriersRvAdapter.OnGiveUpClickListener {
                viewModel.displayDetailScreen(it)
            }, TerriersRvAdapter.OnGuessClickListener { terrier, guessText ->
                viewModel.processGuess(terrier, guessText)
            }, TerriersRvAdapter.OnMoreClickListener {
                viewModel.displayDetailScreen(it)
            })

        viewModel.listOfTerriers.observe(this, Observer {
            if (it != null) {
                Log.d(TAG, "There are ${it.size} terriers")
            }
        })

        viewModel.navigateToFullsizeImage.observe(this, Observer {
            if (it != null) {
                this.findNavController()
                    .navigate(
                        TerriersFragmentDirections.actionMainToFullsize(it.name)
                    )
                //After the navigation has taken place, set nav event to null.
                //!!!!Otherwise the app will crash when Back button is pressed
                // from destination Fragment!!!!
                viewModel.displayFullsizeImageComplete()
            }
        })

        viewModel.navigateToDetailScreen.observe(this, Observer {
            if (it != null) {
                this.findNavController()
                    .navigate(
                        TerriersFragmentDirections.actionMainFragmentToDetail(it)
                    )
                //After the navigation has taken place, set nav event to null.
                //!!!!Otherwise the app will crash when Back button is pressed
                // from destination Fragment!!!!
                viewModel.displayDetailScreenComplete()
            }
        })

        viewModel.correctGuess.observe(this, Observer {
            if (it != null) {
                playSound(barkSound)
                setButtons(true)
            }
        })

        viewModel.incorrectGuess.observe(this, Observer {
            if (it != null) {
                playSound(growlSound)
                showHintDialog(it)
                setButtons(false)
                // Now set _incorrectGuess to null,
                // so we don't get repeat calls
                viewModel.guessComplete()
            }
        })

        binding.setLifecycleOwner(this)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

       // val navController = findNavController()
       // setupWithNavController(toolbar,navController)
        terriers_recycler.addItemDecoration(
            TerriersRvDecoration(
                resources.getDimension(com.heske.terriertime.R.dimen.spacing_large).toInt()
            )
        )
    }

    /**
     * MinSDK is set to 22, doesn't guarantee the most coverage, but
     * then I don't have to use an old soundPool code.
     *
     * Initialize a SoundPool. Raw sound resources are loaded/unloaded
     * in onResume/onPause.
     */
    fun setupSoundPool() {
        soundPool = SoundPool(
            3,
            AudioManager.STREAM_MUSIC, 0
        )
        growlSound = soundPool.load(activity, com.heske.terriertime.R.raw.growl, 1)
        barkSound = soundPool.load(activity, com.heske.terriertime.R.raw.bark, 1)
    }

    /**
     * Pause any sounds that are playing, and play raw sound associated with [soundId]
     * indicating a correct or incorrect guess.
     * Pause any prior sounds before playing the new sound.
     */
    private fun playSound(soundId: Int?) {
        soundId?.let {
            soundPool.autoPause()
            soundPool.play(
                it, 1f,
                1f, 0, 0, 1f
            )
        }
    }

    private fun setButtons(correctGuess: Boolean) {
        if (correctGuess) {
            btn_guess.visibility = GONE
            btn_give_up.visibility = GONE
            btn_more.visibility = VISIBLE
        } else {
            btn_guess.visibility = VISIBLE
            btn_give_up.visibility = VISIBLE
            btn_more.visibility = GONE
        }
    }

    /**
     * Display an AlertDialog with a custom layout indicating that [guess] does not
     * match the breed name.
     * The title and msg will depend on whether the user was wrong, or
     * entered blank spaces or nothing at all.
     */
    private fun showHintDialog(guess: String) {
        val wrongAnswerTitle: String
        val wrongAnswerMsg: String

        if (guess.isEmpty() || guess.isBlank()) {
            wrongAnswerTitle = getString(com.heske.terriertime.R.string.txt_no_answer_title)
            wrongAnswerMsg = getString(com.heske.terriertime.R.string.txt_no_answer)
        } else {
            wrongAnswerMsg = getString(com.heske.terriertime.R.string.txt_wrong_answer)
            wrongAnswerTitle = "$guess ${com.heske.terriertime.R.string.txt_wrong_answer_title}"
        }
        val customLayout = layoutInflater.inflate(com.heske.terriertime.R.layout.alert_dialog, null)
        val tvTitle = customLayout.findViewById(com.heske.terriertime.R.id.tv_alert_title) as TextView
        val tvText = customLayout.findViewById(com.heske.terriertime.R.id.tv_alert_text) as TextView
        val btnOk = customLayout.findViewById(com.heske.terriertime.R.id.btn_alert_ok) as Button
        val builder = AlertDialog.Builder(context)
        builder.setView(customLayout)
        tvTitle.text = wrongAnswerTitle
        tvText.text = wrongAnswerMsg
        val alertDialog = builder.create()
        btnOk.setOnClickListener(View.OnClickListener {
            alertDialog.dismiss()
        })
        alertDialog.show()
    }
}