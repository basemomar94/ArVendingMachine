package com.machine.screensaverdemo

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.machine.screensaverdemo.databinding.DialogArMachineBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ArMachineDialog(
//    val item1: Uri,
//    val item2: Uri,
//    val item3: Uri,
//    val item4: Uri,
//    val item5: Uri,
//    val item6: Uri,
    val spanNumber: Int,
    val totalCount: Int,
    val banner: String,
    val imageUri: Uri?,
    val fontSize: Float,
    val speed: Long,
    val fontColor: Int = R.color.black,
    val bannerColor: Int,
    val light: Float,
    val topMargin: Int,
    val interval: Long,
    val adScale: Int,
    val glass: Uri? = null,
    val holder: Uri? = null

) :
    DialogFragment(),
    ArAdapter.ArInterface, ItemDialog.ItemDetailsInterface, CartAdapter.CartInterface,
    CheckOutDialog.CheckOutInterface {
    var binding: DialogArMachineBinding? = null
    val adsList: MutableList<ArItem> = mutableListOf()
    val cartList: MutableList<ArItem> = mutableListOf()
    var arAdapter: ArAdapter? = null
    var cartAdapter: CartAdapter? = null
    private var movingTextAnimation: Animation? = null
    private var movingAd1Animation: Animation? = null
    private var movingAd2Animation: Animation? = null
    private var movingAd3Animation: Animation? = null
    val shelfItemsOne: MutableList<ArItem> = mutableListOf()
    val shelfItemsTwo: MutableList<ArItem> = mutableListOf()
    val shelfItemsThree: MutableList<ArItem> = mutableListOf()
    val shelfItemsFour: MutableList<ArItem> = mutableListOf()
    val shelfItemsFive: MutableList<ArItem> = mutableListOf()
    var shelfOneAdapter: ArAdapter? = null
    var shelfTwoAdapter: ArAdapter? = null
    var shelfThreeAdapter: ArAdapter? = null
    var shelfFourAdapter: ArAdapter? = null
    var shelfFiveAdapter: ArAdapter? = null


    companion object {
        fun getInstance(
//            item1: Uri,
//            item2: Uri,
//            item3: Uri,
//            item4: Uri,
//            item5: Uri,
//            item6: Uri,
            spanNumber: Int,
            totalCount: Int,
            banner: String,
            imageUri: Uri?,
            fontSize: Float,
            speed: Long,
            fontColor: Int = R.color.black,
            bannerColor: Int,
            light: Float,
            topMargin: Int,
            interval: Long,
            adScale: Int,
            glass: Uri?,
            holder: Uri?
        ) =
            ArMachineDialog(
//                item1, item2, item3, item4, item5, item6,
                spanNumber, totalCount, banner,
                imageUri,
                fontSize,
                speed,
                fontColor,
                bannerColor,
                light,
                topMargin,
                interval,
                adScale,
                glass,
                holder
            )
    }

    override fun onStart() {
        super.onStart()
        val dialog: Dialog? = dialog
        if (dialog != null) {
            dialog.window?.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.BLACK))
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogArMachineBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding?.cardView?.setBackgroundResource(R.drawable.cardbackground);

        movingTextAnimation = AnimationUtils.loadAnimation(requireContext(), R.anim.move)
        movingAd1Animation = AnimationUtils.loadAnimation(requireContext(), R.anim.move_ad_1)
        movingAd2Animation = AnimationUtils.loadAnimation(requireContext(), R.anim.move_ad_2)
        movingAd3Animation = AnimationUtils.loadAnimation(requireContext(), R.anim.move_ad_3)

        showCheckout()
        animationSetup()
        setUpTextAnimation()
        hidingLottie()
        setupAdOneAnimation()
//        setupAdTwoAnimation()
//        setupAdThreeAnimation()
        updateScreenSaver()
        //setupAd()
        updateBackGround()
        setupGlass()




        lifecycleScope.launchWhenCreated {
            //   setupItemsRv()
            setupCartRv()
            shelfOneRv()
            shelfTwoRv()
            shelfThreeRv()
            shelfFourRv()
            shelfFiveRv()
        }

    }

    private fun setupGlass() {
        if (glass != null) {
            binding?.glass?.setImageURI(glass)
        }
    }

    private fun setupCartRv() {
        cartAdapter = CartAdapter(cartList, this)
        binding?.cartRv?.apply {
            adapter = cartAdapter
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            setHasFixedSize(true)

        }


    }

    private fun setupItemsRv() {
//        val loopNum = (totalCount / 6).toInt()
//        adsList.add(ArItem(resources.getDrawable(R.drawable.stawbery1), false, "Strawberry", 5.50))
//        for (i in 1..loopNum) {
//            adsList.add(ArItem(resources.getDrawable(R.drawable.stawbery1), true, "Strawberry", 5.50))
//            adsList.add(ArItem(item3, true, "Red Velvet", 8.00))
//            adsList.add(ArItem(item2, true, "Strawberry", 5.50))
//            adsList.add(ArItem(item5, true, "Chocolate", 9.50))
//            if (i % 2 == 0) {
//                adsList.add(ArItem(item5, false, "Chocolate"))
//
//            }
//            adsList.add(ArItem(item6, true, "Chocolate", 9.50))
//            adsList.add(ArItem(item4, true, "Red Velvet", 8.00))
//
//
//        }
//        adsList.add(ArItem(item5, false, "Red Velvet"))
//        adsList.add(ArItem(item4, true, "Red Velvet", 8.00))
//        adsList.add(ArItem(item2, true, "Strawberry", 5.50))
//        adsList.add(ArItem(item5, true, "Chocolate", 9.50))
//        adsList.add(ArItem(item4, true, "Red Velvet", 8.00))


        binding?.adsRv?.apply {
            arAdapter = ArAdapter(requireContext(), adsList, this@ArMachineDialog, holder)
            adapter = arAdapter
            layoutManager = GridLayoutManager(requireContext(), spanNumber)
            setHasFixedSize(true)

        }
    }

    private fun shelfOneRv() {

        shelfItemsOne.add(
            ArItem(
                resources.getDrawable(R.drawable.vanila1),
                true,
                "Strawberry",
                5.50,
                1
            )
        )
        shelfItemsOne.add(
            ArItem(
                resources.getDrawable(R.drawable.vanila1),
                true,
                "Strawberry",
                5.50,
                1
            )
        )
        shelfItemsOne.add(
            ArItem(
                resources.getDrawable(R.drawable.chocalte1),
                true,
                "Chocolate",
                5.50,
                1
            )
        )
        shelfItemsOne.add(
            ArItem(
                resources.getDrawable(R.drawable.stawbery1),
                true,
                "Red Valet",
                5.50,
                1
            )
        )

        binding?.shelf1Rv?.apply {
            shelfOneAdapter =
                ArAdapter(requireContext(), shelfItemsOne, this@ArMachineDialog, holder)
            adapter = shelfOneAdapter
            layoutManager =
                LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
            setHasFixedSize(true)

        }
    }

    private fun shelfTwoRv() {
        shelfItemsTwo.add(
            ArItem(
                resources.getDrawable(R.drawable.vanila1),
                true,
                "Strawberry",
                5.50, 2
            )
        )
        shelfItemsTwo.add(
            ArItem(
                resources.getDrawable(R.drawable.chocalte1),
                true,
                "Chocolate",
                5.50,
                2
            )
        )
        shelfItemsTwo.add(
            ArItem(
                resources.getDrawable(R.drawable.stawbery1),
                true,
                "Red Valet",
                5.50,
                2
            )
        )
        shelfItemsTwo.add(
            ArItem(
                resources.getDrawable(R.drawable.vanila1),
                true,
                "Strawberry",
                5.50, 2

            )
        )

        binding?.shelf2Rv?.apply {
            shelfTwoAdapter =
                ArAdapter(requireContext(), shelfItemsTwo, this@ArMachineDialog, holder)
            adapter = shelfTwoAdapter
            layoutManager =
                LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
            setHasFixedSize(true)

        }
    }

    private fun shelfThreeRv() {
        shelfItemsThree.add(
            ArItem(
                resources.getDrawable(R.drawable.vanila1),
                true,
                "Strawberry",
                5.50, 3
            )
        )
        shelfItemsThree.add(
            ArItem(
                resources.getDrawable(R.drawable.chocalte1),
                true,
                "Chocolate",
                5.50, 3
            )
        )
        shelfItemsThree.add(
            ArItem(
                resources.getDrawable(R.drawable.stawbery1),
                true,
                "Red Valet",
                5.50, 3
            )
        )
        shelfItemsThree.add(
            ArItem(
                resources.getDrawable(R.drawable.vanila1),
                true,
                "Strawberry",
                5.50, 3
            )
        )

        binding?.shelf3Rv?.apply {
            shelfThreeAdapter =
                ArAdapter(requireContext(), shelfItemsThree, this@ArMachineDialog, holder)
            adapter = shelfThreeAdapter
            layoutManager =
                LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
            setHasFixedSize(true)

        }
    }

    private fun shelfFourRv() {
        shelfItemsFour.add(
            ArItem(
                resources.getDrawable(R.drawable.vanila1),
                true,
                "Strawberry",
                5.50, 4
            )
        )
        shelfItemsFour.add(
            ArItem(
                resources.getDrawable(R.drawable.chocalte1),
                true,
                "Chocolate",
                5.50, 4
            )
        )
        shelfItemsFour.add(
            ArItem(
                resources.getDrawable(R.drawable.stawbery1),
                true,
                "Red Valet",
                5.50, 4
            )
        )
        shelfItemsFour.add(
            ArItem(
                resources.getDrawable(R.drawable.vanila1),
                true,
                "Strawberry",
                5.50, 4
            )
        )

        binding?.shelf4Rv?.apply {
            shelfFourAdapter =
                ArAdapter(requireContext(), shelfItemsFour, this@ArMachineDialog, holder)
            adapter = shelfFourAdapter
            layoutManager =
                LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
            setHasFixedSize(true)

        }
    }

    private fun shelfFiveRv() {
        shelfItemsFive.add(
            ArItem(
                resources.getDrawable(R.drawable.vanila1),
                true,
                "Strawberry",
                5.50, 5
            )
        )
        shelfItemsFive.add(
            ArItem(
                resources.getDrawable(R.drawable.chocalte1),
                true,
                "Chocolate",
                5.50, 5
            )
        )
        shelfItemsFive.add(
            ArItem(
                resources.getDrawable(R.drawable.stawbery1),
                true,
                "Red Valet",
                5.50, 5
            )
        )
        shelfItemsFive.add(
            ArItem(
                resources.getDrawable(R.drawable.vanila1),
                true,
                "Strawberry",
                5.50, 5
            )
        )

        binding?.shelf5Rv?.apply {
            shelfFiveAdapter =
                ArAdapter(requireContext(), shelfItemsFive, this@ArMachineDialog, holder)
            adapter = shelfFiveAdapter
            layoutManager =
                LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
            setHasFixedSize(true)

        }
    }

    override fun viewDetails(arItem: ArItem, position: Int) {
        // hideTicker()
        binding?.animationView?.visibility = View.GONE
        if (arItem.isAvailable) {
            ItemDialog.getInstance(arItem, position, this)
                .show(requireActivity().supportFragmentManager, ItemDialog::class.java.simpleName)
        }


    }

    override fun addToCart(arItem: ArItem, position: Int) {
        arItem.isAvailable = false
        binding?.cartLayout?.visibility = View.VISIBLE

        when (arItem.shelf) {
            1-> shelfOneAdapter?.notifyItemChanged(position)
            2-> shelfTwoAdapter?.notifyItemChanged(position)
            3-> shelfThreeAdapter?.notifyItemChanged(position)
            4-> shelfFourAdapter?.notifyItemChanged(position)
            5-> shelfFiveAdapter?.notifyItemChanged(position)
        }

        cartList.add(0, arItem)
        cartAdapter?.notifyItemInserted(0)
        binding?.total?.text = " $" + cartList.sumOf { it.itemPrice }.toBigDecimal().setScale(2)

    }

    override fun removeItem(cartItem: ArItem, position: Int) {
        cartList.removeAt(position)
        Log.d("list size", cartList.size.toString())
        if (cartList.size < 0) {
            binding?.cartLayout?.visibility = View.GONE
        }
        cartAdapter?.notifyItemRemoved(position)
        val index: Int = when (cartItem.shelf) {
            1 -> shelfItemsOne.indexOf(cartItem)
            2 -> shelfItemsTwo.indexOf(cartItem)
            3 -> shelfItemsThree.indexOf(cartItem)
            4 -> shelfItemsFour.indexOf(cartItem)
            else -> shelfItemsFive.indexOf(cartItem)
        }

        when (cartItem.shelf) {
            1 -> {
                // shelfItemsOne.add(index, cartItem)
                val vendingItem = shelfItemsOne[index]
                vendingItem.isAvailable = true
                cartAdapter?.notifyItemChanged(index)
            }
            2 -> {
                // shelfItemsTwo.add(index,cartItem)
                val vendingItem = shelfItemsTwo[index]
                vendingItem.isAvailable = true
                cartAdapter?.notifyItemChanged(index)
            }
            3 -> {
                val vendingItem = shelfItemsThree[index]
                vendingItem.isAvailable = true
                // shelfItemsThree.add(index,cartItem)
                cartAdapter?.notifyItemChanged(index)
            }
            4 -> {
                val vendingItem = shelfItemsFour[index]
                vendingItem.isAvailable = true
                //  shelfItemsFour.add(index,cartItem)
                cartAdapter?.notifyItemChanged(index)
            }
            5 -> {
                val vendingItem = shelfItemsFive[index]
                vendingItem.isAvailable = true
                // shelfItemsFive.add(index,cartItem)
                cartAdapter?.notifyItemChanged(index)
            }
        }

        binding?.total?.text = " $" + cartList.sumOf { it.itemPrice }.toBigDecimal().setScale(2)

    }

    private fun showCheckout() {
        binding?.button5?.setOnClickListener {
            CheckOutDialog.getInstance(cartList, this).show(
                requireActivity().supportFragmentManager,
                CheckOutDialog::class.java.simpleName
            )
        }
    }


    private fun setUpTextAnimation() {
        binding?.typeAnimation?.startAnimation(movingTextAnimation)
        movingTextAnimation?.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(p0: Animation?) {


            }

            override fun onAnimationEnd(p0: Animation?) {
                binding?.imageAd1?.startAnimation(movingAd1Animation)

            }

            override fun onAnimationRepeat(p0: Animation?) {
            }
        })
    }

    private fun setupAdOneAnimation() {
        movingAd1Animation?.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(p0: Animation?) {
                lifecycleScope.launch {
                    delay(interval)
                    binding?.typeAnimation?.startAnimation(movingTextAnimation)

                }

            }

            override fun onAnimationEnd(p0: Animation?) {
            }

            override fun onAnimationRepeat(p0: Animation?) {

            }
        })

    }

