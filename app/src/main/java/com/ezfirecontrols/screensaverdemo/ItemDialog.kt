package com.ezfirecontrols.screensaverdemo

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import com.ezfirecontrols.screensaverdemo.databinding.DialogItemBinding
import kotlinx.coroutines.delay

class ItemDialog(val arItem: ArItem, val itemPosition: Int, val itemDetailsInterface: ItemDetailsInterface) :
    DialogFragment() {

    var binding: DialogItemBinding? = null

    companion object {
        fun getInstance(arItem: ArItem, itemPosition: Int, itemDetailsInterface: ItemDetailsInterface) =
            ItemDialog(arItem, itemPosition, itemDetailsInterface)
    }

    override fun onStart() {
        super.onStart()
        val dialog: Dialog? = dialog
        if (dialog != null) {
            dialog.window?.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.WHITE))
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogItemBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupDetails()
        binding?.button3?.setOnClickListener {
//            binding?.button3?.playAnimation()
            lifecycleScope.launchWhenCreated {
              //  delay(2000)
                itemDetailsInterface.addToCart(arItem, itemPosition)
                dismiss()
            }


        }
    }


    private fun setupDetails() {
        arItem.apply {
            binding?.detailsImage?.setImageURI(this.image)
            binding?.detailsName?.text = this?.itemName
            binding?.detailsPrice?.text = this?.itemPrice.toString() + " $"

        }
    }

    interface ItemDetailsInterface {
        fun addToCart(arItem: ArItem, position: Int)
    }
}