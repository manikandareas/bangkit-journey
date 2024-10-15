package com.example.dicodingevent.ui.detailEvent

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.dicodingevent.databinding.FragmentDetailEventBinding
import com.example.dicodingevent.ui.common.Utils
import com.google.android.material.snackbar.Snackbar

class DetailEventFragment : Fragment() {


    private lateinit var viewModel: DetailEventViewModel
    private var _binding: FragmentDetailEventBinding? = null
    private val binding get() = _binding!!

    companion object {
        const val EXTRA_EVENT_ID = "extra_event_id"
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentDetailEventBinding.inflate(inflater, container, false)


        val eventID = arguments?.getInt(EXTRA_EVENT_ID)!!

        val factory = DetailEventViewModelFactory(eventID)
        viewModel = ViewModelProvider(this, factory)[DetailEventViewModel::class.java]

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()
    }

    @SuppressLint("QueryPermissionsNeeded", "SetTextI18n")
    private fun observeViewModel() {
        viewModel.event.observe(viewLifecycleOwner) { res ->
            val event = res?.event
            binding.eventName.text = event?.name
            binding.eventDate.text = event?.beginTime
            binding.eventLocation.text = event?.cityName
            binding.eventSummary.text = event?.summary
            binding.categoryEvent.text = event?.category

            binding.eventParticipant.text = "${event?.registrants} / ${event?.quota} Participants"
            binding.eventOwnerName.text = event?.ownerName


            Utils.renderHtmlString(binding.eventDescription, event?.description ?: "")
            Glide.with(this)
                .load(event?.mediaCover!!) // URL Gambar
                .into(binding.eventImage) // imageView mana yang


            binding.btnMore.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(event.link)
                if (intent.resolveActivity(requireActivity().packageManager) != null) {
                    // Menjalankan intent untuk membuka link di browser
                    startActivity(intent)
                } else {
                    // Tangani jika tidak ada browser yang dapat menangani intent
                    Toast.makeText(requireContext(), "No browser found", Toast.LENGTH_SHORT).show()
                }
            }
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        viewModel.error.observe(viewLifecycleOwner) { error ->
            if (error != null) {
                Snackbar.make(binding.root, error, Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}