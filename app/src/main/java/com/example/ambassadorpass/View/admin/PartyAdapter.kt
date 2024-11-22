package com.example.ambassadorpass.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.ambassadorpass.Model.Party
import com.example.ambassadorpass.R
import java.text.SimpleDateFormat
import java.util.Locale

class PartyAdapter(
    private val context: Context,
    private var partyList: MutableList<Party>
) : android.widget.BaseAdapter() {

    override fun getCount(): Int = partyList.size

    override fun getItem(position: Int): Any = partyList[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.list_item, parent, false)
        val party = partyList[position]

        try {
            val partyNameTextView = view.findViewById<TextView>(R.id.listName)
            val partyIdTextView = view.findViewById<TextView>(R.id.listId)
            val partyDateTextView = view.findViewById<TextView>(R.id.partyDate)

            val formattedDate = party.partyDate?.toDate()?.let {
                SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(it)
            } ?: "No Date"

            partyNameTextView.text = party.partyName
            partyIdTextView.text = party.partyId
            partyDateTextView.text = formattedDate
        } catch (e: Exception) {
            Log.e("AdapterError", "Failed to bind data for party at position $position", e)
        }

        return view
    }

    fun updateData(newData: List<Party>) {
        partyList.clear()
        partyList.addAll(newData)
        Log.d("AdapterUpdate", "Updated adapter with ${partyList.size} parties")
        notifyDataSetChanged()
    }
}
