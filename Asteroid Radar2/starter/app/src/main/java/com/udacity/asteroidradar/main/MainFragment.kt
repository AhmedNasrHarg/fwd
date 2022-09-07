package com.udacity.asteroidradar.main

import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
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
import kotlinx.coroutines.*

 lateinit var lifecycleowner: LifecycleOwner
class MainFragment : Fragment() {

    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
    }

    lateinit var layoutManager:RecyclerView.LayoutManager
    lateinit var adapter:AsteroidAdapter
    lateinit var recyclerview: RecyclerView
    lateinit var navController: NavController

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding = FragmentMainBinding.inflate(inflater)
        binding.lifecycleOwner = this
        lifecycleowner = binding.lifecycleOwner!!
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

        //done: use picasso to show image of day
        CoroutineScope(Dispatchers.Main).launch {
            viewModel.loadImageOfDay()
            viewModel.getAsteroid()
        }

        //here: pass data to adapter and observer it
        //todo: add query parameters dynamically to get coming seven days as requested...
        //todo: after getting asteroid successfully, save it to DB. from repo tb3n not here...
        viewModel.asteroidList.observe(this.viewLifecycleOwner, Observer { asteroid ->
            if (asteroid !=null)
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
