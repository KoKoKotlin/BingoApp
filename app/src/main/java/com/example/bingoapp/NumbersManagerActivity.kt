package com.example.bingoapp

import android.content.Intent
import android.os.Bundle
import android.text.InputFilter
import android.text.InputType
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.bingoapp.databinding.ActivityNumbersManagerBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class NumbersManagerActivity: AppCompatActivity() {
    val numbers = mutableListOf<Int>()
    lateinit var gridAdapter: BingoNumberAdapter
    private lateinit var binding: ActivityNumbersManagerBinding

    fun showToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    private fun showEditDialog(position: Int) {
        val textInputLayout = TextInputLayout(this).apply {
            hint = "Number (1–99)"
            boxBackgroundMode = TextInputLayout.BOX_BACKGROUND_OUTLINE
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }

        val editText = TextInputEditText(this).apply {
            inputType = InputType.TYPE_CLASS_NUMBER
            filters = arrayOf(InputFilter.LengthFilter(2))
        }
        textInputLayout.addView(editText)

        val container = FrameLayout(this).apply {
            addView(textInputLayout, FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                setMargins(50, 100, 50, 100)
            })
        }

        MaterialAlertDialogBuilder(this, com.google.android.material.R.style.ThemeOverlay_Material3_MaterialAlertDialog_Centered)
            .setView(container)
            .setPositiveButton("Change") { _, _ ->
                val number = editText.text.toString().toIntOrNull()
                    ?: return@setPositiveButton showToast("Invalid number!")

                if (number !in 1..99)
                    return@setPositiveButton showToast("Invalid number!")

                if (numbers.contains(number))
                    return@setPositiveButton showToast("Duplicate number!")

                numbers[position] = number
                gridAdapter.notifyDataSetChanged()
            }
            .setNegativeButton("Cancel", null)
            .show()

        editText.requestFocus()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNumbersManagerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val width = intent.getIntExtra("width", 6)
        val height = intent.getIntExtra("height", 16)
        numbers.addAll(intent.getIntegerArrayListExtra("numbers") ?: listOf())

        gridAdapter = BingoNumberAdapter(numbers, this, width, height)
        binding.numbersGrid.numColumns = width
        binding.numbersGrid.adapter = gridAdapter

        binding.numbersGrid.setOnItemClickListener { _, _, position, _ ->
            showEditDialog(position)
        }

        binding.btnFinish.setOnClickListener {
            finishIntent(false)
        }

        binding.btnCancel.setOnClickListener {
            finishIntent(true)
        }
    }

    private fun finishIntent(cancel: Boolean) {
        val intentResult = Intent().apply {
            if (!cancel) {
                putIntegerArrayListExtra("numbers", ArrayList(numbers))
            }
        }
        setResult(if (!cancel) RESULT_OK else RESULT_CANCELED, intentResult)
        finish()
    }

    override fun onNavigateUp(): Boolean {
        finishIntent(false)
        return super.onNavigateUp()
    }
}