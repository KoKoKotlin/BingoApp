package com.example.bingoapp

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.InputFilter
import android.text.InputType
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.GridLayoutManager
import com.example.bingoapp.databinding.ActivityMainBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val bingoCards: MutableList<BingoCard> = mutableListOf()
    private lateinit var adapter: BingoCardMiniAdapter
    private val _numbers = mutableListOf<UInt>()
    val numbers: MutableList<UInt>
        get() = _numbers

    private val gson = Gson()
    private val prefs by lazy { getSharedPreferences("BingoApp", MODE_PRIVATE) }

    fun resortCards() {
        bingoCards.sortByDescending { c -> c.markedCount(numbers) }
        adapter.notifyDataSetChanged()
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    val startForResultCard = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == RESULT_OK) {
            Toast.makeText(this, "New Bingo card created!", Toast.LENGTH_SHORT).show()
            it.data?.getParcelableExtra("NEW_BINGO_CARD", BingoCard::class.java)
                ?.let(bingoCards::add)
            resortCards()
        } else {
            Toast.makeText(this, "Canceled creating a new card!", Toast.LENGTH_SHORT).show()
        }
    }

    val startForResultNumbers = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == RESULT_OK) {
            it.data?.getIntegerArrayListExtra("numbers")
                ?.let { ns -> _numbers.clear(); _numbers.addAll(ns.map{ k -> k.toUInt() }) }
            resortCards()
        }
    }

    private fun showAddNumberDialog() {
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
            .setPositiveButton("Add") { _, _ ->
                val number = editText.text.toString().toIntOrNull()
                if (number != null && number in 1..99) {
                    _numbers.add(number.toUInt())
                    resortCards()
                } else {
                    Toast.makeText(this, "Invalid number", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel", null)
            .show()

        editText.requestFocus()
    }
    fun onDeleteCard(index: Int) {
        MaterialAlertDialogBuilder(this)
            .setTitle("Delete card?")
            .setMessage("Are you sure you want to delete bingo card with id ${bingoCards[index].id}?")
            .setPositiveButton("Delete") { _, _ ->
                bingoCards.removeAt(index)
                adapter.notifyItemRemoved(index)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    fun onClickEditNumbers(view: View) {
        startForResultNumbers.launch(Intent(this, NumbersManagerActivity::class.java).apply {
            putIntegerArrayListExtra("numbers", ArrayList(_numbers.map { it.toInt() }))
        })
    }

    override fun onPause() {
        super.onPause()

        with(prefs.edit()) {
            putString("cards", gson.toJson(bingoCards))
            putString("numbers", gson.toJson(numbers))
            apply()
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val cardsJson = prefs.getString("cards", null)
        if (cardsJson != null) {
            val type = object: TypeToken<List<BingoCard>>() {}.type;
            bingoCards.addAll(gson.fromJson(cardsJson, type))
        }

        val numbersJson = prefs.getString("numbers", null)
        if (numbersJson != null) {
            val type = object: TypeToken<List<UInt>>() {}.type;
            numbers.addAll(gson.fromJson<List<UInt>>(numbersJson, type))
        }

        adapter = BingoCardMiniAdapter(bingoCards, this)
        binding.rvBingoCards.layoutManager = GridLayoutManager(this, 1)
        binding.rvBingoCards.adapter = adapter
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    fun onClickAddCard(view: View) {
        startForResultCard.launch(Intent(this, CreateCardActivity::class.java))
    }

    fun onClickAddNumber(view: View) {
        showAddNumberDialog()
    }
}