package com.manikandareas.devent.ui.fragment.upcoming

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.manikandareas.devent.databinding.FragmentUpcomingBinding
import com.manikandareas.devent.domain.models.EventModel
import com.manikandareas.devent.ui.activity.detail_event.DetailEventActivity
import com.manikandareas.devent.ui.adapter.EventListAdapter
import com.manikandareas.devent.utils.SpacingItemDecoration
import com.manikandareas.devent.utils.UIState
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class UpcomingFragment : Fragment() {

    private var _binding: FragmentUpcomingBinding? = null
    private val binding get() = _binding!!

    private val viewModel by activityViewModel<UpcomingViewModel>()

    private val upcomingEventListener by lazy {
        object : EventListAdapter.EventListListener {
            override fun onItemClicked(eventData: EventModel) {
                val detailIntent = Intent(context, DetailEventActivity::class.java)
                detailIntent.putExtra(DetailEventActivity.EXTRA_EVENT, eventData)
                startActivity(detailIntent)
            }

            override fun onFavoriteClicked(eventData: EventModel) {
                viewModel.updateEventFavorite(eventData)
            }
        }
    }

    private val upcomingEventAdapter by lazy { EventListAdapter(upcomingEventListener) }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUpcomingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObserver()
        initView()
        initListener()
    }

    override fun onStart() {
        super.onStart()
        viewModel.getUpcomingEvent()
    }

    private fun initObserver() = with(viewModel) {
        upcomingEvent.observe(viewLifecycleOwner) {
            when (it) {
                is UIState.Loading -> {
                    binding.upcomingLoading.visibility = View.VISIBLE
                }

                is UIState.Error -> {
                    binding.upcomingLoading.visibility = View.GONE
                    Log.d(TAG, "Error at upcoming  event observer $it")
                }

                is UIState.Success -> {
                    binding.upcomingLoading.visibility = View.GONE
                    upcomingEventAdapter.submitList(it.data)
                }
            }

        }

        toastEvent.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
        }
    }

    private fun initView() = with(binding) {
        rvUpcomingEvent.apply {
            adapter = upcomingEventAdapter
            addItemDecoration(SpacingItemDecoration(1, 40, true))
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun initListener() = with(binding) {
        btnBack.setOnClickListener { view ->
            view.findNavController().popBackStack()
        }
    }

    companion object {
        private const val TAG = "FragmentUpcoming"
    }
}