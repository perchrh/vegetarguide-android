package no.vegetarguide.scanner.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;

public class Category implements Parcelable, Comparable<Category> {

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

    @Expose
    private String code;

    public Category() {
    }

    public Category(String name, String code) {
        this.name = name;
        this.code = code;
    }

    public Category(Parcel in) {
        this.name = in.readString();
        this.code = in.readString();
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.code);
    }

    @Override
    public int compareTo(Category another) {
        return this.name.compareTo(another.name);
    }
}
