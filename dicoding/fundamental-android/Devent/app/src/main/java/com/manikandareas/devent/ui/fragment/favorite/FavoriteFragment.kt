package com.manikandareas.devent.ui.fragment.favorite

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.manikandareas.devent.databinding.FragmentFavoriteBinding
import com.manikandareas.devent.domain.models.EventModel
import com.manikandareas.devent.ui.activity.detail_event.DetailEventActivity
import com.manikandareas.devent.ui.adapter.EventListAdapter
import com.manikandareas.devent.utils.SpacingItemDecoration
import com.manikandareas.devent.utils.UIState
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class FavoriteFragment : Fragment() {

    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!

    private val viewModel by activityViewModel<FavoriteViewModel>()

    private val eventListAdapterListener by lazy {
        object : EventListAdapter.EventListListener {
            override fun onItemClicked(eventData: EventModel) {
                activity?.let {
                    val intent = Intent(it, DetailEventActivity::class.java)
                    intent.putExtra(DetailEventActivity.EXTRA_EVENT, eventData)
                    it.startActivity(intent)
                }
            }
            override fun onFavoriteClicked(eventData: EventModel) {
                viewModel.updateEventFavorite(eventData)

            }
        }
    }

    private val favoriteAdapter by lazy { EventListAdapter(eventListAdapterListener) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObserver()
        initListener()
        initView()
    }

    override fun onStart() {
        super.onStart()
        viewModel.getFavoriteEvent()
    }

    private fun initObserver() {
        viewModel.favoriteEvent.observe(viewLifecycleOwner) { state ->
            when(state) {
                is UIState.Error -> {
                    binding.favoriteLoading.visibility = View.GONE
                }
                UIState.Loading -> {
                    binding.favoriteLoading.visibility = View.VISIBLE
                }
                is UIState.Success -> {
                    binding.favoriteLoading.visibility = View.GONE
                        favoriteAdapter.submitList(state.data)
                    }

            }
        }

        viewModel.toastEvent.observe(viewLifecycleOwner) { message ->
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun initView() = with(binding) {
        rvFavoriteEvent.addItemDecoration(SpacingItemDecoration(1, 40, true))
        rvFavoriteEvent.adapter = favoriteAdapter
        rvFavoriteEvent.layoutManager = LinearLayoutManager(context)
    }

    private fun initListener() = with(binding) {
        btnBack.setOnClickListener { view ->
            view.findNavController().popBackStack()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}