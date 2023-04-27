package com.iapolinarortiz.phonedialer

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.ContactsContract
import androidx.test.platform.app.InstrumentationRegistry

class Utils {
    companion object {
        fun getContactByName(phone: String): Uri? {
            val context: Context = InstrumentationRegistry.getInstrumentation().targetContext
            val cursor: Cursor = context.contentResolver.query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null,
                null,
                null,
                null
            ) ?: return null

            if (cursor.count > 0) {
                while (cursor.moveToNext()) {
                    val id: String =
                        cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone._ID))
                    val phoneNumber: String =
                        cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                    if (phoneNumber == phone) {
                        return Uri.withAppendedPath(ContactsContract.Data.CONTENT_URI, id)
                    }
                }
            }

            return null
        }
    }
}
