package com.example.bingoapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.bingoapp.databinding.ActivityMainBinding
import com.example.bingoapp.databinding.ActivityNumbersManagerBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar

class NumbersManagerActivity: AppCompatActivity() {
    private lateinit var adapter: NumbersAdapter

    private val numbers = mutableListOf<UInt>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_numbers_manager)

        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        numbers.addAll(intent.getIntegerArrayListExtra("numbers")?.map { it.toUInt()} ?: listOf())

        val recyclerView = findViewById<RecyclerView>(R.id.rv_numbers)
        adapter = NumbersAdapter(numbers) { deletedNumber, position ->
            numbers.add(position, deletedNumber)
            adapter.notifyItemInserted(position)
        }

        recyclerView.adapter = adapter

        ItemTouchHelper(object: ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val deletedNumber = numbers.removeAt(position)
                adapter.notifyItemRemoved(position)

                Snackbar.make(recyclerView, "Number $deletedNumber removed", Snackbar.LENGTH_LONG)
                    .setAction("UNDO") {
                        numbers.add(position, deletedNumber)
                        adapter.notifyItemInserted(position)
                    }
                    .show()
            }
        }).attachToRecyclerView(recyclerView)

        val binding = ActivityNumbersManagerBinding.inflate(layoutInflater)
        binding.fabClearAll.setOnClickListener {
            if (numbers.isEmpty()) return@setOnClickListener

            MaterialAlertDialogBuilder(this)
                .setTitle("Clear all numbers?")
                .setMessage("This cannot be undone.")
                .setPositiveButton("Clear") { _, _ ->
                    numbers.clear()
                    adapter.notifyDataSetChanged()
                    Toast.makeText(this, "All numbers cleared", Toast.LENGTH_SHORT).show()
                }
                .setNegativeButton("Cancel", null)
                .show()
        }
    }

    private fun finishIntent() {
        val intentResult = Intent().apply {
            putIntegerArrayListExtra("numbers", ArrayList(numbers.map { it.toInt() }))
        }
        setResult(RESULT_OK, intentResult)
        finish()
    }

    fun onClickFinish(view: View) {
        finishIntent()
    }

    override fun onSupportNavigateUp(): Boolean {
        finishIntent()
        return true
    }
}