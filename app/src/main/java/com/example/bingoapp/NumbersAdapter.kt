package com.example.bingoapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.bingoapp.databinding.ItemNumberBinding

class NumbersAdapter(
    private val numbers: MutableList<UInt>,
    private val onUndo: (UInt, Int) -> Unit
): RecyclerView.Adapter<NumbersAdapter.VH>() {

    class VH(val binding: ItemNumberBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        VH(ItemNumberBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.binding.tvNumber.text = numbers[position].toString()
    }

    override fun getItemCount() = numbers.size
}