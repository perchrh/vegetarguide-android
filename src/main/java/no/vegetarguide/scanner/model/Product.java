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
    private Boolean containsInsectExcretions;
    private Boolean containsEggs;
    private Boolean containsAnimalMilk;
    private Boolean manufacturerConfirmsProductIsVegan;
    private Boolean manufacturerConfirmsProductIsLactoOvoVegetarian;

    private Boolean containsPossibleAnimalAdditives;
    private Boolean containsPossibleAnimalEnumbers;

    private String lactoOvoVegetarianComment;
    private String notLactoOvoVegetarianComment;
    private String generalComment;
    private String confirmedLactoOvoVegetarianComment;
    private String confirmedVeganComment;

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
        this.containsInsectExcretions = (Boolean) in.readSerializable();
        this.containsEggs = (Boolean) in.readSerializable();
        this.containsAnimalMilk = (Boolean) in.readSerializable();
        this.manufacturerConfirmsProductIsVegan = (Boolean) in.readSerializable();
        this.containsPossibleAnimalAdditives = (Boolean) in.readSerializable();
        this.containsPossibleAnimalEnumbers = (Boolean) in.readSerializable();
        this.lactoOvoVegetarianComment = in.readString();
        this.notLactoOvoVegetarianComment = in.readString();
        this.confirmedVeganComment = in.readString();
        this.generalComment = in.readString();
        this.manufacturerConfirmsProductIsLactoOvoVegetarian = (Boolean) in.readSerializable();
        this.confirmedLactoOvoVegetarianComment = in.readString();
    }

    public boolean isMaybeVegan() {
        return isMaybeLactoOvoVegetarian()
                && Boolean.FALSE.equals(this.containsInsectExcretions)
                && Boolean.FALSE.equals(this.containsEggs)
                && Boolean.FALSE.equals(this.containsAnimalMilk);
    }

    public boolean isAnimalDerivedForCertain() {
        return Boolean.TRUE.equals(this.containsBodyParts)
                || Boolean.TRUE.equals(this.containsRedListedAdditives);
    }

    public boolean isMaybeLactoOvoVegetarian() {
        return !isAnimalDerivedForCertain();
    }

    public boolean isLactoOvoVegetarian() {
        return isMaybeLactoOvoVegetarian() && unspecifiedAdditivesAreLactoOvoVegetarian();
    }

    private boolean unspecifiedAdditivesAreLactoOvoVegetarian() {
        return Boolean.FALSE.equals(containsMajorUnspecifiedAdditives)
                || Boolean.TRUE.equals(manufacturerConfirmsProductIsLactoOvoVegetarian);
    }

    public boolean isVegan() {
        final boolean isCandidate = isMaybeVegan();
        final boolean isConfirmed = Boolean.TRUE.equals(manufacturerConfirmsProductIsVegan);

        return ((isCandidate && isConfirmed)
                || (isCandidate && Boolean.FALSE.equals(containsPossibleAnimalAdditives)
                && Boolean.FALSE.equals(containsPossibleAnimalEnumbers)
                && Boolean.FALSE.equals(containsMajorUnspecifiedAdditives)));
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSubcategory() {
        return subcategory;
    }

    public void setSubcategory(String subcategory) {
        this.subcategory = subcategory;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getGtin() {
        return gtin;
    }

    public void setGtin(String gtin) {
        this.gtin = gtin;
    }

    public URL getImageurl() {
        return imageurl;
    }

    public void setImageurl(URL imageurl) {
        this.imageurl = imageurl;
    }

    public Boolean getContainsBodyParts() {
        return containsBodyParts;
    }

    public void setContainsBodyParts(Boolean containsBodyParts) {
        this.containsBodyParts = containsBodyParts;
    }

    public Boolean getContainsRedListedAdditives() {
        return containsRedListedAdditives;
    }

    public void setContainsRedListedAdditives(Boolean containsRedListedAdditives) {
        this.containsRedListedAdditives = containsRedListedAdditives;
    }

    public Boolean getContainsMajorUnspecifiedAdditives() {
        return containsMajorUnspecifiedAdditives;
    }

    public void setContainsMajorUnspecifiedAdditives(Boolean containsMajorUnspecifiedAdditives) {
        this.containsMajorUnspecifiedAdditives = containsMajorUnspecifiedAdditives;
    }

    public Boolean getContainsInsectExcretions() {
        return containsInsectExcretions;
    }

    public void setContainsInsectExcretions(Boolean containsInsectExcretions) {
        this.containsInsectExcretions = containsInsectExcretions;
    }

    public Boolean getContainsEggs() {
        return containsEggs;
    }

    public void setContainsEggs(Boolean containsEggs) {
        this.containsEggs = containsEggs;
    }

    public Boolean getContainsAnimalMilk() {
        return containsAnimalMilk;
    }

    public void setContainsAnimalMilk(Boolean containsAnimalMilk) {
        this.containsAnimalMilk = containsAnimalMilk;
    }

    public Boolean getManufacturerConfirmsProductIsVegan() {
        return manufacturerConfirmsProductIsVegan;
    }

    public void setManufacturerConfirmsProductIsVegan(Boolean manufacturerConfirmsProductIsVegan) {
        this.manufacturerConfirmsProductIsVegan = manufacturerConfirmsProductIsVegan;
    }

    public Boolean getContainsPossibleAnimalAdditives() {
        return containsPossibleAnimalAdditives;
    }

    public void setContainsPossibleAnimalAdditives(Boolean containsPossibleAnimalAdditives) {
        this.containsPossibleAnimalAdditives = containsPossibleAnimalAdditives;
    }

    public Boolean getContainsPossibleAnimalEnumbers() {
        return containsPossibleAnimalEnumbers;
    }

    public void setContainsPossibleAnimalEnumbers(Boolean containsPossibleAnimalEnumbers) {
        this.containsPossibleAnimalEnumbers = containsPossibleAnimalEnumbers;
    }

    public String getLactoOvoVegetarianComment() {
        return lactoOvoVegetarianComment;
    }

    public void setLactoOvoVegetarianComment(String lactoOvoVegetarianComment) {
        this.lactoOvoVegetarianComment = lactoOvoVegetarianComment;
    }

    public String getNotLactoOvoVegetarianComment() {
        return notLactoOvoVegetarianComment;
    }

    public void setNotLactoOvoVegetarianComment(String notLactoOvoVegetarianComment) {
        this.notLactoOvoVegetarianComment = notLactoOvoVegetarianComment;
    }

    public String getGeneralComment() {
        return generalComment;
    }

    public void setGeneralComment(String generalComment) {
        this.generalComment = generalComment;
    }

    public Boolean getManufacturerConfirmsProductIsLactoOvoVegetarian() {
        return manufacturerConfirmsProductIsLactoOvoVegetarian;
    }

    public void setManufacturerConfirmsProductIsLactoOvoVegetarian(Boolean manufacturerConfirmsProductIsLactoOvoVegetarian) {
        this.manufacturerConfirmsProductIsLactoOvoVegetarian = manufacturerConfirmsProductIsLactoOvoVegetarian;
    }

    public String getConfirmedLactoOvoVegetarianComment() {
        return confirmedLactoOvoVegetarianComment;
    }

    public void setConfirmedLactoOvoVegetarianComment(String confirmedLactoOvoVegetarianComment) {
        this.confirmedLactoOvoVegetarianComment = confirmedLactoOvoVegetarianComment;
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
        dest.writeSerializable(this.containsInsectExcretions);
        dest.writeSerializable(this.containsEggs);
        dest.writeSerializable(this.containsAnimalMilk);
        dest.writeSerializable(this.manufacturerConfirmsProductIsVegan);
        dest.writeSerializable(this.containsPossibleAnimalAdditives);
        dest.writeSerializable(this.containsPossibleAnimalEnumbers);
        dest.writeString(this.lactoOvoVegetarianComment);
        dest.writeString(this.notLactoOvoVegetarianComment);
        dest.writeString(this.confirmedVeganComment);
        dest.writeString(this.generalComment);
        dest.writeSerializable(this.manufacturerConfirmsProductIsLactoOvoVegetarian);
        dest.writeString(this.confirmedLactoOvoVegetarianComment);
    }

    public String getConfirmedVeganComment() {
        return confirmedVeganComment;
    }

    public void setConfirmedVeganComment(String confirmedVeganComment) {
        this.confirmedVeganComment = confirmedVeganComment;
    }
}
