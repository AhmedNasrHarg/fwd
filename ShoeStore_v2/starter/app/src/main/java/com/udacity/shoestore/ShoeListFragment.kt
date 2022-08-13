package com.udacity.shoestore

import android.graphics.Color
import android.os.Bundle
import android.text.InputType
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.LinearLayout
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.udacity.shoestore.databinding.FragmentShoeListBinding
import com.udacity.shoestore.models.Shoe
import com.udacity.shoestore.models.ShoeListViewModel

class ShoeListFragment : Fragment() {

    lateinit var binding: FragmentShoeListBinding
    lateinit var navCtl: NavController
    //1. using ViewModelProvider to get a shared viewModel
    //lateinit var viewModel: ShoeListViewModel
    //2. using activityViewModels(), you are getting a ViewModel that is scoped to the Activity.
    val viewModel: ShoeListViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_shoe_list, container, false)
        navCtl = container!!.findNavController()
        setHasOptionsMenu(true)

        //use requireActivity() so that view model data survive to activity life cycle not fragment
        // l2n if used for fragments data will not be shared. l2n fragment will destroy before the other get data
        ////1. using ViewModelProvider to get a shared viewModel
//        viewModel = ViewModelProvider(requireActivity()).get(ShoeListViewModel::class.java)

        viewModel.shoes.observe(viewLifecycleOwner, Observer{ newShoes ->
            renderShoes(newShoes)
        })

        binding.floatingActionButton.setOnClickListener {
            navCtl.navigate(ShoeListFragmentDirections.actionShoeListFragmentToShoeDetailFragment())
        }
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        navCtl.popBackStack(R.id.loginFragment, false)
         NavigationUI.onNavDestinationSelected(item!!,
            requireView()!!.findNavController())
                || super.onOptionsItemSelected(item)
       return true
    }

    fun renderShoes(newShoes: MutableList<Shoe>) {
        if (newShoes.size > 0) {
            for (i in 0..newShoes.size - 1) {
                val item = newShoes.get(i)
                if (item.name != "" && item.description != "" && item.company != "" && item.size != 0.0) {
    //                Toast.makeText(context,newShoes.get(i).name,Toast.LENGTH_LONG).show()
                    val parentLayout = binding.shoelist
                    val childlayout = LinearLayout(context)
                    childlayout.orientation = LinearLayout.HORIZONTAL
                    childlayout.layoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    )
                    childlayout.gravity = Gravity.CENTER
                    //create 4 views with data
                    //and add each to the childlayout
                    //1.name
                    val name = TextView(context)
                    name.text = item.name
                    val params = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.MATCH_PARENT
                    )
                    params.marginStart = 16
                    params.marginEnd = 16
                    params.bottomMargin = 16
                    params.topMargin = 16
                    name.textSize = 32.0f
    //                name.setTextColor(1)
                    name.layoutParams = params
                    childlayout.addView(name)

                    //2.company
                    val company = TextView(context)
                    company.text = item.company
                    company.textSize = 28.0f
    //                company.setTextColor(1)
                    company.layoutParams = params
                    childlayout.addView(company)

                    //3.size
                    val size = TextView(context)
                    size.text = item.size.toString()
                    size.textSize = 20.0f
    //                size.setTextColor(1)
                    size.layoutParams = params
                    childlayout.addView(size)

                    //add all to parent
                    parentLayout.addView(childlayout)

                    //4. description
                    val desc = TextView(context)
                    desc.setElegantTextHeight(true);
                    desc.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
                    desc.setSingleLine(false);
                    desc.text= item.description
                    val descParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    )
                    descParams.marginStart = 32
                    descParams.marginEnd = 32
                    desc.layoutParams = descParams
                    parentLayout.addView(desc)

                    //5. horizontal line
                    val line = View(context)
                    val lineParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT, 2
                    )
                    lineParams.marginStart = 16
                    lineParams.marginEnd = 16
                    line.layoutParams = lineParams
                    line.setBackgroundColor(
                        Color.parseColor("#000000")
                    )
                    parentLayout.addView(line)
                }
            }
        }
    }

}