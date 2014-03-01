package no.vegetarguide.scanner.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.net.URL;

public class Product implements Parcelable {
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Product createFromParcel(Parcel in) {
            return new Product(in);
        }

        public Product[] newArray(int size) {
            return new Product[size];
        }
    };
    private String title;
    private String brand;
    private String subtitle;

    private String category;
    private String subcategory;
    private String _id;
    private String gtin;
    private URL imageurl;

    private Boolean containsBodyParts;
    private Boolean containsRedListedAdditives;
    private Boolean containsMajorUnspecifiedAdditives;
    private Boolean containsHoney;
    private Boolean containsEggs;
    private Boolean containsAnimalMilk;
    private Boolean manufacturerConfirmsProductIsVegan;
    private Boolean containsPossibleAnimalAdditives;
    private Boolean containsPossibleAnimalEnumbers;

    public Product() {
    }

    public Product(Parcel in) {
        this.title = in.readString();
        this.brand = in.readString();
        this.subtitle = in.readString();
        this.category = in.readString();
        this.subcategory = in.readString();
        this._id = in.readString();
        this.gtin = in.readString();
        this.imageurl = (URL) in.readSerializable();
        this.containsBodyParts = (Boolean) in.readSerializable();
        this.containsRedListedAdditives = (Boolean) in.readSerializable();
        this.containsMajorUnspecifiedAdditives = (Boolean) in.readSerializable();
        this.containsHoney = (Boolean) in.readSerializable();
        this.containsEggs = (Boolean) in.readSerializable();
        this.containsAnimalMilk = (Boolean) in.readSerializable();
        this.manufacturerConfirmsProductIsVegan = (Boolean) in.readSerializable();
        this.containsPossibleAnimalAdditives = (Boolean) in.readSerializable();
        this.containsPossibleAnimalEnumbers = (Boolean) in.readSerializable();
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

    public void setContainsHoney(Boolean containsHoney) {
        this.containsHoney = containsHoney;
    }

    public Boolean isContainsHoney() {
        return containsHoney;
    }

    public void setContainsEggs(Boolean containsEggs) {
        this.containsEggs = containsEggs;
    }

    public Boolean isContainsEggs() {
        return containsEggs;
    }

    public void setContainsAnimalMilk(Boolean containsAnimalMilk) {
        this.containsAnimalMilk = containsAnimalMilk;
    }

    public Boolean isContainsAnimalMilk() {
        return containsAnimalMilk;
    }

    public void setManufacturerConfirmsProductIsVegan(Boolean manufacturerConfirmsProductIsVegan) {
        this.manufacturerConfirmsProductIsVegan = manufacturerConfirmsProductIsVegan;
    }

    public Boolean isManufacturerConfirmsProductIsVegan() {
        return manufacturerConfirmsProductIsVegan;
    }

    public void setContainsPossibleAnimalAdditives(Boolean containsPossibleAnimalAdditives) {
        this.containsPossibleAnimalAdditives = containsPossibleAnimalAdditives;
    }

    public Boolean isContainsPossibleAnimalAdditives() {
        return containsPossibleAnimalAdditives;
    }

    public void setContainsPossibleAnimalEnumbers(Boolean containsPossibleAnimalEnumbers) {
        this.containsPossibleAnimalEnumbers = containsPossibleAnimalEnumbers;
    }

    public Boolean isContainsPossibleAnimalEnumbers() {
        return containsPossibleAnimalEnumbers;
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
        dest.writeString(this.category);
        dest.writeString(this.subcategory);
        dest.writeString(this._id);
        dest.writeString(this.gtin);
        dest.writeSerializable(this.imageurl);
        dest.writeSerializable(this.containsBodyParts);
        dest.writeSerializable(this.containsRedListedAdditives);
        dest.writeSerializable(this.containsMajorUnspecifiedAdditives);
        dest.writeSerializable(this.containsHoney);
        dest.writeSerializable(this.containsEggs);
        dest.writeSerializable(this.containsAnimalMilk);
        dest.writeSerializable(this.manufacturerConfirmsProductIsVegan);
        dest.writeSerializable(this.containsPossibleAnimalAdditives);
        dest.writeSerializable(this.containsPossibleAnimalEnumbers);
    }
}
