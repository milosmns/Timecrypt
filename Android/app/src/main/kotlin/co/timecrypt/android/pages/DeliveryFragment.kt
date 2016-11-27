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
class DeliveryFragment() : TimecryptFragment(mutableListOf()) {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_delivery, container, false)
    }

    override fun onStart() {
        super.onStart()
        deliveryEmailTo.setText(message.emailTo)
        deliveryEmailFrom.setText(message.emailFrom)
        deliveryPassword.setText(message.password)

        deliveryEmailTo.addTextChangedListener(emailToWatcher)
        deliveryEmailFrom.addTextChangedListener(emailFromWatcher)
        deliveryPassword.addTextChangedListener(passwordWatcher)
    }

    override fun onStop() {
        super.onStop()
        deliveryEmailTo.removeTextChangedListener(emailToWatcher)
        deliveryEmailFrom.removeTextChangedListener(emailFromWatcher)
        deliveryPassword.removeTextChangedListener(passwordWatcher)
    }

    private val emailToWatcher = object : TextWatcherAdapter() {
        override fun afterTextChanged(text: Editable) {
            message.emailTo = text.toString()
        }
    }

    private val emailFromWatcher = object : TextWatcherAdapter() {
        override fun afterTextChanged(text: Editable) {
            message?.emailFrom = text.toString()
        }
    }

    private val passwordWatcher = object : TextWatcherAdapter() {
        override fun afterTextChanged(text: Editable) {
            message?.password = text.toString()
        }
    }

}