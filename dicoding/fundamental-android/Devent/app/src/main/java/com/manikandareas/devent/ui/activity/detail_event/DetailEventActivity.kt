package com.manikandareas.devent.ui.activity.detail_event

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import coil.load
import com.manikandareas.devent.R
import com.manikandareas.devent.databinding.ActivityDetailEventBinding
import com.manikandareas.devent.domain.models.EventModel
import com.manikandareas.devent.utils.DateHelper
import com.manikandareas.devent.utils.StringUtils
import org.koin.androidx.viewmodel.ext.android.viewModel

class DetailEventActivity : AppCompatActivity() {
    private val binding by lazy { ActivityDetailEventBinding.inflate(layoutInflater) }

    private val viewModel by viewModel<DetailEventViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        getDataFromIntent().let {
            it?.let { event ->
               viewModel.setEvent(event)
            }
        }

        initObserver()
        initListener()
    }


    private fun getDataFromIntent(): EventModel? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(EXTRA_EVENT, EventModel::class.java)
        } else {
            intent.getParcelableExtra(EXTRA_EVENT)
        }
    }


    @SuppressLint("SetTextI18n")
    private fun initObserver() = with(binding) {
       viewModel.event.observe(this@DetailEventActivity) {event ->
           tvEventOrganizer.text = StringUtils.twoWordsFirst(event.ownerName)
           tvEventTitle.text = event.name
           tvEventDescription.text = StringUtils.fromHTML(event.description)
           tvEventSummary.text = event.summary
           tvEventQuota.text = if (event.quota - event.registrants ==0) "Sold Out" else (event.quota - event.registrants).toString()
           tvEventLocation.text = event.cityName

           val beginTime = DateHelper.changeFormat("yyyy-MM-dd HH:mm:ss", "dd MMM yyyy, HH:mm",
               event.beginTime
           )
           val endTime = DateHelper.changeFormat("yyyy-MM-dd HH:mm:ss", "HH:mm",
               event.endTime
           )

           tvEventInformation.text = "🗓️ $beginTime - $endTime"


           ivEventCover.load(event.image) {
               crossfade(true)
               error(R.drawable.no_image_available)
           }

           setFavoriteIcon(event.isFavorite)

           fabShowDetail.setOnClickListener {
               val intent = Intent(Intent.ACTION_VIEW)
               if (event != null) {
                   intent.data = Uri.parse(event.link)
               }
               startActivity(intent)
           }
           btnShare.setOnClickListener {
               val shareIntent = Intent(Intent.ACTION_SEND)
               shareIntent.type = "text/plain"
               shareIntent.putExtra(Intent.EXTRA_SUBJECT, event.name)
               shareIntent.putExtra(Intent.EXTRA_TEXT, event.link)
               startActivity(Intent.createChooser(shareIntent, "Share Event"))
           }
           fabFavorite.setOnClickListener {
               viewModel.updateFavoriteEvent(event)
               setFavoriteIcon(event.isFavorite)
           }
       }

       viewModel.toastEvent.observe(this@DetailEventActivity) { message ->
            Toast.makeText(this@DetailEventActivity, message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun initListener() = with(binding) {
        btnBack.setOnClickListener {
            finish()
        }
    }

    private fun setFavoriteIcon(isFavorite: Boolean) {
        if (isFavorite) {
            binding.fabFavorite.setImageResource(R.drawable.ic_favorite)
        } else {
            binding.fabFavorite.setImageResource(R.drawable.ic_favorite_border)
        }
    }


    companion object {
        const val EXTRA_EVENT = "extra_event"
    }
}