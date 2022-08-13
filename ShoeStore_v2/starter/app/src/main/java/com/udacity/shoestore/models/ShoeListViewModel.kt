package com.udacity.shoestore.models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ShoeListViewModel():ViewModel() {
    var shoes = MutableLiveData<MutableList<Shoe>>()
    var currentShoe = MutableLiveData<Shoe>()
    var name  = MutableLiveData<String>()
    var company  = MutableLiveData<String>()
    var size  = MutableLiveData<Double>()
    var description  = MutableLiveData<String>()
    init{
        shoes.value = mutableListOf<Shoe>()
        size.value=0.0
    }

    fun addNewShoe( ){
        println("1")
        println(name)
        println("2")
        println(company)
        println("3")
        println(size)
        println("4")
        println(description)
        currentShoe.value= Shoe(name.value!!,size.value!!,company.value!!,description.value!!)
      shoes.value?.add(currentShoe.value!!)
      println("size nasr     "+shoes.value?.size)
    }
}