package com.udacity.shoestore

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.udacity.shoestore.databinding.FragmentShoeDetailBinding
import com.udacity.shoestore.models.Shoe
import com.udacity.shoestore.models.ShoeListViewModel

class ShoeDetailFragment : Fragment() {

    lateinit var binding: FragmentShoeDetailBinding
    lateinit var navCtl: NavController
    lateinit var viewModel: ShoeListViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_shoe_detail, container, false)
        navCtl = container!!.findNavController()

        viewModel = ViewModelProvider(requireActivity()).get(ShoeListViewModel::class.java)

        binding.saveBtn.setOnClickListener {
            navCtl.navigate(ShoeDetailFragmentDirections.actionShoeDetailFragmentToShoeListFragment())
            addNewShoe()
        }

        binding.cancelBtn.setOnClickListener {
            navCtl.navigate(ShoeDetailFragmentDirections.actionShoeDetailFragmentToShoeListFragment())
        }
        return binding.root
    }
    fun addNewShoe() {
        val shoeName = binding.shoeName.text.toString()
        val company = binding.company.text.toString()
        var shoeSize:Double =0.0
        if (!(binding.shoeSize.text.toString().isEmpty())){
         shoeSize = binding.shoeSize.text.toString().toDouble()
        }
        val desc = binding.description.text.toString()
        val shoe:Shoe = Shoe(shoeName,shoeSize,company,desc)
        viewModel.addNewShoe(shoe)
    }
}