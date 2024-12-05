package com.example.ambassadorpass.view.admin

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.ambassadorpass.R

class AttendeesAdapter(
    private val context: Context,
    private val attendees: List<Map<String, String>>,
    private val partyDetails: Map<String, Any>
) : android.widget.BaseAdapter() {

    override fun getCount(): Int = attendees.size

    override fun getItem(position: Int): Map<String, String> = attendees[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.list_item_attendee, parent, false)

        val attendee = attendees[position]
        val attendeeName = attendee["attendeeName"] ?: "Unknown"
        val ambassadorName = attendee["ambassadorName"] ?: "Unknown"

        view.findViewById<TextView>(R.id.attendeeName).text = attendeeName
        view.findViewById<TextView>(R.id.ambassadorName).text = ambassadorName

        return view
    }

    // Add a footer view for analysis
    fun getFooterView(): View {
        val footerView = LayoutInflater.from(context).inflate(R.layout.footer_analysis, null)

        // Extract party details
        val ticketPrice = partyDetails["ticketPrice"] as? Double ?: 0.0
        val ticketsSold = partyDetails["ticketsSold"] as? Int ?: 0
        val ambassadorMarkup = partyDetails["ambassadorMarkup"] as? Double ?: 0.0

        // Calculate total earnings
        val totalEarnings = ticketPrice * ticketsSold

        // Calculate attendees per ambassador
        val attendeesPerAmbassador = attendees.groupBy { it["ambassadorName"] ?: "Unknown" }
            .mapValues { (_, attendeeList) -> attendeeList.size }

        // Calculate commission per ambassador
        val commissionsPerAmbassador = attendeesPerAmbassador.mapValues { (ambassador, count) ->
            val commissionPerTicket = (ambassadorMarkup / 100) * ticketPrice
            commissionPerTicket * count
        }

        // Populate footer
        footerView.findViewById<TextView>(R.id.totalEarnings).text = "Total Earnings: $totalEarnings"
        footerView.findViewById<TextView>(R.id.totalAttendees).text = "Total Attendees: ${attendees.size}"

        val detailsText = StringBuilder()
        attendeesPerAmbassador.forEach { (ambassadorName, count) ->
            val totalCommission = commissionsPerAmbassador[ambassadorName] ?: 0.0
            detailsText.append("Ambassador: $ambassadorName\n")
            detailsText.append("Invited: $count\n")
            detailsText.append("Commission: $totalCommission\n\n")
        }

        footerView.findViewById<TextView>(R.id.ambassadorDetails).text = detailsText.toString()

        return footerView
    }
}
