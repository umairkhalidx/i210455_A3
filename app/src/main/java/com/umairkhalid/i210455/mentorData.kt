package com.umairkhalid.i210455

import android.os.Parcel
import android.os.Parcelable

data class mentorData(var mentorID: String = "",var name: String = "",
                      var occupation: String = "",var description: String = "",
                      var price: String = "",var profileImg: String = "",
                      var status: String = "", var favourite: String = ""): Parcelable {

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
        parcel.writeString(mentorID)
        parcel.writeString(name)
        parcel.writeString(occupation)
        parcel.writeString(description)
        parcel.writeString(price)
        parcel.writeString(profileImg)
        parcel.writeString(status)
        parcel.writeString(favourite)

    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<mentorData> {
        override fun createFromParcel(parcel: Parcel): mentorData {
            return mentorData(parcel)
        }

        override fun newArray(size: Int): Array<mentorData?> {
            return arrayOfNulls(size)
        }
    }
}
