package com.machine.screensaverdemo

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.machine.screensaverdemo.databinding.CartItemBinding

class CartAdapter(val cartList: MutableList<ArItem>, val cartInterface: CartInterface) :
    RecyclerView.Adapter<CartAdapter.ViewHolder>() {


    inner class ViewHolder(val binding: CartItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.cartCancel.setOnClickListener {
                val item = cartList[adapterPosition]
                cartInterface.removeItem(item, adapterPosition)


            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = CartItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val cart = cartList[position]
      holder.binding.cartImage.setImageDrawable(cart.image)
        holder.binding.cartPrice.text = "$" + cart.itemPrice.toBigDecimal().setScale(2)
        holder.binding.cartTitle.text = cart.itemName

    }

    override fun getItemCount() = cartList.size

    interface CartInterface {
        fun removeItem(cartItem: ArItem, position: Int)
    }


}