package com.example.bingoapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.bingoapp.databinding.ActivityCreateCardBinding

class CreateCardActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCreateCardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateCardBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
    }

    fun onCreateCard(view: View) {
        val editTexts = listOf(
            binding.edit0, binding.edit1, binding.edit2, binding.edit3, binding.edit4, binding.edit5,
            binding.edit6, binding.edit7, binding.edit8, binding.edit9, binding.edit10, binding.edit11,
            binding.edit12, binding.edit13, binding.edit14, binding.edit15, binding.edit16, binding.edit17
        )

        val values = editTexts.map {
            if (it.text.isEmpty()) null else it.text.toString().toUIntOrNull()
        }.toTypedArray()
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
        Toast.makeText(this, "New Bingo card created!", Toast.LENGTH_SHORT).show()
        finish()
    }
}