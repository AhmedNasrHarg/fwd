package com.udacity.shoestore.models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ShoeListViewModel():ViewModel() {
     var shoes= MutableLiveData<MutableList<Shoe>>()
    init{
        shoes.value = mutableListOf<Shoe>()
    }

    fun addNewShoe( shoe:Shoe){
      shoes.value?.add(shoe)
      println("size nasr     "+shoes.value?.size)
    }
}