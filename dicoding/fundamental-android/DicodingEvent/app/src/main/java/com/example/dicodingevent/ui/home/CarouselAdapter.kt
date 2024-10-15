package com.example.dicodingevent.ui.home

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
import com.example.dicodingevent.databinding.CarouselItemBinding
import com.example.dicodingevent.ui.common.ListAdapter
import com.example.dicodingevent.ui.detailEvent.DetailEventFragment


class CarouselAdapter(
    private val events: ListEvents,
    private val bookmarkListener: ListAdapter.BookmarkListener
) :
    RecyclerView.Adapter<CarouselAdapter.CarouselViewHolder>() {

    inner class CarouselViewHolder(val binding: CarouselItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarouselViewHolder {
        return CarouselViewHolder(
            CarouselItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = events.listEvents.size

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: CarouselViewHolder, position: Int) {
        Log.i("CarouselAdapter", "onBindViewHolder: ${events.listEvents[position]}")

        val event = events.listEvents[position]

        Glide.with(holder.itemView.context)
            .load(event.mediaCover) // URL Gambar
            .into(holder.binding.carouselImageView) // imageView mana yang

        holder.binding.eventName.text = event.name
        holder.binding.eventCityName.text = "\uD83D\uDCCD ${event.cityName}"
        holder.binding.eventOwnerName.text = event.ownerName

        setBookmarkIcon(holder, event)


        holder.binding.carouselItem.setOnClickListener { view ->
            val bundle = Bundle()
            bundle.putInt(DetailEventFragment.EXTRA_EVENT_ID, event.id)
            view.findNavController().navigate(R.id.navigation_detail_event, bundle)
        }

        holder.binding.actBookmark.setOnClickListener {
            bookmarkListener.onBookmarkClick(event)
            setBookmarkIcon(holder, event)
        }
    }

    private fun setBookmarkIcon(holder: CarouselViewHolder, event: Event) {
        if (bookmarkListener.isBookmarked(event)) {
            holder.binding.bookmarkIcon.setImageResource(R.drawable.baseline_bookmark_remove_24)
        } else {
            holder.binding.bookmarkIcon.setImageResource(R.drawable.baseline_bookmark_border_24)
        }
    }



}