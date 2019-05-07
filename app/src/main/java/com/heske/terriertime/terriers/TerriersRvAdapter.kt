package com.heske.terriertime.terriers

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.heske.terriertime.database.TerriersTableEntity
import com.heske.terriertime.databinding.ListitemTerriersBinding
import com.heske.terriertime.utils.toAssetPath
import kotlinx.android.synthetic.main.listitem_terriers.view.*

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

/**
 * This class implements a [RecyclerView] [ListAdapter] which uses Data Binding
 * to present [List] of [TerriersTableEntity] data, including computing diffs between lists.
 */
class TerriersRvAdapter(val onGuessClickListener: OnGuessClickListener) :
    ListAdapter<TerriersTableEntity, TerriersRvAdapter.TerrierViewHolder>(DiffCallback()) {

    /**
     * The TerrierViewHolder constructor takes the binding variable from the associated
     * list item, which nicely gives it access to the full object information,
     * in this case it's a [TerriersTableEntity] object.
     * See listitem_terrier.xml for all the views associated with each
     * row in terriers_recycler.
     *
     * binding.terrier - defined in listitem_terrier <data> block
     * terrierListItem - the object to be displayed in this row
     */
    class TerrierViewHolder(private val binding: ListitemTerriersBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            onButtonClickListener: View.OnClickListener,
            onImageClickListener: View.OnClickListener,
            listItem: TerriersTableEntity
        ) {
            binding.apply {
                // Binding variables are in listitem_terriers.xml.
                terrierTableEntity = listItem
                buttonClickListener = onButtonClickListener
                imageClickListener = onImageClickListener
                executePendingBindings()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
            TerrierViewHolder {
        return TerrierViewHolder(
            ListitemTerriersBinding
                .inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    /**
     * Replaces the contents of a view (invoked by the layout manager)
     * Set up listeners for all the buttons at index [position] in the [holder].
     * Button clicks will call back to [TerriersFragment] for processing.
     */

    override fun onBindViewHolder(holder: TerrierViewHolder, position: Int) {
        //val listItem = getItem(position)

        getItem(position).let { terrierEntity ->
            with(holder) {
                itemView.tag = terrierEntity
                itemView.btn_guess.setOnClickListener {
                    val guess = holder.itemView.et_guess_txt.text.toString()
                    onGuessClickListener.onGuessButtonClick(terrierEntity, guess)
                }
                val buttonClickListener = createOnButtonClickListener(terrierEntity)
                val imageClickListener = createOnImageClickListener(terrierEntity)
                bind(buttonClickListener,imageClickListener,terrierEntity)
            }
        }
    }

    /**
     * The MORE and GIVE UP buttons both display the Detail activity.
     */
    private fun createOnButtonClickListener(terriersTableEntity: TerriersTableEntity): View.OnClickListener {
        return View.OnClickListener {
            val fragmentTitle = terriersTableEntity.name
            val direction =
                TerriersFragmentDirections.actionMainFragmentToDetail(
                    terriersTableEntity,
                    fragmentTitle
                )
            it.findNavController().navigate(direction)
        }
    }

    private fun createOnImageClickListener(terriersTableEntity: TerriersTableEntity): View.OnClickListener {
        return View.OnClickListener {
            val imageFilePath = terriersTableEntity.name.toAssetPath()
            val direction =
                TerriersFragmentDirections.actionMainToFullsize(
                    terriersTableEntity.name,
                    imageFilePath
                )
            it.findNavController().navigate(direction)
        }
    }

    /**
     * Custom listener that handles clicks on [RecyclerView] items.  Passes the data
     * associated with the current item to the [onClick] function.
     * @param clickListener lambda that will be called with the current data
     * This listener displays a full-screen image.  It has nothing to do with the
     * three Buttons. Those have their own listeners.
     */
    class OnGuessClickListener(val clickListener: (terrierEntity: TerriersTableEntity, guess: String) -> Unit) {
        fun onGuessButtonClick(terrierEntity: TerriersTableEntity, guess: String)
                = clickListener(terrierEntity, guess)
    }
}

/**
 * Allows the RecyclerView to determine which items have changed
 * when the list has been updated.
 */
private class DiffCallback : DiffUtil.ItemCallback<TerriersTableEntity>() {
    override fun areItemsTheSame(oldItem: TerriersTableEntity, newItem: TerriersTableEntity): Boolean {
        return oldItem === newItem
    }

    override fun areContentsTheSame(oldItem: TerriersTableEntity, newItem: TerriersTableEntity): Boolean {
        return oldItem == newItem
    }
}
