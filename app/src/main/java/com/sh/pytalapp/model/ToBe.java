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
public class ToBe implements Parcelable, Serializable {

    private String name;
    private String code;
    private int soLanQuayTo;
    private int soLanQuayBe;
    private int tongSoLanQuay;

    protected ToBe(Parcel in) {
        name = in.readString();
        code = in.readString();
        soLanQuayTo = in.readInt();
        soLanQuayBe = in.readInt();
        tongSoLanQuay = in.readInt();
    }

    public static final Creator<ToBe> CREATOR = new Creator<ToBe>() {
        @Override
        public ToBe createFromParcel(Parcel in) {
            return new ToBe(in);
        }

        @Override
        public ToBe[] newArray(int size) {
            return new ToBe[size];
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
        dest.writeInt(soLanQuayTo);
        dest.writeInt(soLanQuayBe);
        dest.writeInt(tongSoLanQuay);
    }
}
