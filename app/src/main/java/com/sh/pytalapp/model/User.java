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
public class User implements Parcelable, Serializable {

    private String codeOTP;
    private String createdDate;
    private String serialDevice;
    private String lastLogin;
    private String role;

    protected User(Parcel in) {
        codeOTP = in.readString();
        createdDate = in.readString();
        serialDevice = in.readString();
        lastLogin = in.readString();
        role = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(codeOTP);
        dest.writeString(createdDate);
        dest.writeString(serialDevice);
        dest.writeString(lastLogin);
        dest.writeString(role);
    }
}
