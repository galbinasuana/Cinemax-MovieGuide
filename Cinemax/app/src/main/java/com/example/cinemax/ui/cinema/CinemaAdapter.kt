package com.example.cinemax.ui.cinema

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.cinemax.R

class CinemaAdapter(private val context: Context, private val dataSource: List<String>) : BaseAdapter() {

    private val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getCount(): Int {
        return dataSource.size
    }

    override fun getItem(position: Int): Any {
        return dataSource[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val rowView = convertView ?: inflater.inflate(R.layout.cinema_list_item, parent, false)

        val pinIcon = rowView.findViewById<ImageView>(R.id.pinIcon)
        val cinemaName = rowView.findViewById<TextView>(R.id.cinemaName)
        val arrowIcon = rowView.findViewById<ImageView>(R.id.arrowIcon)

        val cinemaDetails = getItem(position) as String
        val details = cinemaDetails.split(", ")

        cinemaName.text = details[0]

        pinIcon.setImageResource(R.drawable.marker)
        arrowIcon.setImageResource(R.drawable.arrow)

        return rowView
    }
}