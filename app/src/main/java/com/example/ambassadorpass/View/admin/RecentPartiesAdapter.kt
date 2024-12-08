package com.example.ambassadorpass.view.admin

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ambassadorpass.R
import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.*

class RecentPartiesAdapter(private val parties: List<Map<String, Any>>) :
    RecyclerView.Adapter<RecentPartiesAdapter.PartyViewHolder>() {

    class PartyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val partyName: TextView = view.findViewById(R.id.partyName)
        val partyId: TextView = view.findViewById(R.id.partyId)
        val partyDate: TextView = view.findViewById(R.id.partyDate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PartyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_recent_party, parent, false)
        return PartyViewHolder(view)
    }

    override fun onBindViewHolder(holder: PartyViewHolder, position: Int) {
        val party = parties[position]

        // Bind Party Name
        holder.partyName.text = party["partyName"] as? String ?: "Unknown"

        // Bind Party ID
        holder.partyId.text = "ID: ${party["partyId"] as? String ?: "Unknown ID"}"

        // Format and Bind Party Date
        val partyDate = (party["partyDate"] as? Date) ?: Date() // Default to current date if null
        val dateFormatter = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault())
        holder.partyDate.text = "Party Date: ${dateFormatter.format(partyDate)}"
    }

    override fun getItemCount(): Int = parties.size
}