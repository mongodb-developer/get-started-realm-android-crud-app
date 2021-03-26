package com.mongodb.hellorealm.ui.addviews

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.mongodb.hellorealm.databinding.FragmentAddViewsBinding

class AddViewsFragment : Fragment() {

    private lateinit var addViewsViewModel: AddViewsViewModel
    private var _binding: FragmentAddViewsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        addViewsViewModel =
            ViewModelProvider(this).get(AddViewsViewModel::class.java)

        _binding = FragmentAddViewsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textGallery
        addViewsViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}