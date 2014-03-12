package no.vegetarguide.scanner.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;

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
    @Expose
    private String title;
    @Expose
    private String brand;
    @Expose
    private String subtitle;

    @Expose
    private String category;
    @Expose
    private String subcategory;
    @Expose
    private String _id;
    @Expose
    private String gtin;
    @Expose
    private URL imageurl;
    @Expose
    private String general_comment;

    @Expose
    private Ingredients ingredients = new Ingredients();

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
        this.general_comment = in.readString();
        this.ingredients = in.readParcelable(Ingredients.class.getClassLoader());
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
        dest.writeString(this.general_comment);
        dest.writeParcelable(this.ingredients, 0);
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

    public String getGeneral_comment() {
        return general_comment;
    }

    public void setGeneral_comment(String general_comment) {
        this.general_comment = general_comment;
    }

    public Ingredients getIngredients() {
        return ingredients;
    }

    public void setIngredients(Ingredients ingredients) {
        this.ingredients = ingredients;
    }
}
