package com.example.bingoapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.bingoapp.databinding.BingoCardBinding

class BingoCardMiniAdapter(
    private val cards: List<BingoCard>,
    private val main: MainActivity
): RecyclerView.Adapter<BingoCardMiniAdapter.ViewHolder>() {
    class ViewHolder(val binding: BingoCardBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
        = ViewHolder(BingoCardBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val card = cards[position]
        val miniCells = with(holder.binding) {
            listOf(mini0, mini1, mini2, mini3, mini4, mini5,
                mini6, mini7, mini8, mini9, mini10, mini11,
                mini12, mini13, mini14, mini15, mini16, mini17)
        }
        val cellCrosses = with(holder.binding) {
            listOf(
                cross0, cross1, cross2, cross3, cross4, cross5,
                cross6, cross7, cross8, cross9, cross10, cross11,
                cross12, cross13, cross14, cross15, cross16, cross17
            )
        }

        card.values.forEachIndexed { index, i ->
            miniCells[index].text = i?.toString() ?: "-"
            cellCrosses[index].visibility = if (main.numbers.contains(i ?: 0)) View.VISIBLE else View.GONE
        }
        holder.binding.btnDeleteCard.setOnClickListener {
            main.onDeleteCard(position)
        }
        holder.binding.textCardId.text = "Id: ${card.id}"
    }

    override fun getItemCount() = cards.size
}