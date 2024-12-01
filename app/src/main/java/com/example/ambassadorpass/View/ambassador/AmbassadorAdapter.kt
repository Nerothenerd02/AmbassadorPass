package com.example.ambassadorpass.view.ambassador

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.ambassadorpass.Model.Party
import com.example.ambassadorpass.R
import com.example.ambassadorpass.viewmodel.AmbassadorViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth

class PartyAdapter(private val context: Context, private val parties: List<Party>) : android.widget.BaseAdapter() {

    private val expandedStates = BooleanArray(parties.size) { false }

    override fun getCount(): Int = parties.size

    override fun getItem(position: Int): Party = parties[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.list_item_ambassador, parent, false)

        val cardView: CardView = view.findViewById(R.id.partyCard)
        val extraInfoContainer: LinearLayout = view.findViewById(R.id.extraInfoContainer)
        val expandButton: FloatingActionButton = view.findViewById(R.id.partyActionButton)

        val party = parties[position]
        view.findViewById<TextView>(R.id.partyName).text = party.partyName
        view.findViewById<TextView>(R.id.partyId).text = party.partyId
        view.findViewById<TextView>(R.id.partyDate).text = party.partyDate?.toDate().toString()

        expandButton.setOnClickListener {
            val ambassadorId = FirebaseAuth.getInstance().currentUser?.uid
            if (ambassadorId != null) {
                val viewModel = ViewModelProvider(context as AppCompatActivity).get(AmbassadorViewModel::class.java)

                viewModel.calculateTotalCommission(ambassadorId, party.partyId)
                    .observe(context as AppCompatActivity, Observer { (attendees, totalCommission) ->
                        val attendeeNames = attendees.joinToString("\n")
                        AlertDialog.Builder(context)
                            .setTitle("Attendees (Paid)")
                            .setMessage(
                                "Attendees:\n$attendeeNames\n\n" +
                                        "Total Commission: $totalCommission"
                            )
                            .setPositiveButton("OK", null)
                            .show()
                    })
            } else {
                Toast.makeText(context, "Unable to fetch attendees. User not logged in.", Toast.LENGTH_SHORT).show()
            }
        }


        return view
    }
}
