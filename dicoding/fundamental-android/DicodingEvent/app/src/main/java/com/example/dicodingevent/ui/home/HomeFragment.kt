package com.example.dicodingevent.ui.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dicodingevent.R
import com.example.dicodingevent.databinding.FragmentHomeBinding
import com.example.dicodingevent.ui.bookmark.BookmarkViewModel
import com.example.dicodingevent.ui.common.ListAdapter
import com.google.android.material.carousel.CarouselLayoutManager
import com.google.android.material.carousel.CarouselSnapHelper
import com.google.android.material.carousel.HeroCarouselStrategy

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val homeViewModel by activityViewModels<HomeViewModel>()

    private val bookmarkViewModel by activityViewModels<BookmarkViewModel>()

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpcomingCarousel()
        setListEvents()
        observeViewModel()


        with(binding) {
            tvSeeAllUpcoming.setOnClickListener { view ->
                view.findNavController().navigate(R.id.navigation_upcoming)
            }
            tvSeeAllPastEvents.setOnClickListener { view ->
                view.findNavController().navigate(R.id.navigation_finished)
            }
            imgBookmark.setOnClickListener { view ->
                view.findNavController().navigate(R.id.navigation_bookmark)
            }
            actSearch.setOnClickListener { view ->
                view.findNavController().navigate(R.id.navigation_search)
            }
        }


    }


    private fun setUpcomingCarousel() {
        binding.upcomingCarousel.apply {
            setHasFixedSize(true)
            layoutManager = CarouselLayoutManager().apply {
                setCarouselStrategy(HeroCarouselStrategy())
                carouselAlignment = CarouselLayoutManager.ALIGNMENT_CENTER
            }
            CarouselSnapHelper().attachToRecyclerView(this)
        }
    }

    private fun setListEvents() {
        binding.listEvents.apply {
            layoutManager = LinearLayoutManager(context)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun observeViewModel() {
        homeViewModel.upcomingEvents.observe(viewLifecycleOwner) { listEvents ->
            val limitedEvents = listEvents?.listEvents?.take(5) ?: emptyList()
            val eventCount = listEvents?.listEvents?.size ?: 0
            binding.eventCount.text =
                "There are $eventCount${if (eventCount == 40) "+" else ""} interesting\nevents happening soon."

            if (limitedEvents.isNotEmpty()) {
                val carouselAdapter = CarouselAdapter(
                    listEvents!!.copy(listEvents = limitedEvents),
                    bookmarkViewModel.bookmarkHandler
                )
                binding.upcomingCarousel.adapter = carouselAdapter
            }
        }

        homeViewModel.pastEvents.observe(viewLifecycleOwner) { listEvents ->
            val listAdapter = ListAdapter(listEvents!!, bookmarkViewModel.bookmarkHandler)
            binding.listEvents.adapter = listAdapter
        }

        homeViewModel.upcomingEventsIsLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBarUpcomingEvents.visibility =
                if (isLoading) View.VISIBLE else View.GONE
        }
        homeViewModel.finishedEventsIsLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBarFinishedEvents.visibility =
                if (isLoading) View.VISIBLE else View.GONE
        }

        homeViewModel.error.observe(viewLifecycleOwner) { errorMessage ->
            if (errorMessage != null) {
                Log.e("HomeFragment", "Error: $errorMessage")
            }
        }

        bookmarkViewModel.toastMessage.observe(viewLifecycleOwner) { it ->
            it.getContentIfNotHandled()?.let { message ->
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            }
        }

        bookmarkViewModel.bookmarkedEvents.observe(viewLifecycleOwner) { evt ->
            if (evt != null) {
                if (evt.size > 0) {
                    binding.imgBookmark.setImageResource(R.drawable.baseline_bookmark_24)
                } else {
                    binding.imgBookmark.setImageResource(R.drawable.baseline_bookmark_border_24)
                }

            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}