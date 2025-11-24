package com.example.bingoapp

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class BingoCard(val id: UInt, val values: Array<UInt?>): Parcelable {
    init {
        if (values.size != 6 * 3) throw IllegalArgumentException("Bingo card needs exactly 18 values!")
    }

    fun markedCount(numbers: List<UInt>) = values.filter { numbers.contains(it ?: 0) }.size
}