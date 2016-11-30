package co.timecrypt.android.v2.api

import android.os.Parcel
import android.os.Parcelable
import com.squareup.moshi.Json
import java.util.*

/**
 * Interpretation of a Timecrypt message.
 *
 * @param text Contents of the message, *must not be empty*
 * @param views How many times is this message allowed to be shown, can be empty, defaults to 1
 * @param destructDate When to self-destruct the message (format: yyyy-mm-dd), must be at least one day in the future, can be empty, defaults to `tomorrow`
 * @param emailTo Where to send the invitation to this message, can be empty, no default
 * @param emailFrom Where to send the "message read" notification, can be empty, no default
 * @param title Title of the message, can be empty, no default
 * @param password Which passphrase to use to encrypt the message, can be empty, defaults to datastore's passphrase
 */
class TimecryptMessage(
        var text: String,
        var views: Int = 1,
        @Json(name = "lifetime") var destructDate: Date? = null,
        @Json(name = "email_to") var emailTo: String? = null,
        @Json(name = "email_from") var emailFrom: String? = null,
        var title: String? = null,
        var password: String? = null
) : Parcelable {

    var extra_DestructOptionPicked = 0

    override fun toString(): String {
        return "TimecryptMessage(text='$text', views=$views, destructDate=$destructDate, " +
                "emailTo=$emailTo, emailFrom=$emailFrom, title=$title, password=$password)"
    }

    /* Parcelable implementation */

    companion object {
        @JvmField
        @Suppress("unused")
        val CREATOR: Parcelable.Creator<TimecryptMessage> = object : Parcelable.Creator<TimecryptMessage> {
            override fun createFromParcel(source: Parcel): TimecryptMessage = TimecryptMessage(source)
            override fun newArray(size: Int): Array<TimecryptMessage?> = arrayOfNulls(size)
        }
    }

    constructor(p: Parcel) : this(p.readString(), p.readInt(), p.readSerializable() as Date?, p.readString(), p.readString(), p.readString(), p.readString()) {
        extra_DestructOptionPicked = p.readInt()
    }

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeString(text)
        dest?.writeInt(views)
        dest?.writeSerializable(destructDate)
        dest?.writeString(emailTo)
        dest?.writeString(emailFrom)
        dest?.writeString(title)
        dest?.writeString(password)
        dest?.writeInt(extra_DestructOptionPicked)
    }

}