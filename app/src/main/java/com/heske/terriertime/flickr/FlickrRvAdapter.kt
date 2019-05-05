package com.heske.terriertime.flickr

import android.view.LayoutInflater
import android.view.ViewGroup
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
class FlickrRvAdapter(val onImageClickListener: OnImageClickListener) :

    ListAdapter<FlickrTableEntity, FlickrRvAdapter.FlickrRvAdapterViewHolder>(DiffCallback()) {

    /**
     * The FlickrRvAdapterViewHolder constructor takes the binding variable from the associated
     * GridViewItem, which nicely gives it access to the full object information,
     * in this case it's just a String.
     */
    class FlickrRvAdapterViewHolder(private var binding: ListitemFlickrImageBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(listItem: FlickrTableEntity) {
            binding.apply {
                // Binding variable name="flickr_image_url" is in listitem_flickr_image.xml.
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
        val listItem = getItem(position)

        holder.itemView.apply {
            setOnClickListener {
                onImageClickListener.onImageClick(listItem)
            }
            tag = listItem
        }
        holder.bind(listItem)
    }

    class OnImageClickListener(val clickListener: (imageUrl: FlickrTableEntity) -> Unit) {
        fun onImageClick(imageUrl: FlickrTableEntity) = clickListener(imageUrl)
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
