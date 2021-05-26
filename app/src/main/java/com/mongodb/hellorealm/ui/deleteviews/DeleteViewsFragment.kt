package com.mongodb.hellorealm.ui.deleteviews

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
import com.mongodb.hellorealm.databinding.FragmentDeleteViewsBinding
import com.mongodb.hellorealm.hideKeyboard
import com.mongodb.hellorealm.ui.home.HomeViewModel

class DeleteViewsFragment : Fragment() {

    private val deleteViewModel by viewModels<DeleteViewsViewModels> {
        object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                val realmApp = (requireActivity().application as HelloRealmSyncApp).realmSync
                return DeleteViewsViewModels(realmApp) as T
            }
        }
    }

    private var _binding: FragmentDeleteViewsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDeleteViewsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btDeleteConfirm.setOnClickListener {
            hideKeyboard()
            deleteViewModel.deleteViewCount(binding.etViewCount.text.toString().toInt())
        }

        deleteViewModel.visitInfo.observe(viewLifecycleOwner) {
            binding.tvViewCount.text = resources.getString(R.string.update_view_count, it)
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}