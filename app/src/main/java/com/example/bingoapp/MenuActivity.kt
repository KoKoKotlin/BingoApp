package com.example.bingoapp

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.ColorUtils
import com.example.bingoapp.databinding.ActivityMenuBinding

class MenuActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMenuBinding

    private fun updateColor() {
        val hue = binding.sliderHue.value
        val sat = binding.sliderSat.value / 100
        val lig = binding.sliderLig.value / 100

        val pureHue = Color.HSVToColor(floatArrayOf(hue, 1f, 1f))
        val color = Color.HSVToColor(floatArrayOf(hue, sat, lig))

        binding.sliderHue.trackTintList = ColorStateList.valueOf(pureHue)
        binding.sliderHue.thumbTintList = ColorStateList.valueOf(pureHue)

        binding.cardviewMenu.setCardBackgroundColor(color)
        val lighterColor = ColorUtils.blendARGB(color, Color.WHITE, 0.50f)
        binding.miniMenu.setBackgroundColor(lighterColor)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val width = intent.getIntExtra("width", 8)
        val height = intent.getIntExtra("height", 16)
        val hue = intent.getIntExtra("hue", 180)
        val sat = intent.getIntExtra("sat", 33)
        val lig = intent.getIntExtra("lig", 55)
        val sortingVariant = intent.getStringExtra("sortingVariant")?.let { SortingVariant.from(it) }
            ?: SortingVariant.MostCrossed

        binding.etWidth.setText(width.toString())
        binding.etHeight.setText(height.toString())

        binding.sliderSat.value = sat.toFloat()
        binding.sliderLig.value = lig.toFloat()

        binding.sliderHue.addOnChangeListener { _, _, _ -> updateColor() }
        binding.sliderSat.addOnChangeListener { _, _, _ -> updateColor() }
        binding.sliderLig.addOnChangeListener { _, _, _ -> updateColor() }

        // Update colors (the value needs to change once)
        binding.sliderHue.value = if (hue == 360) 0f else 360f
        binding.sliderHue.performClick()
        binding.sliderHue.value = hue.toFloat()
        binding.sliderHue.performClick()

        val sortOptions = SortingVariant.entries.toTypedArray()
        val arrayAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            sortOptions)
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.actvSort.setAdapter(arrayAdapter)
        binding.actvSort.setText(sortingVariant.displayName, false)

        binding.btnCancel.setOnClickListener {
            finishIntent(true)
        }

        binding.btnSave.setOnClickListener {
            finishIntent(false)
        }
    }

    private fun finishIntent(cancel: Boolean) {
        val width = binding.etWidth.text.toString().toInt()
        val height = binding.etHeight.text.toString().toInt()
        val hue = binding.sliderHue.value.toInt()
        val sat = binding.sliderSat.value.toInt()
        val lig = binding.sliderLig.value.toInt()
        val sortingVariant = SortingVariant.from(binding.actvSort.text.toString()).value
        val intentResult = Intent().apply {
            if (!cancel) {
                putExtra("width", width)
                putExtra("height", height)
                putExtra("hue", hue)
                putExtra("sat", sat)
                putExtra("lig", lig)
                putExtra("sortingVariant", sortingVariant)
            }
        }
        setResult(if (!cancel) RESULT_OK else RESULT_CANCELED, intentResult)
        finish()
    }

    override fun onNavigateUp(): Boolean {
        finishIntent(true)
        return super.onNavigateUp()
    }
}