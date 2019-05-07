package com.heske.terriertime.flickr

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.heske.terriertime.database.FlickrTableEntity
import com.heske.terriertime.databinding.ListitemFlickrImageBinding

/**
 * This class implements a [RecyclerView] [ListAdapter] which uses Data Binding to present [List]
 * data, including computing diffs between lists.
 * @param onClick a lambda that takes the
 */
class FlickrRvAdapter :
    ListAdapter<FlickrTableEntity, FlickrRvAdapter.FlickrRvAdapterViewHolder>(DiffCallback()) {

    /**
     * The FlickrRvAdapterViewHolder constructor takes the binding variable from the associated
     * GridViewItem, which nicely gives it access to the full object information,
     * in this case it's just a String.
     */
    class FlickrRvAdapterViewHolder(private var binding: ListitemFlickrImageBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(listener: View.OnClickListener,listItem: FlickrTableEntity) {
            binding.apply {
                // Binding variables are in listitem_flickr_image.xml.
                clickListener = listener
                flickrTableEntity = listItem
                executePendingBindings()
            }
        }
    }

    /**
     * Create new [RecyclerView] item views (invoked by the layout manager)
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
            FlickrRvAdapterViewHolder {
        return FlickrRvAdapterViewHolder(
            ListitemFlickrImageBinding
                .inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    /**
     * Replaces the contents of a view (invoked by the layout manager)
     */
    override fun onBindViewHolder(holder: FlickrRvAdapterViewHolder, position: Int) {

        getItem(position).let { flickrTableEntity ->
            with(holder) {
                itemView.tag = flickrTableEntity
                val listener = createOnClickListener(flickrTableEntity.breedName)
                bind(listener, flickrTableEntity)
            }
        }
    }

    private fun createOnClickListener(breedName: String): View.OnClickListener {
        return View.OnClickListener {
            val direction =
                FlickrFragmentDirections.actionFlickrToFullsize(breedName)
            it.findNavController().navigate(direction)
        }
    }
}

/**
 * Allows the RecyclerView to determine which items have changed when the [List] of
 * images has been updated.
 */
class DiffCallback : DiffUtil.ItemCallback<FlickrTableEntity>() {
    override fun areItemsTheSame(oldItem: FlickrTableEntity, newItem: FlickrTableEntity): Boolean {
        return oldItem === newItem
    }

    override fun areContentsTheSame(oldItem: FlickrTableEntity, newItem: FlickrTableEntity): Boolean {
        return oldItem == newItem
    }
}
