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
        val partyDate: TextView = view.findViewById(R.id.partyDate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PartyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_recent_party, parent, false)
        return PartyViewHolder(view)
    }

    override fun onBindViewHolder(holder: PartyViewHolder, position: Int) {
        val party = parties[position]

        // Get party name
        holder.partyName.text = party["partyName"] as? String ?: "Unknown"

        // Convert and format party date
        val partyDateTimestamp = party["partyDate"] as? Timestamp
        val formattedDate = if (partyDateTimestamp != null) {
            val date = partyDateTimestamp.toDate()
            SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault()).format(date)
        } else {
            "No Date"
        }
        holder.partyDate.text = formattedDate
    }

    override fun getItemCount(): Int = parties.size
}
