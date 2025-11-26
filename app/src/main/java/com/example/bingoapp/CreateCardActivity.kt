package com.example.bingoapp

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.bingoapp.databinding.ActivityCreateCardBinding

class CreateCardActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCreateCardBinding
    private var currentCellIdx: Int = 0
    private var firstDigit: Int? = null
    private var secondDigit: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateCardBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)


        binding.bingoCard.mini0.background = ContextCompat
            .getDrawable(this, R.drawable.mini_cell_border_highlight)
        binding.bingoCard.btnDeleteCard.visibility = View.INVISIBLE

        with(binding) {
            listOf(btn0, btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9, btnNull)
        }.forEachIndexed { idx, btn ->
            if (idx != 10) {
                btn.setOnClickListener {
                    if (firstDigit == null) {
                        firstDigit = idx
                    } else {
                        secondDigit = idx
                        finalizeCell()
                    }
                }
            } else {
                btn.setOnClickListener {
                    firstDigit = null
                    secondDigit = null
                    finalizeCell()
                }
            }
        }

        val miniCells = with(binding.bingoCard) {
            listOf(
                mini0, mini1, mini2, mini3, mini4, mini5,
                mini6, mini7, mini8, mini9, mini10, mini11,
                mini12, mini13, mini14, mini15, mini16, mini17
            )
        }

        miniCells.forEachIndexed { idx, cell ->
            cell.setOnClickListener {
                miniCells[currentCellIdx].background = null
                miniCells[idx].background = ContextCompat
                    .getDrawable(this, R.drawable.mini_cell_border_highlight)
                currentCellIdx = idx
            }
        }
    }

    private fun finalizeCell() {
        if (currentCellIdx >= 18) return;

        val miniCells = with(binding.bingoCard) {
            listOf(mini0, mini1, mini2, mini3, mini4, mini5,
                mini6, mini7, mini8, mini9, mini10, mini11,
                mini12, mini13, mini14, mini15, mini16, mini17)
        }

        if (firstDigit == null  || secondDigit == null) {
            miniCells[currentCellIdx].text = "-"
        } else {
            miniCells[currentCellIdx].text = (firstDigit!! * 10 + secondDigit!!).toString()
            firstDigit = null
            secondDigit = null
        }
        currentCellIdx++
        miniCells[currentCellIdx - 1].background = null
        if (currentCellIdx < 18) {
            miniCells[currentCellIdx].background = ContextCompat
                .getDrawable(this, R.drawable.mini_cell_border_highlight)
        }
    }

    fun onCancel(view: View) {
        setResult(RESULT_CANCELED)
        finish()
    }

    fun onCreateCard(view: View) {
        val values = with(binding.bingoCard) {
            listOf(mini0, mini1, mini2, mini3, mini4, mini5,
                mini6, mini7, mini8, mini9, mini10, mini11,
                mini12, mini13, mini14, mini15, mini16, mini17)
        }.map { if (it.text == "-" || it.text == "") null else it.text.toString().toUInt() }
            .toTypedArray()


        val id = binding.bingoCardId.text.toString().toUIntOrNull()
        if (id == null) {
            Toast.makeText(this, "Card id missing!", Toast.LENGTH_LONG).show()
            return
        }

        val bingoCard = BingoCard(id, values)
        val intentResult = Intent().apply {
            putExtra("NEW_BINGO_CARD", bingoCard)
        }
        setResult(RESULT_OK, intentResult)
        finish()
    }
}