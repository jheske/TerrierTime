package com.heske.terriertime.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.heske.terriertime.databinding.FlickrImageRowItemBinding

/**
 * This class implements a [RecyclerView] [ListAdapter] which uses Data Binding to present [List]
 * data, including computing diffs between lists.
 * @param onClick a lambda that takes the
 */
class PhotoGridAdapter() :
        ListAdapter<String, PhotoGridAdapter.PhotoViewHolder>(DiffCallback) {
    /**
     * The PhotoViewHolder constructor takes the binding variable from the associated
     * GridViewItem, which nicely gives it access to the full object information,
     * in this case it's just a String.
     */
    class PhotoViewHolder(private var binding: FlickrImageRowItemBinding):
            RecyclerView.ViewHolder(binding.root) {
        fun bind(imageUrl: String) {
            binding.dogImageUrl = imageUrl
            // This is important, because it forces the data binding to execute immediately,
            // which allows the RecyclerView to make the correct view size measurements
            binding.executePendingBindings()
        }
    }

    /**
     * Allows the RecyclerView to determine which items have changed when the [List] of [MarsProperty]
     * has been updated.
     */
    companion object DiffCallback : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }
    }

    /**
     * Create new [RecyclerView] item views (invoked by the layout manager)
     */
    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): PhotoViewHolder {
        return PhotoViewHolder(
            FlickrImageRowItemBinding.inflate(
                LayoutInflater.from(
                    parent.context
                )
            )
        )
    }

    /**
     * Replaces the contents of a view (invoked by the layout manager)
     */
    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        val imageUrl = getItem(position)
// TODO Implement onClick to display FullSizeImageFragment
//        holder.itemView.setOnClickListener {
//            onClickListener.onClick(imageUrl)
//        }
        holder.bind(imageUrl)
    }

    /**
     * Custom listener that handles clicks on [RecyclerView] items.  Passes the [MarsProperty]
     * associated with the current item to the [onClick] function.
     * @param clickListener lambda that will be called with the current [MarsProperty]
     */
    class OnClickListener(val clickListener: (imageUrl:String) -> Unit) {
        fun onClick(imageUrl:String) = clickListener(imageUrl)
    }
}
