package co.timecrypt.android.pages

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import co.timecrypt.android.R
import kotlinx.android.synthetic.main.fragment_delivery.*

/**
 * The fragment containing the destruct date picker.
 */
class DeliveryFragment() : TimecryptFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_delivery, container, false)
    }

    override fun onStart() {
        super.onStart()
        message?.let {
            deliveryEmailTo.setText(message!!.emailTo)
            deliveryEmailFrom.setText(message!!.emailFrom)
            deliveryPassword.setText(message!!.password)
        }

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

    private val emailToWatcher = object : TextWatcher {
        override fun afterTextChanged(text: Editable) {
            message?.emailTo = text.toString()
        }

        override fun beforeTextChanged(text: CharSequence, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(text: CharSequence, start: Int, before: Int, count: Int) {
        }
    }

    private val emailFromWatcher = object : TextWatcher {
        override fun afterTextChanged(text: Editable) {
            message?.emailFrom = text.toString()
        }

        override fun beforeTextChanged(text: CharSequence, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(text: CharSequence, start: Int, before: Int, count: Int) {
        }
    }

    private val passwordWatcher = object : TextWatcher {
        override fun afterTextChanged(text: Editable) {
            message?.password = text.toString()
        }

        override fun beforeTextChanged(text: CharSequence, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(text: CharSequence, start: Int, before: Int, count: Int) {
        }
    }

}