package com.heske.terriertime.terriers

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.heske.terriertime.database.Terrier
import com.heske.terriertime.databinding.ListitemTerriersBinding
import kotlinx.android.synthetic.main.listitem_terrier.view.*

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
 * to present [List] of [Terrier] data, including computing diffs between lists.
 */
class TerriersRvAdapter(
    val onImageClickListener: OnImageClickListener,
    val onGiveUpClickListener: OnGiveUpClickListener,
    val onGuessClickListener: OnGuessClickListener,
    val onMoreClickListener: OnMoreClickListener
) :
    ListAdapter<Terrier, TerriersRvAdapter.TerrierViewHolder>(DiffCallback) {

    /**
     * The TerrierViewHolder constructor takes the binding variable from the associated
     * list item, which nicely gives it access to the full object information,
     * in this case it's a [Terrier] object.
     * See listitem_terrier.xml for all the views associated with each
     * row in terriers_recycler.
     *
     * binding.terrier - defined in listitem_terrier <data> block
     * terrierListItem - the object to be displayed in this row
     */
    class TerrierViewHolder(private var binding: ListitemTerriersBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(terrierListItem: Terrier) {
            binding.terrier = terrierListItem
            // This is important, because it forces the data binding to execute immediately,
            // which allows the RecyclerView to make the correct view size measurements
            binding.executePendingBindings()
        }
    }

    /**
     * Allows the RecyclerView to determine which items have changed
     * when the [List] of [Terrier] objects has been updated.
     */
    companion object DiffCallback : DiffUtil.ItemCallback<Terrier>() {
        override fun areItemsTheSame(oldItem: Terrier, newItem: Terrier): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Terrier, newItem: Terrier): Boolean {
            return oldItem == newItem
        }
    }

    /**
     * Create new [RecyclerView] item views (invoked by the layout manager)
     */
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TerrierViewHolder {
        val guess_text = parent.et_guess_txt
        return TerrierViewHolder(
            com.heske.terriertime.databinding.ListitemTerriersBinding
                .inflate(LayoutInflater.from(parent.context))
        )
    }

    /**
     * Replaces the contents of a view (invoked by the layout manager)
     * Set up listeners for all the buttons at index [position] in the [holder].
     * Button clicks will call back to [TerrierFragment] for processing.
     */
    override fun onBindViewHolder(holder: TerrierViewHolder, position: Int) {
        val terrierListItem = getItem(position)

        holder.itemView.apply {
            img_photo.setOnClickListener {
                onImageClickListener.onImageClick(terrierListItem)
            }
            btn_give_up.setOnClickListener {
                onGiveUpClickListener.onGiveUpButtonClick(terrierListItem)
            }
            btn_guess.setOnClickListener {
                val guess = holder.itemView.et_guess_txt.text.toString()
                onGuessClickListener.onGuessButtonClick(terrierListItem,guess)
            }
            btn_more.setOnClickListener {
                onMoreClickListener.onMoreButtonClick(terrierListItem)
            }
        }
        holder.bind(terrierListItem)
    }

    /**
     * Custom listener that handles clicks on [RecyclerView] items.  Passes the [Terrier]
     * associated with the current item to the [onClick] function.
     * @param clickListener lambda that will be called with the current [Terrier]
     * This listener displays a full-screen image.  It has nothing to do with the
     * three Buttons. Those have their own listeners.
     */
    class OnImageClickListener(val clickListener: (terrierListItem: Terrier) -> Unit) {
        fun onImageClick(terrierListItem: Terrier) = clickListener(terrierListItem)
    }

    class OnGiveUpClickListener(val clickListener: (terrierListItem: Terrier) -> Unit) {
        fun onGiveUpButtonClick(terrierListItem: Terrier) = clickListener(terrierListItem)
    }

    class OnMoreClickListener(val clickListener: (terrierListItem: Terrier) -> Unit) {
        fun onMoreButtonClick(terrierListItem: Terrier) = clickListener(terrierListItem)
    }

    class OnGuessClickListener(val clickListener: (terrierListItem: Terrier,guess: String) -> Unit) {
        fun onGuessButtonClick(terrierListItem: Terrier,guess: String)
                = clickListener(terrierListItem,guess)
    }
}