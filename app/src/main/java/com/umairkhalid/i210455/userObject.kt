package com.umairkhalid.i210455

import android.os.Parcel
import android.os.Parcelable

data class userObject(var userID: String = "",var name: String = "",
                      var email: String = "",var city: String = "",
                      var country: String = "",var contact: String = "",
                      var profileImg: String = "", var coverImg: String = ""): Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(userID)
        parcel.writeString(name)
        parcel.writeString(email)
        parcel.writeString(city)
        parcel.writeString(country)
        parcel.writeString(contact)
        parcel.writeString(profileImg)
        parcel.writeString(coverImg)

    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<userObject> {
        override fun createFromParcel(parcel: Parcel): userObject {
            return userObject(parcel)
        }

        override fun newArray(size: Int): Array<userObject?> {
            return arrayOfNulls(size)
        }
    }
}