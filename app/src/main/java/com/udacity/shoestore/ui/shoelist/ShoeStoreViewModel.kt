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

    var eventNewShoeSaved = MutableLiveData<Boolean>()
//    val eventNewShoeSaved: LiveData<Boolean> get() = _eventNewShoeSaved

    var shoename = MutableLiveData<String>()
//    val shoeName: LiveData<String> get() = _shoename

    var shoecompany = MutableLiveData<String>()
//    val company: LiveData<String> get() = _company

    var shoesize = MutableLiveData<Double>()

//    val shoeSize: LiveData<Double> get() = _shoesize

    var shoedescription = MutableLiveData<String>()

//    val description: LiveData<String> get() = _description

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
        shoename.value = name
        shoecompany.value = company
        shoesize.value = size
        shoedescription.value = description
    }

    fun addNewShoe(name: String, size: Double, company: String, description: String, images: List<Int>) {
        val newShoe = Shoe(name, size, company, description, images)
        _shoeList.add(newShoe)
        _shoeListLiveData.value = _shoeList
        _selectedShoe.value = newShoe
    }

    fun saveNewShoe() {
        addNewShoe(
            shoename.value!!,
            shoesize.value ?: 0.0,
            shoecompany.value!!,
            shoedescription.value!!,
            listOf(R.drawable.shoe1) // TODO @SimbaStart:       placeholder image for newShoe
        )
//        TODO @SimbaStart:     check if new shoe added is logging
        val newShoe: Shoe = Shoe(
            shoename.value!!,
            shoesize.value ?: 0.0,
            shoecompany.value!!,
            shoedescription.value!!,
            listOf(R.drawable.shoe1)
        )
        Timber.i("newShoe added is $newShoe")

        eventNewShoeSaved.value = true
        Timber.i("***** _eventNewShoeSaved _eventNewShoeSaved is $eventNewShoeSaved")
    }

    fun newShoeSavedComplete() {
        eventNewShoeSaved.value = false
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

    init {
        defaultList()
    }
}
