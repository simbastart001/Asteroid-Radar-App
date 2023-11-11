package com.udacity.shoestore.ui.shoelist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.udacity.shoestore.R
import com.udacity.shoestore.models.Shoe
import timber.log.Timber

class ShoeStoreViewModel : ViewModel() {
    private var _shoeList: MutableList<Shoe> = mutableListOf()
    private val _shoeListLiveData = MutableLiveData<List<Shoe>>()
    val shoeListLiveData: LiveData<List<Shoe>>
        get() = _shoeListLiveData

    private var _shoename = MutableLiveData<String>()
    val shoeName: LiveData<String> get() = _shoename

    private var _company = MutableLiveData<String>()
    val company: LiveData<String> get() = _company


    private var _shoesize = MutableLiveData<Double>()
    val shoeSize: LiveData<Double> get() = _shoesize

    private var _description = MutableLiveData<String>()
    val description: LiveData<String> get() = _description

    private var _image: MutableList<Int> = mutableListOf()
    private val _imageLiveData = MutableLiveData<List<Int>>()
    val image: LiveData<List<Int>> get() = _imageLiveData

    private var _selectedShoe = MutableLiveData<Shoe>()
    val selectedShoe: LiveData<Shoe>
        get() = _selectedShoe

    fun addShoe(name: String, size: Double, company: String, description: String, images: List<Int>) {
        val newShoe = Shoe(name, size, company, description, images)
        _shoeList.add(newShoe)
        _shoeListLiveData.value = _shoeList.toList()
        _shoename.value = name
        _company.value = company
        _shoesize.value = size
        _description.value = description
    }

    fun addNewShoe(name: String, size: Double, company: String, description: String, images: List<Int>) {
        val newShoe = Shoe(name, size, company, description, images)
        _shoeList.add(newShoe)
        _shoeListLiveData.value = _shoeList.toList()
        _selectedShoe.value = newShoe
    }


    fun defaultList() {
        addShoe(
            "Soccer Shoe",
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
        return _shoeListLiveData.value!!
    }

    init {
        defaultList()
    }
}
