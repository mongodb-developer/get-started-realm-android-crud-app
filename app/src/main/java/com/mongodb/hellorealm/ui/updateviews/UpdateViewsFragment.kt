package com.mongodb.hellorealm.ui.updateviews

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mongodb.hellorealm.HelloRealmSyncApp
import com.mongodb.hellorealm.R
import com.mongodb.hellorealm.databinding.FragmentAddViewsBinding
import com.mongodb.hellorealm.hideKeyboard

class UpdateViewsFragment : Fragment() {


    private val addViewsViewModel by viewModels<UpdateViewsViewModel> {
        object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                val realmApp = (requireActivity().application as HelloRealmSyncApp).realmSync
                return UpdateViewsViewModel(realmApp) as T
            }
        }
    }

    private var _binding: FragmentAddViewsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentAddViewsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btAddConfirm.setOnClickListener {
            hideKeyboard()
            addViewsViewModel.updateViewCount(binding.etViewCount.text.toString().toInt())
        }

        addViewsViewModel.visitInfo.observe(viewLifecycleOwner) {
            binding.tvViewCount.text = resources.getString(R.string.update_view_count, it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}