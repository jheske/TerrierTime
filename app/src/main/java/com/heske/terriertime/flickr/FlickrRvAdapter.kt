package com.heske.terriertime.flickr

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.heske.terriertime.database.FlickrImage
import com.heske.terriertime.databinding.ListitemFlickrImageBinding

/**
 * This class implements a [RecyclerView] [ListAdapter] which uses Data Binding to present [List]
 * data, including computing diffs between lists.
 * @param onClick a lambda that takes the
 */
class FlickrRvAdapter :
    ListAdapter<FlickrImage, FlickrRvAdapter.FlickrRvAdapterViewHolder>(DiffCallback()) {

    /**
     * The FlickrRvAdapterViewHolder constructor takes the binding variable from the associated
     * GridViewItem, which nicely gives it access to the full object information.
     */
    class FlickrRvAdapterViewHolder(private var binding: ListitemFlickrImageBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(listener: View.OnClickListener, listItem: FlickrImage) {
            binding.apply {
                // Binding variables are in listitem_flickr_image.xml.
                clickListener = listener
                flickr = listItem
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

        getItem(position).let { flickrImage ->
            with(holder) {
                itemView.tag = flickrImage
                val clickListener = createOnClickListener(flickrImage)
                bind(clickListener, flickrImage)
            }
        }
    }

    private fun createOnClickListener(flickrImage: FlickrImage): View.OnClickListener {
        return View.OnClickListener {
            val direction =
                FlickrFragmentDirections.actionFlickrToFullsize(
                    flickrImage.breedName,
                    flickrImage.imageFilePath
                )
            it.findNavController().navigate(direction)
        }
    }
}

/**
 * Allows the RecyclerView to determine which items have changed when the [List] of
 * images has been updated.
 */
class DiffCallback : DiffUtil.ItemCallback<FlickrImage>() {
    override fun areItemsTheSame(oldItem: FlickrImage, newItem: FlickrImage): Boolean {
        return oldItem === newItem
    }

    override fun areContentsTheSame(oldItem: FlickrImage, newItem: FlickrImage): Boolean {
        return oldItem == newItem
    }
}
