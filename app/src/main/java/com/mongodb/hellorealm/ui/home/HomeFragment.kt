package com.mongodb.hellorealm.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.mongodb.hellorealm.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var homeViewModel =
        ViewModelProvider.NewInstanceFactory().create(HomeViewModel::class.java)

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
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
        homeViewModel.readData()

        val textView: TextView = binding.textHome
        homeViewModel.text.observe(viewLifecycleOwner, {
            textView.text = it
        })

        homeViewModel.visitInfo.observe(viewLifecycleOwner, {
            binding.textHomeSubtitle.text = "You have visited us $it times"
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}