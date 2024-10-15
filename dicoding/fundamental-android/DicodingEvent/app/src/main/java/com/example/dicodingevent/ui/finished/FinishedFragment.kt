package com.example.dicodingevent.ui.finished

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dicodingevent.databinding.FragmentFinishedBinding
import com.example.dicodingevent.ui.bookmark.BookmarkViewModel
import com.example.dicodingevent.ui.common.ListAdapter
import com.google.android.material.snackbar.Snackbar


class FinishedFragment : Fragment() {

    private var _binding: FragmentFinishedBinding? = null
    private val binding get() = _binding!!

    private val viewModel by activityViewModels<FinishedViewModel>()
    private val bookmarkViewModel by activityViewModels<BookmarkViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFinishedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListEvents()
        observeViewModel()
    }


    private fun observeViewModel() {
        with(viewModel) {
            finishedEvents.observe(viewLifecycleOwner) { fe ->
                val listAdapter = ListAdapter(fe!!, bookmarkViewModel.bookmarkHandler)

                Log.i("FinishedFragment", fe.message)
                binding.listEvents.adapter = listAdapter
            }

            isLoading.observe(viewLifecycleOwner) { isLoading ->
                binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            }

            error.observe(viewLifecycleOwner) { error ->
                if (error != null) {
                    Snackbar.make(binding.root, error, Snackbar.LENGTH_SHORT).show()
                }
            }
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