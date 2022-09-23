package com.machine.screensaverdemo

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.machine.screensaverdemo.databinding.ArItemBinding

class ArAdapter(
    val context: Context,
    val adsList: MutableList<ArItem>,
    val arInterface: ArInterface,
    val holderImage: Uri?
) :
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
        holder.binding.itemPhoto.setImageDrawable(ad.image)
        holder.binding.cakeName.text = ad.itemName
        if (!ad.isAvailable) {
            holder.binding.itemPhoto.visibility = View.INVISIBLE
        } else {
            holder.binding.itemPhoto.visibility = View.VISIBLE

        }
        if (holderImage != null) {
            holder.binding.itemHolder.setImageURI(holderImage)
        }
    }

    override fun getItemCount() = adsList.size

    interface ArInterface {
        fun viewDetails(arItem: ArItem, position: Int)
    }
}