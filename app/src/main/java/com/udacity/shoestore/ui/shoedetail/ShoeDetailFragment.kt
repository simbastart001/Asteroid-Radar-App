package com.udacity.shoestore.ui.shoedetail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TimePicker
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.udacity.shoestore.R
import com.udacity.shoestore.databinding.FragmentShoeDetailBinding
import com.udacity.shoestore.models.Shoe
import com.udacity.shoestore.ui.shoelist.ShoeStoreViewModel
import timber.log.Timber

class ShoeDetailFragment : Fragment() {
    private lateinit var binding: FragmentShoeDetailBinding
    private lateinit var shoeListViewModel: ShoeStoreViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_shoe_detail,
            container,
            false
        )
        shoeListViewModel = ViewModelProvider(requireActivity()).get(ShoeStoreViewModel::class.java)
        binding.lifecycleOwner = requireActivity();

        shoeListViewModel.eventNewShoeSaved.observe(requireActivity(), Observer { isSaved ->
            Timber.i("eventNewShoeSaved value is $isSaved")
            if (isSaved) {
                saveComplete()
//                TODO @SimbaStart:     prevent memory leaks by setting the boolean to false again
                shoeListViewModel.newShoeSavedComplete()
            }
        })

        binding.cancelButton.setOnClickListener {
            findNavController().navigate(ShoeDetailFragmentDirections.actionShoeDetailFragmentToShoeListFragment())
        }

        return binding.root
    }

    fun saveComplete() {
        Toast.makeText(requireContext(), "New shoe saved!", Toast.LENGTH_SHORT).show()
        findNavController().navigate(ShoeDetailFragmentDirections.actionShoeDetailFragmentToShoeListFragment())
    }
}
