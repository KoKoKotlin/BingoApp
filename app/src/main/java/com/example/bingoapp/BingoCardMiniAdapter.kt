package com.example.bingoapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.bingoapp.databinding.BingoCardBinding

class BingoCardMiniAdapter(
    private val cards: List<BingoCard>
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

        card.values.forEachIndexed { index, i -> miniCells[index].text = i?.toString() ?: "-" }
    }

    override fun getItemCount() = cards.size
}