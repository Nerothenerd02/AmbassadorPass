package com.example.ambassadorpass.adapter

import android.app.AlertDialog
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.ambassadorpass.Model.Party
import com.example.ambassadorpass.R
import com.example.ambassadorpass.viewmodel.AssignAmbassadorViewModel
import java.text.SimpleDateFormat
import java.util.Locale

class PartyAdapter(
    private val context: Context,
    private var partyList: MutableList<Party>,
    private val viewModel: AssignAmbassadorViewModel
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

            // Handle the assign button
            val assignButton = view.findViewById<View>(R.id.assignAmbassadorButton)
            assignButton.setOnClickListener {
                showAssignAmbassadorDialog(context, party.partyId)
            }

        } catch (e: Exception) {
            Log.e("AdapterError", "Failed to bind data for party at position $position", e)
        }

        return view
    }

    private fun showAssignAmbassadorDialog(context: Context, partyId: String) {
        // Inflate the dialog view
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_assign_ambassador, null)

        val dialog = AlertDialog.Builder(context)
            .setTitle("Assign Ambassador")
            .setView(dialogView)
            .setCancelable(true)
            .create()

        // References to the fields
        val ambassadorNameField = dialogView.findViewById<EditText>(R.id.ambassadorName)
        val ambassadorEmailField = dialogView.findViewById<EditText>(R.id.ambassadorEmail)
        val sendButton = dialogView.findViewById<Button>(R.id.sendButton)

        sendButton.setOnClickListener {
            val name = ambassadorNameField.text.toString().trim()
            val email = ambassadorEmailField.text.toString().trim()

            if (name.isNotEmpty() && email.isNotEmpty()) {
                // Use ViewModel to create ambassador
                viewModel.handleAmbassadorSubmission(email, name, partyId) { isSuccess, errorMessage ->
                    if (isSuccess) {
                        Toast.makeText(context, "Ambassador assigned successfully", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "Failed to assign ambassador: $errorMessage", Toast.LENGTH_SHORT).show()
                    }
                }


                // Dismiss the dialog after sending
                dialog.dismiss()
            } else {
                // Show error if any of the fields are empty
                Toast.makeText(context, "Please fill out all fields", Toast.LENGTH_SHORT).show()
            }
        }

        dialog.show()
    }

    fun updateData(newData: List<Party>) {
        partyList.clear()
        partyList.addAll(newData)
        Log.d("AdapterUpdate", "Updated adapter with ${partyList.size} parties")
        notifyDataSetChanged()
    }
}
