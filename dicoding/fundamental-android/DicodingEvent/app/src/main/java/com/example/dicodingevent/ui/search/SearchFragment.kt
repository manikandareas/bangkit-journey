package com.example.dicodingevent.ui.search

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dicodingevent.databinding.FragmentSearchBinding
import com.example.dicodingevent.ui.bookmark.BookmarkViewModel
import com.example.dicodingevent.ui.common.ListAdapter
import com.google.android.material.snackbar.Snackbar

class SearchFragment : Fragment() {
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private val viewModel by activityViewModels<SearchViewModel>()
    private val bookmarkViewModel by activityViewModels<BookmarkViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setAutoFocusSV()
        setListEvents()
        initListener()
        observeViewModel()
    }


    private fun initListener() = with(binding) {
        svEvents.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    viewModel.findEvents(query)
                } else {
                    Toast.makeText(context, "Please input the keyword", Toast.LENGTH_SHORT).show()
                }

                svEvents.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }

        })
    }

    private fun setAutoFocusSV() {
        binding.svEvents.requestFocus()
        val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(binding.svEvents, InputMethodManager.SHOW_IMPLICIT)
    }

    private fun observeViewModel() {
        with(viewModel) {
            searchEvents.observe(viewLifecycleOwner) { fe ->

                if (fe == null || fe.listEvents.isEmpty()) {
                    Toast.makeText(context, "Oh no event not found ...", Toast.LENGTH_SHORT).show()
                    return@observe
                }

                val listAdapter = ListAdapter(fe, bookmarkViewModel.bookmarkHandler)

                Log.i("SearchFragment", fe.message)
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