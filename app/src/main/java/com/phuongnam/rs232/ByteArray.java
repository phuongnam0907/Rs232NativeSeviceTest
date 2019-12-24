package com.phuongnam.rs232;

import android.os.Parcel;
import android.os.Parcelable;

public class ByteArray implements Parcelable {
    private byte aByte;

    public ByteArray(byte mByte){
        aByte = mByte;
    }

    protected ByteArray(Parcel in) {
        aByte = in.readByte();
    }

    public static final Creator<ByteArray> CREATOR = new Creator<ByteArray>() {
        @Override
        public ByteArray createFromParcel(Parcel in) {
            return new ByteArray(in);
        }

        @Override
        public ByteArray[] newArray(int size) {
            return new ByteArray[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(aByte);
    }
}
