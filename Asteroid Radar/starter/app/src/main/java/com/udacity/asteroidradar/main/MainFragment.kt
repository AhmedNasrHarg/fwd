package com.udacity.asteroidradar.main

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.udacity.asteroidradar.AsteroidAdapter
import com.udacity.asteroidradar.AsteroidListener
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.databinding.FragmentMainBinding

class MainFragment : Fragment() {

    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
    }

    lateinit var layoutManager:RecyclerView.LayoutManager
    lateinit var adapter:AsteroidAdapter
    lateinit var recyclerview: RecyclerView
    lateinit var navController: NavController

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding = FragmentMainBinding.inflate(inflater)
        binding.lifecycleOwner = this

        binding.viewModel = viewModel

        setHasOptionsMenu(true)

        layoutManager = LinearLayoutManager(context)
        recyclerview = RecyclerView(requireContext()!!)
        recyclerview.layoutManager = layoutManager
        navController = container!!.findNavController()

        adapter = AsteroidAdapter(AsteroidListener { asteroid ->
            //navigate
//            Toast.makeText(context,"hi",Toast.LENGTH_LONG).show()
            navController.navigate(MainFragmentDirections.actionShowDetail(asteroid))
        })
        binding.asteroidRecycler.adapter = adapter

        //todo: use picasso to show image of day
        viewModel.loadImageOfDay()
        //here: pass data to adapter and observer it
        //todo: add query parameters dynamically to get coming seven days as requested...
        viewModel.getAsteroid()
        //todo: after getting asteroid successfully, save it to DB. from repo tb3n not here...
        viewModel.asteroidList.observe(this.viewLifecycleOwner, Observer { asteroid ->
            adapter.data = asteroid
        })

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return true
    }
}
