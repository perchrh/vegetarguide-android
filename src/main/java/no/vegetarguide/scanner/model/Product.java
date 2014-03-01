package no.vegetarguide.scanner.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Product implements Parcelable {

    private String title;
    private String brand;
    private String subtitle;
    private Boolean containsBodyParts;
    private Boolean containsRedListedAdditives;
    private Boolean containsMajorUnspecifiedAdditives;

    public Product() {
    }

    public static final Creator CREATOR = new Creator() {
        public Product createFromParcel(Parcel in) {
            return new Product(in);
        }

        public Product[] newArray(int size) {
            return new Product[size];
        }
    };


    public Product(Parcel in) {
        this.title = in.readString();
        this.brand = in.readString();
        this.subtitle = in.readString();
        this.containsBodyParts = (Boolean) in.readSerializable();
        this.containsRedListedAdditives = (Boolean) in.readSerializable();
        this.containsMajorUnspecifiedAdditives = (Boolean) in.readSerializable();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public void setContainsBodyParts(Boolean containsBodyParts) {
        this.containsBodyParts = containsBodyParts;
    }

    public Boolean isContainsBodyParts() {
        return containsBodyParts;
    }

    public void setContainsRedListedAdditives(Boolean containsRedListedAdditives) {
        this.containsRedListedAdditives = containsRedListedAdditives;
    }

    public Boolean isContainsRedListedAdditives() {
        return containsRedListedAdditives;
    }

    public void setContainsMajorUnspecifiedAdditives(Boolean containsMajorUnspecifiedAdditives) {
        this.containsMajorUnspecifiedAdditives = containsMajorUnspecifiedAdditives;
    }

    public Boolean isContainsMajorUnspecifiedAdditives() {
        return containsMajorUnspecifiedAdditives;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeString(this.brand);
        dest.writeString(this.subtitle);
        dest.writeSerializable(this.containsBodyParts);
        dest.writeSerializable(this.containsRedListedAdditives);
        dest.writeSerializable(this.containsMajorUnspecifiedAdditives);
    }
}
