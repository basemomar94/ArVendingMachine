package com.ezfirecontrols.screensaverdemo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ezfirecontrols.screensaverdemo.databinding.ArItemBinding

class ArAdapter(val adsList: MutableList<ArItem>, val arInterface: ArInterface) :
    RecyclerView.Adapter<ArAdapter.ViewHolder>() {


    inner class ViewHolder(val binding: ArItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding?.root?.setOnClickListener {
                val item = adsList[adapterPosition]
                arInterface.viewDetails(item, adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ArItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val ad = adsList[position]
        holder.binding.itemPhoto.setImageURI(ad.image)
        if (!ad.isAvailable) {
            holder.binding.itemPhoto.visibility = View.GONE
        } else {
            holder.binding.itemPhoto.visibility = View.VISIBLE

        }
    }

    override fun getItemCount() = adsList.size

    interface ArInterface {
        fun viewDetails(arItem: ArItem, position: Int)
    }
}