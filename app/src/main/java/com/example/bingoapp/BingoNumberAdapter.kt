package com.example.bingoapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView


class BingoNumberAdapter(val data: MutableList<Int>, val context: Context, val width: Int, val height: Int): BaseAdapter() {

    override fun getCount() = width * height
    override fun getItem(position: Int) = if (position < data.count() && data[position] != -1) { data[position] } else { null }
    override fun getItemId(position: Int) = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
        var convertView = convertView
        if (convertView == null) {
            convertView = LayoutInflater
                .from(context)
                .inflate(R.layout.numbers_grid_item, parent, false)
        }

        val textView = convertView.findViewById<TextView>(R.id.number_grid_item_text)
        textView.width = (parent!!.width.toFloat() / width.toFloat()).toInt() - 2
        textView.height = (parent!!.height.toFloat() / height.toFloat()).toInt() - 2
        textView.text = getItem(position)?.toString() ?: ""

        return convertView
    }
}