//    private fun setupAdTwoAnimation() {
//        movingAd2Animation?.setAnimationListener(object : Animation.AnimationListener {
//            override fun onAnimationStart(p0: Animation?) {
//                lifecycleScope.launch {
//                    delay(interval)
//                    binding?.imageAd3?.startAnimation(movingAd3Animation)
//
//
//                }
//
//            }
//
//            override fun onAnimationEnd(p0: Animation?) {
//            }
//
//            override fun onAnimationRepeat(p0: Animation?) {
//
//            }
//        })
//
//    }
//
//    private fun setupAdThreeAnimation() {
//        movingAd3Animation?.setAnimationListener(object : Animation.AnimationListener {
//            override fun onAnimationStart(p0: Animation?) {
//
//            }
//
//            override fun onAnimationEnd(p0: Animation?) {
//                binding?.typeAnimation?.startAnimation(movingTextAnimation)
//            }
//
//            override fun onAnimationRepeat(p0: Animation?) {
//
//            }
//        })
//
//    }


    private fun animationSetup() {
        movingTextAnimation?.duration = speed
        movingAd1Animation?.duration = speed
//        movingAd2Animation?.duration = speed
//        movingAd3Animation?.duration = speed

    }


    private fun updateScreenSaver() {

        binding?.typeAnimation?.text = banner
        binding?.typeAnimation?.textSize = fontSize
        //  binding?.backGround?.setImageURI(imageUri)
        binding?.typeAnimation?.setTextColor(fontColor!!)
        binding?.banner?.setBackgroundColor(bannerColor!!)
        val param = binding?.banner?.layoutParams as ViewGroup.MarginLayoutParams
        param.setMargins(0, 2 * topMargin, 0, 0)

        binding?.imageAd1?.requestLayout()
//        binding?.imageAd2?.requestLayout()
//        binding?.imageAd3?.requestLayout()

        binding?.banner?.post {
            val overlapsHeight = (binding?.banner?.height?.times(adScale))?.toInt()

            binding?.banner?.layoutParams = param
            binding?.imageAd1?.layoutParams?.height = overlapsHeight
//            binding?.imageAd2?.layoutParams?.height = overlapsHeight
//            binding?.imageAd3?.layoutParams?.height = overlapsHeight


        }
        //  binding?.imageAd1?.layoutParams?.width = (binding?.imageAd1?.layoutParams?.height?.times(1.5))?.toInt()

    }

