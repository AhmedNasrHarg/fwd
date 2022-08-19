package com.udacity.asteroidradar

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.udacity.asteroidradar.databinding.AsteroidRowBinding

class AsteroidAdapter(val clickListener: AsteroidListener): RecyclerView.Adapter<AsteroidAdapter.ViewHolder>() {

    var data = listOf<Asteroid>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]
        holder.bind(item,clickListener)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder private constructor(val binding: AsteroidRowBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Asteroid,clickListener: AsteroidListener) {
//            binding.asteroidName.text=item.codename
//            binding.closeApproachDateId.text=item.closeApproachDate
//            if(item.isPotentiallyHazardous){
//                binding.icHazardous.setImageResource(R.drawable.ic_status_potentially_hazardous)
//            }else{
//                binding.icHazardous.setImageResource(R.drawable.ic_status_normal)
//            }
            binding.asteroid = item
            binding.clickListener=clickListener
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = AsteroidRowBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }
}
class AsteroidListener( val clickListener: ( asteroid: Asteroid ) -> Unit ){
    fun onClick(asteroid: Asteroid) = clickListener(asteroid)
}