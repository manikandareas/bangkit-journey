package com.manikandareas.devent.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.manikandareas.devent.R
import com.manikandareas.devent.databinding.ItemCarouselBinding
import com.manikandareas.devent.databinding.ItemListBinding
import com.manikandareas.devent.domain.models.EventModel
import com.manikandareas.devent.utils.DateHelper

class CarouselAdapter(val eventListener: CarouselListener? = null) :
    ListAdapter<EventModel, CarouselAdapter.ViewHolder>(this) {

    inner class ViewHolder(private val binding: ItemCarouselBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(eventData: EventModel) = with(binding) {
            ivEventCover.load(eventData.image) {
                crossfade(true)
                error(R.drawable.no_image_available)
            }

            tvEventTitle.text = eventData.name
            tvEventOrganizer.text = eventData.ownerName
            tvEventInformation.text = "\uD83D\uDCCD ${eventData.cityName}  \uD83D\uDDD3\uFE0F ${
                DateHelper.changeFormat(
                    "yyyy-MM-dd HH:mm:ss",
                    "dd MMM yyyy",
                    eventData.beginTime
                )
            }"

            setBtnFavoriteIcon(binding, eventData)
        }

        fun initListener(eventData: EventModel) {
            binding.carouselItemContainer.setOnClickListener {
                eventListener?.onItemClicked(eventData)
            }
            binding.btnFavorite.setOnClickListener {
                eventListener?.onFavoriteClicked(eventData)
                eventData.isFavorite = !eventData.isFavorite
                setBtnFavoriteIcon(binding, eventData)
            }
        }
    }

    private fun setBtnFavoriteIcon(binding: ItemCarouselBinding, eventData: EventModel) {
        binding.btnFavorite.setImageResource(
            if (eventData.isFavorite) R.drawable.ic_favorite
            else R.drawable.ic_favorite_border
        )
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemCarouselBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val eventData = getItem(holder.bindingAdapterPosition)
        holder.bind(eventData)
        holder.initListener(eventData)
    }

    companion object : DiffUtil.ItemCallback<EventModel>() {
        override fun areItemsTheSame(oldItem: EventModel, newItem: EventModel): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: EventModel, newItem: EventModel): Boolean =
            oldItem == newItem

        override fun getChangePayload(oldItem: EventModel, newItem: EventModel): Any? {
            val diffBundle = mutableListOf<ItemEventChangePayload>()

            if (oldItem.isFavorite != newItem.isFavorite) diffBundle.add(
                ItemEventChangePayload.ChangeFavorite(
                    newItem
                )
            )

            return diffBundle.ifEmpty { null }
        }
    }

    private sealed interface ItemEventChangePayload {
        data class ChangeFavorite(val eventData: EventModel) : ItemEventChangePayload
    }

    interface CarouselListener {
        fun onItemClicked(eventData: EventModel)
        fun onFavoriteClicked(eventData: EventModel)
    }

}