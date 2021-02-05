package com.sh.pytalapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChanLe implements Parcelable, Serializable {
    private String name;
    private String code;
    private int tiLeChan;
    private int tiLeLe;

    protected ChanLe(Parcel in) {
        name = in.readString();
        code = in.readString();
        tiLeChan = in.readInt();
        tiLeLe = in.readInt();
    }

    public static final Creator<ChanLe> CREATOR = new Creator<ChanLe>() {
        @Override
        public ChanLe createFromParcel(Parcel in) {
            return new ChanLe(in);
        }

        @Override
        public ChanLe[] newArray(int size) {
            return new ChanLe[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(code);
        dest.writeInt(tiLeChan);
        dest.writeInt(tiLeLe);
    }
}
