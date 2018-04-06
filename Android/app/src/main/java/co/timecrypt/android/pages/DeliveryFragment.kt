package co.timecrypt.android.pages

import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import co.timecrypt.android.R
import co.timecrypt.android.helpers.TextWatcherAdapter
import kotlinx.android.synthetic.main.fragment_delivery.*

/**
 * The fragment containing the destruct date picker.
 */
class DeliveryFragment : TimecryptFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_delivery, container, false)
    }

    // this also gets called by super-class on fragment start
    override fun onMessageUpdated() {
        deliveryEmailTo.setText(message.emailTo)
        deliveryEmailFrom.setText(message.emailFrom)
        deliveryPassword.setText(message.password)
    }

    override fun onStart() {
        super.onStart()

        // assign click listeners
        deliveryEmailTo.addTextChangedListener(emailToWatcher)
        deliveryEmailFrom.addTextChangedListener(emailFromWatcher)
        deliveryPassword.addTextChangedListener(passwordWatcher)

        // assign click listeners (first View toggles second's visibility)
        deliveryInfoDestination.setOnClickListener { deliveryDestinationExplained.toggleVisibility() }
        deliveryInfoNotification.setOnClickListener { deliveryNotificationExplained.toggleVisibility() }
    }

    override fun onStop() {
        super.onStop()

        // stop listening for changes
        deliveryEmailTo.removeTextChangedListener(emailToWatcher)
        deliveryEmailFrom.removeTextChangedListener(emailFromWatcher)
        deliveryPassword.removeTextChangedListener(passwordWatcher)
    }

    /* Message data updates on each text change */

    private val emailToWatcher = object : TextWatcherAdapter() {
        override fun afterTextChanged(text: Editable) {
            message.emailTo = text.toString()
        }
    }

    private val emailFromWatcher = object : TextWatcherAdapter() {
        override fun afterTextChanged(text: Editable) {
            message.emailFrom = text.toString()
        }
    }

    private val passwordWatcher = object : TextWatcherAdapter() {
        override fun afterTextChanged(text: Editable) {
            message.password = text.toString()
        }
    }

}