package com.example.ambassadorpass.service

import android.content.Context
import android.content.Intent
import android.util.Log

class EmailService(private val context: Context) {

    fun sendAmbassadorEmail(recipientEmail: String, ambassadorName: String, partyName: String, generatedPassword: String, generatedPartyLink: String) {
        try {
            val subject = "Your Ambassador Credentials for \$partyName"
            val messageBody = """
                Hello \$ambassadorName,
                
                You have been assigned as an ambassador for the party "\$partyName".
                
                Here are your credentials:
                Email: \$recipientEmail
                Password: \$generatedPassword
                Party Link: \$generatedPartyLink
                
                Please use the above credentials to log in and share your party link with others.
                
                Kind regards,
                AmbassadorPass Team
            """.trimIndent()

            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "message/rfc822"
                putExtra(Intent.EXTRA_EMAIL, arrayOf(recipientEmail))
                putExtra(Intent.EXTRA_SUBJECT, subject)
                putExtra(Intent.EXTRA_TEXT, messageBody)
            }

            context.startActivity(Intent.createChooser(intent, "Choose an Email client:"))

            Log.d("EmailService", "Email intent created successfully for recipient: \$recipientEmail")

        } catch (e: Exception) {
            Log.e("EmailService", "Failed to create email intent", e)
        }
    }

    fun sendAmbassadorPartyUpdateEmail(recipientEmail: String, ambassadorName: String, partyName: String, generatedPartyLink: String) {
        try {
            val subject = "New Party Assigned: \$partyName"
            val messageBody = """
                Hello \$ambassadorName,
                
                You have been assigned as an ambassador for the new party "\$partyName".
                
                Here is your party link:
                Party Link: \$generatedPartyLink
                
                Please use the above link to invite others to the party.
                
                Kind regards,
                AmbassadorPass Team
            """.trimIndent()

            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "message/rfc822"
                putExtra(Intent.EXTRA_EMAIL, arrayOf(recipientEmail))
                putExtra(Intent.EXTRA_SUBJECT, subject)
                putExtra(Intent.EXTRA_TEXT, messageBody)
            }

            context.startActivity(Intent.createChooser(intent, "Choose an Email client:"))

            Log.d("EmailService", "Party update email intent created successfully for recipient: \$recipientEmail")

        } catch (e: Exception) {
            Log.e("EmailService", "Failed to create email intent", e)
        }
    }

    fun sendAdminEmail(recipientEmail: String, adminName: String, generatedPassword: String) {
        try {
            val subject = "Your Admin Credentials"
            val messageBody = """
                Hello \$adminName,
                
                You have been assigned as an admin in AmbassadorPass.
                
                Here are your credentials:
                Email: \$recipientEmail
                Password: \$generatedPassword
                
                Please use the above credentials to log in.
                
                Kind regards,
                AmbassadorPass Team
            """.trimIndent()

            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "message/rfc822"
                putExtra(Intent.EXTRA_EMAIL, arrayOf(recipientEmail))
                putExtra(Intent.EXTRA_SUBJECT, subject)
                putExtra(Intent.EXTRA_TEXT, messageBody)
            }

            context.startActivity(Intent.createChooser(intent, "Choose an Email client:"))

            Log.d("EmailService", "Admin email intent created successfully for recipient: \$recipientEmail")

        } catch (e: Exception) {
            Log.e("EmailService", "Failed to create email intent", e)
        }
    }
}
