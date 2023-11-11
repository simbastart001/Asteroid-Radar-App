package com.udacity.shoestore.ui.shoelist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.udacity.shoestore.R
import com.udacity.shoestore.models.Shoe

class ShoeStoreViewModel : ViewModel() {
    private var _shoeList: MutableList<Shoe> = mutableListOf()
    val shoeListLiveData: LiveData<List<Shoe>>
        get() = _shoeListLiveData

    private val _shoeListLiveData = MutableLiveData<List<Shoe>>()

    fun addShoe(name: String, size: Double, company: String, description: String, images: List<Int>) {
        val newShoe = Shoe(name, size, company, description, images)
        _shoeList.add(newShoe)
        _shoeListLiveData.value = _shoeList.toList()
    }

    fun defaultList() {
        addShoe(
            "Sporty Shoe",
            9.5,
            "Nike",
            "Comfortable sports shoe",
            listOf(R.drawable.shoe, R.drawable.shoe2)
        )
        addShoe(
            "Casual Shoe",
            8.0,
            "Adidas",
            "Stylish casual shoe",
            listOf(R.drawable.shoe5, R.drawable.shoe8)
        )
    }

    // Function to get the list of shoes
    fun getShoeList(): List<Shoe> {
        return _shoeList
    }

    init {
        defaultList()
    }
}



