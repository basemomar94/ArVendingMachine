package com.machine.screensaverdemo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.machine.screensaverdemo.databinding.DialogCheckOutBinding

class CheckOutDialog(val cartList: MutableList<ArItem>, val checkOutInterface: CheckOutInterface) :
    DialogFragment(),
    CartAdapter.CartInterface {
    var binding: DialogCheckOutBinding? = null
    var cartAdapter: CartAdapter? = null

    companion object {
        fun getInstance(cartList: MutableList<ArItem>, checkOutInterface: CheckOutInterface) =
            CheckOutDialog(cartList, checkOutInterface)
    }

//    override fun onStart() {
//        super.onStart()
//        val dialog: BottomSheetDialog? = BottomSheetDialog(requireContext())
//        if (dialog != null) {
//            dialog.window?.setLayout(
//                ViewGroup.LayoutParams.MATCH_PARENT,
//                ViewGroup.LayoutParams.WRAP_CONTENT
//            )
//            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.WHITE))
//        }
//    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogCheckOutBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupCartRv()

    }

    private fun setupCartRv() {
        cartAdapter = CartAdapter(cartList, this)
        binding?.cartRv?.apply {
            adapter = cartAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)

        }
        binding?.total2?.text = " $" + cartList.sumOf { it.itemPrice }.toBigDecimal().setScale(2)


    }

    override fun removeItem(cartItem: ArItem, position: Int) {
        cartAdapter?.notifyItemRemoved(position)
        cartList.removeAt(position)
        binding?.total2?.text = "$" + cartList.sumOf { it.itemPrice }.toBigDecimal().setScale(2)
        checkOutInterface.updateRemovedItem(cartItem)
        if (cartList.size == 0) {
            dismiss()
        }


    }

    interface CheckOutInterface {
        fun updateRemovedItem(removedItem: ArItem)
    }
}