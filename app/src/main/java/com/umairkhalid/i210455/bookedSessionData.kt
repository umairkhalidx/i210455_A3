package com.umairkhalid.i210455

import android.os.Parcel
import android.os.Parcelable

data class bookedSessionData(val userID: String = "",val mentorID: String = "",
                             val name: String = "", val occupation: String = "",
                             val date: String = "", val time: String = "",
                             val profileImg: String = ""
): Parcelable {

    constructor(parcel: Parcel) : this(
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
        parcel.writeString(mentorID)
        parcel.writeString(name)
        parcel.writeString(occupation)
        parcel.writeString(date)
        parcel.writeString(time)
        parcel.writeString(profileImg)

    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<bookedSessionData> {
        override fun createFromParcel(parcel: Parcel): bookedSessionData {
            return bookedSessionData(parcel)
        }

        override fun newArray(size: Int): Array<bookedSessionData?> {
            return arrayOfNulls(size)
        }
    }
}
