package com.iapolinarortiz.phonedialer

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import android.net.Uri
import android.provider.ContactsContract
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.Intents.intending
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.intent.rule.IntentsRule
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.GrantPermissionRule
import org.hamcrest.Matchers
import org.hamcrest.Matchers.allOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class MainActivityTask {
    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @get:Rule
    val intentsTestRule = IntentsRule()

    @get:Rule
    val grantPermissionReadContacts: GrantPermissionRule =
        GrantPermissionRule.grant(android.Manifest.permission.READ_CONTACTS)

    @Test
    fun validateDialing() {
        val phoneNumber = "+52 55 6616 3068"
        onView(withId(R.id.et_phone)).perform(
            typeText(phoneNumber),
            ViewActions.closeSoftKeyboard()
        )
        onView(withId(R.id.btn_dial)).perform(click())

        intended(
            allOf(
                IntentMatchers.hasAction(Intent.ACTION_DIAL),
                IntentMatchers.hasData(Uri.parse("tel:$phoneNumber"))
            )
        )
    }

    @Test
    fun validatePickContact() {
        // Result date
        val number = "+52 55 6616 3068"
        val intentContact = Intent()
        intentContact.data = Utils.getContactByName(number)
        intentContact.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        val activityResult: Instrumentation.ActivityResult =
            Instrumentation.ActivityResult(Activity.RESULT_OK, intentContact)
        intending(IntentMatchers.hasAction(Intent.ACTION_PICK)).respondWith(activityResult)

        onView(withId(R.id.btn_contacts)).perform(click())
        onView(withId(R.id.et_phone)).check(matches(withText(number)))
    }
}