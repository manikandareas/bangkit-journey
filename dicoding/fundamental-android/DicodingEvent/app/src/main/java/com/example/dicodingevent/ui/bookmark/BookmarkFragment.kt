package com.example.dicodingevent.ui.bookmark

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dicodingevent.data.response.ListEvents
import com.example.dicodingevent.databinding.FragmentBookmarkBinding
import com.example.dicodingevent.ui.common.ListAdapter

class BookmarkFragment : Fragment() {

    private var _binding: FragmentBookmarkBinding? = null
    private val binding get() = _binding!!

    private val bookmarkViewModel by activityViewModels<BookmarkViewModel>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBookmarkBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListEvents()
        observeViewModel()
    }

    private fun observeViewModel() {
        bookmarkViewModel.bookmarkedEvents.observe(viewLifecycleOwner) { evt ->
            val listEvents = evt?.toList() ?: emptyList()
            val listAdapter =
                ListAdapter(ListEvents(listEvents, false, ""), bookmarkViewModel.bookmarkHandler)
            binding.listEvents.adapter = listAdapter
        }

        bookmarkViewModel.toastMessage.observe(viewLifecycleOwner) { it ->
            it.getContentIfNotHandled()?.let { message ->
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setListEvents() {
        binding.listEvents.apply {
            layoutManager = LinearLayoutManager(context)
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}