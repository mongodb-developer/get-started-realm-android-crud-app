package com.mongodb.hellorealm.ui.deleteviews

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.mongodb.hellorealm.databinding.FragmentDeleteViewsBinding

class DeleteViewsFragment : Fragment() {

    private lateinit var deleteViewsViewModels: DeleteViewsViewModels
    private var _binding: FragmentDeleteViewsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        deleteViewsViewModels =
            ViewModelProvider(this).get(DeleteViewsViewModels::class.java)

        _binding = FragmentDeleteViewsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textSlideshow
        deleteViewsViewModels.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}