package com.udacity.shoestore.ui.shoelist

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.udacity.shoestore.models.Shoe

class ShoeStoreViewModel : ViewModel() {

    private var _shoeList: MutableList<Shoe> = mutableListOf()
//    val shoeList: LiveData<Shoe> get() = _shoeList


}