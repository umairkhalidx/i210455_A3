package com.umairkhalid.i210455

import android.os.Parcel
import android.os.Parcelable
data class user_data(val name: String = "", val email: String = "",
    val contact: String = "", val country: String = "",
    val city: String = "", val password: String = ""): Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(email)
        parcel.writeString(contact)
        parcel.writeString(country)
        parcel.writeString(city)
        parcel.writeString(password)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<user_data> {
        override fun createFromParcel(parcel: Parcel): user_data {
            return user_data(parcel)
        }

        override fun newArray(size: Int): Array<user_data?> {
            return arrayOfNulls(size)
        }
    }
}