//    private fun setupAd() {
////        item1.apply {
////            if (this != null) {
////                binding?.imageAd1?.setImageURI(this)
////            }
////        }
//
//        item2.apply {
//            if (this != null) {
//                //    binding?.imageAd2?.setImageURI(this)
//            }
//        }
//
//        item3.apply {
//            if (this != null) {
//                //   binding?.imageAd3?.setImageURI(this)
//            }
//        }
//    }

    private fun hideTicker() {

        val param = binding?.banner?.layoutParams as ViewGroup.MarginLayoutParams
        param.setMargins(0, 48, 0, 0)
        binding?.banner?.post {
            binding?.banner?.layoutParams = param


        }

    }

    override fun updateRemovedItem(removedItem: ArItem) {
        val index: Int = when (removedItem.shelf) {
            1 -> shelfItemsOne.indexOf(removedItem)
            2 -> shelfItemsTwo.indexOf(removedItem)
            3 -> shelfItemsThree.indexOf(removedItem)
            4 -> shelfItemsFour.indexOf(removedItem)
            else -> shelfItemsFive.indexOf(removedItem)
        }

        when (removedItem.shelf) {
            1 -> {
                // shelfItemsOne.add(index, cartItem)
                val vendingItem = shelfItemsOne[index]
                vendingItem.isAvailable = true
                shelfOneAdapter?.notifyItemChanged(index)
            }
            2 -> {
                // shelfItemsTwo.add(index,cartItem)
                val vendingItem = shelfItemsTwo[index]
                vendingItem.isAvailable = true
                shelfTwoAdapter?.notifyItemChanged(index)
            }
            3 -> {
                val vendingItem = shelfItemsThree[index]
                vendingItem.isAvailable = true
                // shelfItemsThree.add(index,cartItem)
                shelfThreeAdapter?.notifyItemChanged(index)
            }
            4 -> {
                val vendingItem = shelfItemsFour[index]
                vendingItem.isAvailable = true
                //  shelfItemsFour.add(index,cartItem)
                shelfFourAdapter?.notifyItemChanged(index)
            }
            5 -> {
                val vendingItem = shelfItemsFive[index]
                vendingItem.isAvailable = true
                // shelfItemsFive.add(index,cartItem)
                shelfFiveAdapter?.notifyItemChanged(index)
            }
        }
    }

    private fun updateBackGround() {
        if (imageUri != null) {
            binding?.backGround?.setImageURI(imageUri)
        }
    }

    private fun hidingLottie() {
        binding?.animationView?.setOnClickListener {
            it.visibility = View.GONE

        }
    }
}