package com.example.bingoapp

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.GridLayoutManager
import com.example.bingoapp.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val bingoCards: MutableList<BingoCard> = mutableListOf()
    private lateinit var adapter: BingoCardMiniAdapter

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    val startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        Toast.makeText(this, "New Bingo card created!", Toast.LENGTH_SHORT).show()
        if (it.resultCode == RESULT_OK) {
            it.data?.getParcelableExtra("NEW_BINGO_CARD", BingoCard::class.java)
                ?.let(bingoCards::add)
            adapter.notifyItemInserted(bingoCards.lastIndex)
            binding.rvBingoCards.smoothScrollToPosition(bingoCards.lastIndex)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = BingoCardMiniAdapter(bingoCards)
        binding.rvBingoCards.layoutManager = GridLayoutManager(this, 1)
        binding.rvBingoCards.adapter = adapter
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    fun onClickAddCard(view: View) {
        startForResult.launch(Intent(this, CreateCardActivity::class.java))
    }
}