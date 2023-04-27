package com.iapolinarortiz.phonedialer

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import androidx.activity.result.contract.ActivityResultContracts
import com.iapolinarortiz.phonedialer.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var activityMainBinding: ActivityMainBinding
    private var number: String = ""
    private val getContent =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            run {
                if (result.resultCode == Activity.RESULT_OK) {
                    val data: Intent? = result.data
                    val contactIntentUri = data?.data
                    requireNotNull(contactIntentUri) { "ContactUri must ve non null" }

                    val cursor: Cursor? = this.contentResolver.query(
                        contactIntentUri,
                        arrayOf(ContactsContract.CommonDataKinds.Phone.NUMBER),
                        null,
                        null,
                        null
                    )
                    cursor?.moveToFirst();
                    val numberToIndex =
                        cursor?.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
                            ?: return@registerForActivityResult
                    val numberContact =
                        cursor.getString(numberToIndex)
                    cursor.close()
                    number = numberContact
                    activityMainBinding.etPhone.setText(number)
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityMainBinding.root)

        activityMainBinding.btnDial.setOnClickListener {
            if (activityMainBinding.etPhone.text.isNullOrBlank()) {
                activityMainBinding.textInputNumber.error = "Need to type a number"
                return@setOnClickListener
            }
            val intent = Intent(
                Intent.ACTION_DIAL,
                Uri.parse("tel:${activityMainBinding.etPhone.text.toString()}")
            )
            startActivity(intent)
        }

        activityMainBinding.btnContacts.setOnClickListener {
            val intent =
                Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI)
            getContent.launch(intent)
        }
    }
}