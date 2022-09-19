package com.ezfirecontrols.screensaverdemo

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.marginTop
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.lifecycleScope
import com.ezfirecontrols.screensaverdemo.databinding.ActivityMainBinding
import com.ezfirecontrols.screensaverdemo.databinding.DialogArMachineBinding
import com.github.dhaval2404.colorpicker.ColorPickerDialog
import com.github.dhaval2404.colorpicker.model.ColorShape
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import pub.devrel.easypermissions.EasyPermissions


class MainActivity : AppCompatActivity() {
    private val TAG = this::class.java.simpleName
    private var binding: ActivityMainBinding? = null
    private var wallpaperImage: Uri? = null
    private var adUri1uploaded: Uri? = null
    private var adUri2uploaded: Uri? = null
    private var adUri3uploaded: Uri? = null

    private val WALL_PAPER_IMAGE_CODE = 100
    private val ADD_ONE_CODE = 1
    private val ADD_TWO_CODE = 2
    private val ADD_THREE_CODE = 3
    val mDefaultFontColor = R.color.black
    var bannerColor: Int? = null
    private var movingTextAnimation: Animation? = null
    private var movingAd1Animation: Animation? = null
    private var movingAd2Animation: Animation? = null
    private var movingAd3Animation: Animation? = null
    private var intervals = 5000L
    private var imageScale = 3

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        askPermission()
        showScreenSaver()
        updatePreview()
        movingTextAnimation = AnimationUtils.loadAnimation(this, R.anim.move)
        movingAd1Animation = AnimationUtils.loadAnimation(this, R.anim.move_ad_1)
        movingAd2Animation = AnimationUtils.loadAnimation(this, R.anim.move_ad_2)
        movingAd3Animation = AnimationUtils.loadAnimation(this, R.anim.move_ad_3)




        chooseImage()
        updateFontLive()
        updateSpeedLive()
        updateText()
        chooseFontColor()
        chooseBannerColor()
        lightLiveUpdate()
        topMarginLiveUpdate()
        intervalsLiveUpdate()

        //start animation
        setupAdOneAnimation()
        setupAdTwoAnimation()
        setupAdThreeAnimation()

