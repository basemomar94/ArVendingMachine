package com.ezfirecontrols.screensaverdemo

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
import androidx.recyclerview.widget.LinearLayoutManager
import com.ezfirecontrols.screensaverdemo.databinding.DialogScreenSaverBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ScreenSaveDialog(
    val banner: String,
    val imageUri: Uri?,
    val fontSize: Float,
    val speed: Long,
    val fontColor: Int = R.color.black,
    val bannerColor: Int,
    val light: Float,
    val topMargin: Int,
    val ad1Uri: Uri?,
    val ad2Uri: Uri?,
    val ad3Uri: Uri?,
    val interval: Long,
    val adScale: Int
) :
    DialogFragment() {
    val TAG = this::class.java.simpleName
    var binding: DialogScreenSaverBinding? = null
    private var movingTextAnimation: Animation? = null
    private var movingAd1Animation: Animation? = null
    private var movingAd2Animation: Animation? = null
    private var movingAd3Animation: Animation? = null


    companion object {
        fun getInstance(
            banner: String,
            imageUri: Uri?,
            fontSize: Float,
            speed: Long,
            fontColor: Int = R.color.black,
            bannerColor: Int,
            light: Float,
            topMargin: Int,
            ad1Uri: Uri?,
            ad2Uri: Uri?,
            ad3Uri: Uri?,
            interval: Long,
            adScale: Int
        ) =
            ScreenSaveDialog(
                banner,
                imageUri,
                fontSize,
                speed,
                fontColor,
                bannerColor,
                light,
                topMargin,
                ad1Uri,
                ad2Uri,
                ad3Uri,
                interval,
                adScale
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
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogScreenSaverBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dismissDialog()
        updateScreenSaver()
        Log.d(TAG, "$fontSize")
        movingTextAnimation = AnimationUtils.loadAnimation(requireContext(), R.anim.move)
        movingAd1Animation = AnimationUtils.loadAnimation(requireContext(), R.anim.move_ad_1)
        movingAd2Animation = AnimationUtils.loadAnimation(requireContext(), R.anim.move_ad_2)
        movingAd3Animation = AnimationUtils.loadAnimation(requireContext(), R.anim.move_ad_3)


        //animations setup
        animationSetup()
        setUpTextAnimation()
        setupAdOneAnimation()
        setupAdTwoAnimation()
        setupAdThreeAnimation()
        //setupRv()
        setupAd()

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
                    binding?.imageAd2?.startAnimation(movingAd2Animation)

                }

            }

            override fun onAnimationEnd(p0: Animation?) {
            }

            override fun onAnimationRepeat(p0: Animation?) {

            }
        })

    }

    private fun setupAdTwoAnimation() {
        movingAd2Animation?.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(p0: Animation?) {
                lifecycleScope.launch {
                    delay(interval)
                    binding?.imageAd3?.startAnimation(movingAd3Animation)


                }

            }

            override fun onAnimationEnd(p0: Animation?) {
            }

            override fun onAnimationRepeat(p0: Animation?) {

            }
        })

    }

    private fun setupAdThreeAnimation() {
        movingAd3Animation?.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(p0: Animation?) {

            }

            override fun onAnimationEnd(p0: Animation?) {
                binding?.typeAnimation?.startAnimation(movingTextAnimation)
            }

            override fun onAnimationRepeat(p0: Animation?) {

            }
        })

    }




    private fun animationSetup() {
        movingTextAnimation?.duration = speed
        movingAd1Animation?.duration = speed
        movingAd2Animation?.duration = speed
        movingAd3Animation?.duration = speed

    }


    private fun dismissDialog() {
        binding?.imageView?.setOnClickListener {
            dismiss()
        }
    }


    private fun updateScreenSaver() {

        binding?.typeAnimation?.text = banner
        binding?.typeAnimation?.textSize = fontSize
        binding?.imageView?.setImageURI(imageUri)
        binding?.typeAnimation?.setTextColor(fontColor!!)
        binding?.banner?.setBackgroundColor(bannerColor!!)
        binding?.overlay?.alpha = light
        val param = binding?.banner?.layoutParams as ViewGroup.MarginLayoutParams
        param.setMargins(0, 4 * topMargin, 0, 0)

        binding?.imageAd1?.requestLayout()
        binding?.imageAd2?.requestLayout()
        binding?.imageAd3?.requestLayout()

        binding?.banner?.post {
            val overlapsHeight = (binding?.banner?.height?.times(adScale))?.toInt()

            binding?.banner?.layoutParams = param
            binding?.imageAd1?.layoutParams?.height = overlapsHeight
            binding?.imageAd2?.layoutParams?.height = overlapsHeight
            binding?.imageAd3?.layoutParams?.height = overlapsHeight


        }
        //  binding?.imageAd1?.layoutParams?.width = (binding?.imageAd1?.layoutParams?.height?.times(1.5))?.toInt()

    }

    private fun setupAd() {
        ad1Uri.apply {
            if (this != null) {
                binding?.imageAd1?.setImageURI(this)
            }
        }

        ad2Uri.apply {
            if (this != null) {
                binding?.imageAd2?.setImageURI(this)
            }
        }

        ad3Uri.apply {
            if (this != null) {
                binding?.imageAd3?.setImageURI(this)
            }
        }
    }

//    private fun setupRv() {
//        val adsList: MutableList<ArItem> = mutableListOf()
//        adsList.add(ArItem(imageUri))
//        adsList.add(ArItem(imageUri))
//        adsList.add(ArItem(imageUri))
//        adsList.add(ArItem(imageUri))
//        adsList.add(ArItem(imageUri))
//        adsList.add(ArItem(imageUri))
//        binding?.adsRv?.apply {
//            adapter = ArAdapter(adsList,)
//            setHasFixedSize(true)
//            layoutManager =
//                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
//        }
//    }
}