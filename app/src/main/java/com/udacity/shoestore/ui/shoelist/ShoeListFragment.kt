package com.udacity.shoestore.ui.shoelist

import android.os.Bundle
import android.view.*
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.bumptech.glide.Glide
import com.udacity.shoestore.R
import com.udacity.shoestore.databinding.FragmentShoeListBinding
import com.udacity.shoestore.databinding.ItemShoeBinding
import com.udacity.shoestore.models.Shoe
import timber.log.Timber

class ShoeListFragment : Fragment() {
    private lateinit var binding: FragmentShoeListBinding
    private lateinit var shoeListViewModel: ShoeStoreViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_shoe_list,
            container,
            false
        )
        setHasOptionsMenu(true)

        shoeListViewModel = ViewModelProvider(requireActivity()).get(ShoeStoreViewModel::class.java)
        shoeListViewModel.shoeListLiveData.observe(viewLifecycleOwner, Observer { shoeList ->
            updateUI(shoeList)
        })

        binding.fab.setOnClickListener {
            findNavController().navigate(ShoeListFragmentDirections.actionShoeListFragmentToShoeDetailFragment())
        }

        shoeListViewModel.shoeListLiveData.observe(viewLifecycleOwner, Observer { shoeList ->
            Timber.i("shoeList updated: $shoeList")
        })


        return binding.root
    }

    //            TODO @SimbaStart:     populate fragment with shoeList from our data class Shoe
    private fun updateUI(shoeList: List<Shoe>) {
        val shoeContainer: LinearLayout = binding.shoeContainer
        shoeContainer.removeAllViews()

        for (shoe in shoeList) {
            // Inflate a layout for each shoe
            val shoeItemBinding = ItemShoeBinding.inflate(layoutInflater, shoeContainer, false)
            shoeItemBinding.shoe = shoe
            shoeItemBinding.shoeListViewModel = shoeListViewModel

            // Add the shoe layout to the container
            shoeContainer.addView(shoeItemBinding.root)

            // Load images if any and add them to the shoe layout
            for (imageResource in shoe.images) {
                val shoeImageView = ImageView(requireContext())
                Glide.with(requireContext())
                    .load(imageResource)
                    .placeholder(R.drawable.shoe)
                    .error(R.drawable.shoe1)
                    .into(shoeImageView)
                shoeContainer.addView(shoeImageView)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.logout, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.loginFragment -> {
                // Navigate to the desired destination using the custom action
                findNavController().navigate(ShoeListFragmentDirections.actionShoeListFragmentToLoginFragment())
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }
}
