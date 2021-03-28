package com.mongodb.hellorealm.ui.deleteviews

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.mongodb.hellorealm.R
import com.mongodb.hellorealm.databinding.FragmentDeleteViewsBinding
import com.mongodb.hellorealm.hideKeyboard

class DeleteViewsFragment : Fragment() {

    private val deleteViewModel =
        ViewModelProvider.NewInstanceFactory().create(DeleteViewsViewModels::class.java)

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