package com.umairkhalid.i210455

import android.os.Parcel
import android.os.Parcelable

data class reviewData(val userID: String = "", val name: String = "",
                      val review: String = ""): Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(userID)
        parcel.writeString(name)
        parcel.writeString(review)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<reviewData> {
        override fun createFromParcel(parcel: Parcel): reviewData {
            return reviewData(parcel)
        }

        override fun newArray(size: Int): Array<reviewData?> {
            return arrayOfNulls(size)
        }
    }
}
