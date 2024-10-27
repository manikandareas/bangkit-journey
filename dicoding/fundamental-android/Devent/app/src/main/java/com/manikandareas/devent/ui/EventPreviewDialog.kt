package com.manikandareas.devent.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import coil.load
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.manikandareas.devent.R
import com.manikandareas.devent.databinding.EventPreviewDialogBinding
import com.manikandareas.devent.databinding.FragmentExploreBinding
import com.manikandareas.devent.domain.models.EventModel

class EventPreviewDialog(val eventData: EventModel) : BottomSheetDialogFragment() {

    private var _binding: EventPreviewDialogBinding? = null

    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = EventPreviewDialogBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
    }

    private fun initView() = with(binding) {
        tvEventTitle.text = eventData.name

        ivEventCover.load(eventData.image) {
            crossfade(true)
            error(R.drawable.ic_me)
        }
    }

    companion object {
        const val TAG = "EventPreviewDialog"
    }
}