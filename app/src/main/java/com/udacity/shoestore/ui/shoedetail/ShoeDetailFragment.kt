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

        binding.saveNewshoeButton.setOnClickListener {
            Toast.makeText(requireContext(), "New shoe saved!", Toast.LENGTH_SHORT).show()
            saveNewShoe()
            findNavController().navigate(ShoeDetailFragmentDirections.actionShoeDetailFragmentToShoeListFragment())
        }

        binding.cancelButton.setOnClickListener {
            findNavController().navigate(ShoeDetailFragmentDirections.actionShoeDetailFragmentToShoeListFragment())
        }

        return binding.root
    }

    private fun saveNewShoe() {
        val shoeName = binding.enterShoenameEdtxt.text.toString()
        val shoeSize = binding.enterShoesizeEdtxt.text.toString().toDoubleOrNull()
        val company = binding.enterCompanyEdtxt.text.toString()
        val description = binding.enterDescriptionEdtxt.text.toString()
        shoeListViewModel.addNewShoe(shoeName, shoeSize ?: 0.0, company, description, listOf(R.drawable.shoe1))
        val newShoe: Shoe = Shoe(shoeName, shoeSize!!, company, description, listOf(R.drawable.shoe1))

        Timber.i("newShoe added is $newShoe")

        // Instead of updating ViewModel properties directly, you can set the shoe directly
//        shoeListViewModel.setSelectedShoe(newShoe)
    }
}
