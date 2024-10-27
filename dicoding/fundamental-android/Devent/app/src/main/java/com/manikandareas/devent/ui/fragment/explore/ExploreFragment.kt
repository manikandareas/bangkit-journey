package com.manikandareas.devent.ui.fragment.explore

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.annotation.OptIn
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.badge.ExperimentalBadgeUtils
import com.google.android.material.carousel.CarouselLayoutManager
import com.google.android.material.carousel.CarouselSnapHelper
import com.google.android.material.carousel.HeroCarouselStrategy
import com.google.android.material.search.SearchView.TransitionState
import com.manikandareas.devent.R
import com.manikandareas.devent.databinding.FragmentExploreBinding
import com.manikandareas.devent.domain.models.EventModel
import com.manikandareas.devent.ui.activity.detail_event.DetailEventActivity
import com.manikandareas.devent.ui.adapter.CarouselAdapter
import com.manikandareas.devent.ui.adapter.EventListAdapter
import com.manikandareas.devent.utils.SpacingItemDecoration
import com.manikandareas.devent.utils.UIState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.activityViewModel


class ExploreFragment : Fragment() {

    private var _binding: FragmentExploreBinding? = null
    private val binding get() = _binding!!

    private val viewModel by activityViewModel<ExploreViewModel>()

    private val snapHelper by lazy { CarouselSnapHelper() }

    private val upcomingEventListener by lazy {
        object : CarouselAdapter.CarouselListener {
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

    private val finishedEventListener by lazy {
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

    private val searchEventListener by lazy {
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

    private val upcomingEventAdapter by lazy { CarouselAdapter(upcomingEventListener) }

    private val finishedEventAdapter by lazy { EventListAdapter(finishedEventListener) }

    private val searchEventAdapter by lazy { EventListAdapter(searchEventListener) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentExploreBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObserver()
        initView()
        initListener()
    }

    override fun onStart() {
        super.onStart()
        viewModel.getFinishedEvent()
        viewModel.getUpcomingEvent()
        viewModel.getSizeFavorite()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.clearSearchEvent()
        _binding = null
    }

    private fun initListener() = with(binding) {
        btnViewAllFinished.setOnClickListener { view ->
            view.findNavController().navigate(R.id.navigation_finished)
        }

        btnViewAllUpcoming.setOnClickListener { view ->
            view.findNavController().navigate(R.id.navigation_upcoming)
        }

        btnPreferences.setOnClickListener { view ->
            view.findNavController().navigate(R.id.navigation_preferences)
        }

        ivFavorite.setOnClickListener { view ->
            view.findNavController().navigate(R.id.navigation_favorite)
        }

        svExplore.editText.setOnEditorActionListener { _, actionId, _ ->
            when (actionId) {
                EditorInfo.IME_ACTION_SEARCH -> {
                    val query = svExplore.text.toString().trim()
                    Log.d(TAG, "Search query: $query")

                    val quotaLeft =
                    lifecycleScope.launch(Dispatchers.Main) {
                        try {
                            sbExplore.setText(query)
                            viewModel.searchEventWithQuery(query)
                        } catch (e: Exception) {
                            Log.e(TAG, "Search error", e)
                        }
                    }
                    true
                }
                else -> false
            }
        }

        // Handle search view state changes
        svExplore.addTransitionListener { searchView, _, newState ->
            if (newState == TransitionState.HIDING) {
                lifecycleScope.launch(Dispatchers.Main) {
                    try {
                        if (searchView.text.isEmpty()) {
                            sbExplore.setText("")
                            searchView.setText("")
                            viewModel.clearSearchEvent()
                        }
                    } catch (e: Exception) {
                        Log.e(TAG, "Clear search error", e)
                    }
                }
            }
        }


    }

    @SuppressLint("UseCompatLoadingForDrawables", "SetTextI18n")
    private fun initObserver() {
        viewModel.upcomingEvent.observe(viewLifecycleOwner) {
            when (it) {
                is UIState.Loading -> {
                    binding.upcomingLoading.visibility = View.VISIBLE
                }

                is UIState.Error -> {
                    binding.upcomingLoading.visibility = View.GONE
                    Log.d(TAG, "Error at upcoming event observer $it")
                }

                is UIState.Success -> {
                    binding.upcomingLoading.visibility = View.GONE
                    upcomingEventAdapter.submitList(it.data)
                }
            }
        }

        viewModel.finishedEvent.observe(viewLifecycleOwner) {
            when (it) {
                is UIState.Loading -> {
                    binding.finishedLoading.visibility = View.VISIBLE
                }

                is UIState.Error -> {
                    binding.finishedLoading.visibility = View.GONE
                    Log.d(TAG, "Error at finished event observer $it")
                }

                is UIState.Success -> {
                    binding.finishedLoading.visibility = View.GONE
                    finishedEventAdapter.submitList(it.data)
                }
            }

        }

        viewModel.searchEvent.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UIState.Loading -> {
                    binding.apply {
                        searchLoading.visibility = View.VISIBLE
                        tvNoEvent.visibility = View.GONE
                        rvSearchEvent.visibility = View.GONE
                    }
                }

                is UIState.Error -> {
                    binding.apply {
                        searchLoading.visibility = View.GONE
                        tvNoEvent.visibility = View.VISIBLE
                        rvSearchEvent.visibility = View.GONE
                        tvNoEvent.text = "Error occurred while searching"
                    }
                }

                is UIState.Success -> {
                    binding.apply {
                        searchLoading.visibility = View.GONE

                        if (state.data.isEmpty() && svExplore.text.isNotEmpty()) {
                            tvNoEvent.visibility = View.VISIBLE
                            rvSearchEvent.visibility = View.GONE
                            tvNoEvent.text = "No events found"
                        } else {
                            tvNoEvent.visibility = View.GONE
                            rvSearchEvent.visibility = View.VISIBLE
                            searchEventAdapter.submitList(state.data)
                        }
                    }
                }
            }
        }

        viewModel.sizeFavorite.observe(viewLifecycleOwner) {
            if (it > 0) {
                binding.tvBadgeFavorite.text = it.toString()
                binding.tvBadgeFavorite.background =
                    resources.getDrawable(R.drawable.bg_badge_drawable)
            } else {
                binding.tvBadgeFavorite.text = ""
                binding.tvBadgeFavorite.background = null
            }
        }

        viewModel.toastEvent.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
        }


    }

    private fun initView() = with(binding) {
        snapHelper.attachToRecyclerView(rvUpcomingEvent)
        rvUpcomingEvent.layoutManager = CarouselLayoutManager().apply {
            setCarouselStrategy(HeroCarouselStrategy())
            carouselAlignment = CarouselLayoutManager.ALIGNMENT_CENTER
        }
        rvUpcomingEvent.adapter = upcomingEventAdapter

        rvFinishedEvent.apply {
            adapter = finishedEventAdapter
            addItemDecoration(SpacingItemDecoration(1, 40, true))
            layoutManager = LinearLayoutManager(context)
        }


        svExplore.setupWithSearchBar(binding.sbExplore)

        rvSearchEvent.apply {
            adapter = searchEventAdapter
            addItemDecoration(SpacingItemDecoration(1, 40, true))
            layoutManager = LinearLayoutManager(context).apply {
                // Reset state setiap kali search
                isSmoothScrollbarEnabled = true
            }

            // Optional: Tambahkan item animator
            itemAnimator = DefaultItemAnimator()
        }


    }

    companion object {
        private const val TAG = "ExploreFragment"
    }
}