        //adding images
        addImage1()
        addImage2()
        addImage3()
        //show ar
        showArVending()
    }

    private fun screenSaveSettings() {
        movingTextAnimation?.duration = binding?.speedSeekBar?.progress!!.toLong() * 2000
        movingAd1Animation?.duration = binding?.speedSeekBar?.progress!!.toLong() * 2000
        movingAd2Animation?.duration = binding?.speedSeekBar?.progress!!.toLong() * 2000
        movingAd3Animation?.duration = binding?.speedSeekBar?.progress!!.toLong() * 2000

        binding?.typeAnimation?.text = binding?.bannerTextEdit?.text.toString().trim()
        binding?.typeAnimation?.textSize = binding?.fontSeekBar?.progress!!.toFloat() * 5
        binding?.overlayCover?.alpha = 1 - (binding?.lightSeekBar?.progress!!.toFloat() / 10)
        val param = binding?.banner?.layoutParams as ViewGroup.MarginLayoutParams
        param.setMargins(0, (10 * binding?.marginSeekBar!!.progress), 0, 0)
        imageScale = binding?.scaleSeekBar!!.progress
        binding?.banner?.layoutParams = param
        binding?.banner?.post {
            val overlapsHeight = (binding?.banner?.height?.times(imageScale))?.toInt()

            binding?.banner?.layoutParams = param
            binding?.adPreview1?.layoutParams?.height = overlapsHeight
            binding?.adPreview2?.layoutParams?.height = overlapsHeight
            binding?.adPreview3?.layoutParams?.height = overlapsHeight
            intervals = (binding?.intervalsSeekBar?.progress?.times(1000))!!.toLong()


        }

        setUpTextAnimation()


    }

    private fun updatePreview() {
        binding?.button2?.setOnClickListener {
            screenSaveSettings()
        }
    }


    private fun chooseImage() {
        binding?.button?.setOnClickListener {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(intent, WALL_PAPER_IMAGE_CODE)
        }
    }

    private fun addImage1() {
        binding?.addAd1?.setOnClickListener {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(intent, ADD_ONE_CODE)
        }
    }

    private fun addImage2() {
        binding?.addAd2?.setOnClickListener {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(intent, ADD_TWO_CODE)
        }
    }

    private fun addImage3() {
        binding?.addAd3?.setOnClickListener {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(intent, ADD_THREE_CODE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            when (requestCode) {
                WALL_PAPER_IMAGE_CODE -> {
                    wallpaperImage = data?.data
                    binding?.imageView?.setImageURI(wallpaperImage)
                }

                ADD_ONE_CODE -> {
                    adUri1uploaded = data?.data
                    binding?.adPreview1?.setImageURI(adUri1uploaded)
                    binding?.ad1Small?.setImageURI(adUri1uploaded)

                }

                ADD_TWO_CODE -> {
                    adUri2uploaded = data?.data
                    binding?.adPreview2?.setImageURI(adUri2uploaded)
                    binding?.ad2Small?.setImageURI(adUri2uploaded)

                }

                ADD_THREE_CODE -> {
                    adUri3uploaded = data?.data
                    binding?.adPreview3?.setImageURI(adUri3uploaded)
                    binding?.add3Small?.setImageURI(adUri3uploaded)
                }

            }
        }
        if (requestCode == WALL_PAPER_IMAGE_CODE && resultCode == RESULT_OK) {

            Log.d(TAG, "$data   ")
        }
    }

    private fun askPermission() {
        if (!EasyPermissions.hasPermissions(
                this,
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            )
        ) {
            EasyPermissions.requestPermissions(
                this,
                "please allow reading from storage",
                4,
                android.Manifest.permission.READ_EXTERNAL_STORAGE,
            )
        }
    }

    private fun showScreenSaver() {
        binding?.screenSaverButton?.setOnClickListener {
            screenSaveSettings()
            Log.d(TAG, binding?.typeAnimation!!.textSize.toString())
            ScreenSaveDialog.getInstance(
                banner = binding?.bannerTextEdit?.text.toString(),
                imageUri = wallpaperImage,
                fontSize = binding?.typeAnimation!!.textSize,
                speed = movingTextAnimation!!.duration,
                fontColor = binding?.typeAnimation?.currentTextColor!!,
                bannerColor = bannerColor ?: resources.getColor(R.color.red),
                light = binding?.overlayCover?.alpha!!,
                topMargin = binding?.banner!!.marginTop,
                ad1Uri = adUri1uploaded,
                ad2Uri = adUri2uploaded,
                ad3Uri = adUri3uploaded,
                intervals,
                adScale = imageScale
            )
                .show(this.supportFragmentManager, ScreenSaveDialog::class.java.simpleName)
        }
    }

    private fun updateSpeedLive() {
        binding?.speedSeekBar?.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                screenSaveSettings()

            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
            }
        })
    }

    private fun updateFontLive() {
        binding?.fontSeekBar?.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                screenSaveSettings()

            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
            }
        })
    }

    private fun updateText() {
        binding?.bannerTextEdit?.doAfterTextChanged {
            screenSaveSettings()
        }
    }

    private fun chooseFontColor() {
        binding?.fontColor?.setOnClickListener {
            ColorPickerDialog
                .Builder(this)                        // Pass Activity Instance
                .setTitle("Pick Theme")            // Default "Choose Color"
                .setColorShape(ColorShape.SQAURE)   // Default ColorShape.CIRCLE
                .setDefaultColor(mDefaultFontColor)     // Pass Default Color
                .setColorListener { color, colorHex ->
                    binding?.typeAnimation?.setTextColor(color)
                }
                .show()
        }
    }

    private fun chooseBannerColor() {
        binding?.bannerColor?.setOnClickListener {
            ColorPickerDialog
                .Builder(this)                        // Pass Activity Instance
                .setTitle("Pick Theme")            // Default "Choose Color"
                .setColorShape(ColorShape.SQAURE)   // Default ColorShape.CIRCLE
                .setDefaultColor(mDefaultFontColor)     // Pass Default Color
                .setColorListener { color, colorHex ->
                    binding?.banner?.setBackgroundColor(color)
                    bannerColor = color
                }
                .show()
        }
    }

    private fun lightLiveUpdate() {
        binding?.lightSeekBar?.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                screenSaveSettings()

            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
            }
        })
    }

    private fun topMarginLiveUpdate() {
        binding?.marginSeekBar?.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                screenSaveSettings()

            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
            }
        })
    }

    private fun intervalsLiveUpdate() {
        binding?.intervalsSeekBar?.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                screenSaveSettings()

            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
            }
        })
    }


    private fun scaleLiveUpdate() {
        binding?.scaleSeekBar?.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                screenSaveSettings()

            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
            }
        })

    }

    private fun setUpTextAnimation() {

        binding?.typeAnimation?.startAnimation(movingTextAnimation)
        movingTextAnimation?.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(p0: Animation?) {


            }

            override fun onAnimationEnd(p0: Animation?) {
                binding?.adPreview1?.startAnimation(movingAd1Animation)


            }

            override fun onAnimationRepeat(p0: Animation?) {
            }
        })
    }

    private fun setupAdOneAnimation() {
        movingAd1Animation?.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(p0: Animation?) {
                lifecycleScope.launch {
                    delay(intervals)
                    binding?.adPreview2?.startAnimation(movingAd2Animation)

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
                    delay(intervals)
                    binding?.adPreview3?.startAnimation(movingAd3Animation)


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

    private fun showArVending() {
        binding?.arVending?.setOnClickListener {
            ArMachineDialog.getInstance(
                adUri1uploaded!!,
                adUri2uploaded!!,
                adUri3uploaded!!,
                binding?.editTextNumber?.text.toString().toInt(),
                binding?.totalItems?.text?.toString()!!.toInt(),
                banner = binding?.bannerTextEdit?.text.toString(),
                imageUri = wallpaperImage,
                fontSize = binding?.typeAnimation!!.textSize,
                speed = movingTextAnimation!!.duration,
                fontColor = binding?.typeAnimation?.currentTextColor!!,
                bannerColor = bannerColor ?: resources.getColor(R.color.red),
                light = binding?.overlayCover?.alpha!!,
                topMargin = binding?.banner!!.marginTop,
                intervals,
                adScale = imageScale
            )
                .show(this.supportFragmentManager, ArMachineDialog::class.java.simpleName)
        }
    }


}