package com.example.ambassadorpass.view.admin

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.LifecycleOwner
import com.example.ambassadorpass.R
import com.example.ambassadorpass.viewmodel.ViewPartiesViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.text.SimpleDateFormat
import java.util.*

class ViewPartiesAdapter(
    private val context: Context,
    private val parties: List<Map<String, Any>>, // Keeps expected type as Map<String, Any>
    private val viewModel: ViewPartiesViewModel
) : android.widget.BaseAdapter() {

    override fun getCount(): Int = parties.size

    override fun getItem(position: Int): Map<String, Any> = parties[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.list_item_party, parent, false)

        val party = parties[position]

        // Safely cast fields
        val partyName = party["partyName"] as? String ?: "Unknown"
        val partyId = party["partyId"] as? String ?: "Unknown"
        val partyDate = (party["partyDate"] as? Date) ?: Date() // Default to current date
        val ticketPrice = (party["ticketPrice"] as? Number)?.toDouble() ?: 0.0
        val ticketsSold = (party["ticketsSold"] as? Number)?.toInt() ?: 0
        val ambassadorMarkup = (party["ambassadorMarkup"] as? Number)?.toDouble() ?: 0.0

        // Prepare partyDetails map
        val partyDetails = mapOf(
            "partyId" to partyId,
            "partyName" to partyName,
            "partyDate" to partyDate,
            "ticketPrice" to ticketPrice,
            "ticketsSold" to ticketsSold,
            "ambassadorMarkup" to ambassadorMarkup
        )

        // Format the party date
        val dateFormatter = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault())
        val formattedDate = dateFormatter.format(partyDate)

        // Bind data to the views
        view.findViewById<TextView>(R.id.partyName).text = partyName
        view.findViewById<TextView>(R.id.partyId).text = "ID: $partyId"
        view.findViewById<TextView>(R.id.partyDate).text = "Party Date: $formattedDate"

        // Handle FloatingActionButton click
        val actionButton: FloatingActionButton = view.findViewById(R.id.partyActionButton)
        actionButton.setOnClickListener {
            viewModel.getAttendeesWithAmbassadors(partyId).observe(context as LifecycleOwner) { attendees ->
                // Inflate the dialog layout
                val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_attendees, null)
                val listView = dialogView.findViewById<ListView>(R.id.attendeesListView)

                // Ensure attendees are valid and map them correctly
                val attendeeList = attendees.mapNotNull { it as? Map<String, String> }

                // Pass attendees and partyDetails to AttendeesAdapter
                val attendeesAdapter = AttendeesAdapter(context, attendeeList, partyDetails)
                listView.adapter = attendeesAdapter

                // Add footer analysis view
                val footerView = attendeesAdapter.getFooterView()
                listView.addFooterView(footerView)

                // Show dialog
                AlertDialog.Builder(context)
                    .setTitle("Attendees & Ambassadors")
                    .setView(dialogView)
                    .setPositiveButton("OK", null)
                    .show()
            }
        }

        return view
    }
}


