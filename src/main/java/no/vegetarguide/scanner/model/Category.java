package no.vegetarguide.scanner.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;

public class Category implements Parcelable {

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Category createFromParcel(Parcel in) {
            return new Category(in);
        }

        public Category[] newArray(int size) {
            return new Category[size];
        }
    };
    @Expose
    private String name;

    public Category() {
    }

    public Category(Parcel in) {
        this.name = in.readString();
    }

    public String getName() {
        return name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
    }
}
