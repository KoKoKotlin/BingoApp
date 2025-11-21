package com.example.bingoapp

import android.content.Intent
import android.os.Build
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import com.example.bingoapp.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val bingoCards: MutableList<BingoCard> = mutableListOf()

    companion object {
        private const val REQUEST_BINGO_CARD_ID = 1000;
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    val startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == REQUEST_BINGO_CARD_ID) {
            it.data?.getParcelableExtra("NEW_BINGO_CARD", BingoCard::class.java)
                ?.let(bingoCards::add)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    fun onClickAddCard(view: View) {
        val intent = Intent(this, CreateCardActivity::class.java)
        startActivityForResult(intent, 1001)

    }
}