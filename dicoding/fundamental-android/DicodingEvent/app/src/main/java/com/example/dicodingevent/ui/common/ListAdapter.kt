package com.example.dicodingevent.ui.common

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.dicodingevent.R
import com.example.dicodingevent.data.response.Event
import com.example.dicodingevent.data.response.ListEvents
import com.example.dicodingevent.databinding.ListItemBinding
import com.example.dicodingevent.ui.detailEvent.DetailEventFragment

class ListAdapter(private val events: ListEvents, private val bookmarkListener: BookmarkListener) :
    RecyclerView.Adapter<ListAdapter.ListViewHolder>() {

    inner class ListViewHolder(val binding: ListItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        return ListViewHolder(
            ListItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = events.listEvents.size

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        Log.i("ListAdapter", "onBindViewHolder: ${events.listEvents[position]}")

        val event = events.listEvents[position]

        Glide.with(holder.itemView.context)
            .load(event.imageLogo) // URL Gambar
            .into(holder.binding.imgItemPhoto) // imageView mana yang

        holder.binding.eventName.text = event.name
        holder.binding.eventCityName.text = "\uD83D\uDCCD ${event.cityName}"
        holder.binding.eventOwnerName.text = event.ownerName

        holder.binding.listItem.setOnClickListener { view ->
            val bundle = Bundle()
            bundle.putInt(DetailEventFragment.EXTRA_EVENT_ID, event.id)

            view.findNavController().navigate(R.id.navigation_detail_event, bundle)
        }

        setBookmarkIcon(holder, event)


        holder.binding.actBookmark.setOnClickListener {
            bookmarkListener.onBookmarkClick(event)
            setBookmarkIcon(holder, event)
        }

    }

    private fun setBookmarkIcon(holder: ListViewHolder, event: Event) {
        if (bookmarkListener.isBookmarked(event)) {
            holder.binding.bookmarkIcon.setImageResource(R.drawable.baseline_bookmark_remove_24)
        } else {
            holder.binding.bookmarkIcon.setImageResource(R.drawable.baseline_bookmark_border_24)
        }
    }

    interface BookmarkListener {
        fun onBookmarkClick(event: Event)
        fun isBookmarked(event: Event): Boolean
    }

}