package com.manikandareas.devent.ui.fragment.preferences

import android.Manifest
import android.app.UiModeManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import com.manikandareas.devent.databinding.FragmentPreferencesBinding
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class PreferencesFragment : Fragment() {

    private var _binding: FragmentPreferencesBinding? = null

    private val binding get() = _binding!!

    private val viewModel by activityViewModel<PreferencesViewModel>()

    private val notificationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            viewModel.setDailyReminderEnabled(true)
        } else {
            Toast.makeText(context, "Notification permission denied", Toast.LENGTH_SHORT).show()
            binding.switchReminder.isChecked = false
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPreferencesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initObserver()
        initListener()
    }

    private fun initObserver() {
        viewModel.isDarkMode.observe(viewLifecycleOwner) { isDarkMode ->
            binding.switchDarkMode.isChecked = isDarkMode

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val uiModeManager =
                    requireContext().getSystemService(Context.UI_MODE_SERVICE) as UiModeManager
                uiModeManager.setApplicationNightMode(
                    if (isDarkMode) UiModeManager.MODE_NIGHT_YES else UiModeManager.MODE_NIGHT_NO
                )
            } else {
                AppCompatDelegate.setDefaultNightMode(
                    if (isDarkMode) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
                )
            }
        }

        viewModel.isNotificationEnabled.observe(viewLifecycleOwner) { isEnabled ->
            binding.switchReminder.isChecked = isEnabled
        }
    }

    private fun initListener() {
        binding.switchDarkMode.setOnCheckedChangeListener { _, isChecked ->
            viewModel.setIsDarkMode(isChecked)
        }

        binding.switchReminder.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    requestNotificationPermission()
                } else {
                    viewModel.setDailyReminderEnabled(true)
                }
            } else {
                viewModel.setDailyReminderEnabled(false)
            }
        }

        binding.btnBack.setOnClickListener { view ->
            view.findNavController().popBackStack()
        }
    }

    private fun requestNotificationPermission() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            viewModel.setDailyReminderEnabled(true)
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            } else {
                viewModel.setDailyReminderEnabled(true)
            }
        }
    }


}