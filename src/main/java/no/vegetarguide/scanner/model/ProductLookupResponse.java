package no.vegetarguide.scanner.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;

import java.net.URL;

public class ProductLookupResponse implements Parcelable {

    @SuppressWarnings("unused")
    // not exposed to Gson
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public ProductLookupResponse createFromParcel(Parcel in) {
            return new ProductLookupResponse(in);
        }

        public ProductLookupResponse[] newArray(int size) {
            return new ProductLookupResponse[size];
        }
    };
    @Expose
    private ResultType result;
    @Expose
    private StatusType status;
    @Expose
    private LookupErrorType error;
    @Expose
    private String title;
    @Expose
    private String subtitle;
    @Expose
    private String category;
    @Expose
    private String subcategory;
    @Expose
    private String brand;
    @Expose
    private String _id;
    @Expose
    private String gtin;
    @Expose
    private URL imageurl;
    @Expose
    private Boolean contains_animal_additives;
    @Expose
    private Boolean contains_animal_milk;
    @Expose
    private Boolean contains_bodyparts;
    @Expose
    private Boolean contains_eggs;
    @Expose
    private Boolean contains_honey;
    @Expose
    private Boolean otherwise_animal_derived;
    @Expose
    private Boolean animal_tested;
    @Expose
    private String otherwise_animal_derived_detail;
    @Expose
    private AdditivesDetails additivesDetails;

    @Expose
    private String user_commentary;

    @SuppressWarnings("unused")
    public ProductLookupResponse() {
        //used by Gson
    }

    public ProductLookupResponse(Parcel in) {
        result = (ResultType) in.readSerializable();
        status = (StatusType) in.readSerializable();
        error = (LookupErrorType) in.readSerializable();
        title = in.readString();
        subtitle = in.readString();
        category = in.readString();
        subcategory = in.readString();
        brand = in.readString();
        _id = in.readString();
        gtin = in.readString();
        imageurl = (URL) in.readSerializable();
        contains_animal_additives = (Boolean) in.readSerializable();
        contains_animal_milk = (Boolean) in.readSerializable();
        contains_bodyparts = (Boolean) in.readSerializable();
        contains_eggs = (Boolean) in.readSerializable();
        contains_honey = (Boolean) in.readSerializable();
        otherwise_animal_derived = (Boolean) in.readSerializable();
        otherwise_animal_derived_detail = in.readString();
        animal_tested = (Boolean) in.readSerializable();
        additivesDetails = in.readParcelable(AdditivesDetails.class.getClassLoader());
        user_commentary = in.readString();
    }

    public boolean hasError() {
        return error != null;
    }

    public ResultType getResult() {
        return result;
    }

    public StatusType getStatus() {
        return status;
    }

    public LookupErrorType getError() {
        return error;
    }

    public String getTitle() {
        return title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public String getCategory() {
        return category;
    }

    public String getSubcategory() {
        return subcategory;
    }

    public String getBrand() {
        return brand;
    }

    public String get_id() {
        return _id;
    }

    public String getGtin() {
        return gtin;
    }

    public URL getImageurl() {
        return imageurl;
    }

    public Boolean getContains_animal_additives() {
        return contains_animal_additives;
    }

    public Boolean getContains_animal_milk() {
        return contains_animal_milk;
    }

    public Boolean getContains_bodyparts() {
        return contains_bodyparts;
    }

    public Boolean getContains_eggs() {
        return contains_eggs;
    }

    public Boolean getContains_honey() {
        return contains_honey;
    }

    public Boolean getOtherwise_animal_derived() {
        return otherwise_animal_derived;
    }

    public String getOtherwise_animal_derived_detail() {
        return otherwise_animal_derived_detail;
    }

    public Boolean getAnimal_tested() {
        return animal_tested;
    }

    public AdditivesDetails getAdditivesDetails() {
        return additivesDetails;
    }

    public String getUser_commentary() {
        return user_commentary;
    }

    @Override
    public String toString() {
        return "ProductLookupResponse{" +
                "result=" + result +
                ", status=" + status +
                ", error=" + error +
                ", title='" + title + '\'' +
                ", subtitle='" + subtitle + '\'' +
                ", category='" + category + '\'' +
                ", subcategory='" + subcategory + '\'' +
                ", brand='" + brand + '\'' +
                ", gtin='" + gtin + '\'' +
                ", imageurl=" + imageurl +
                ", contains_animal_additives=" + contains_animal_additives +
                ", contains_animal_milk=" + contains_animal_milk +
                ", contains_bodyparts=" + contains_bodyparts +
                ", contains_eggs=" + contains_eggs +
                ", contains_honey=" + contains_honey +
                ", otherwise_animal_derived=" + otherwise_animal_derived +
                ", animal_tested=" + animal_tested +
                ", otherwise_animal_derived_detail='" + otherwise_animal_derived_detail + '\'' +
                ", additivesDetails=" + additivesDetails +
                ", user_commentary='" + user_commentary + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeSerializable(result);
        dest.writeSerializable(status);
        dest.writeSerializable(error);
        dest.writeString(title);
        dest.writeString(subtitle);
        dest.writeString(category);
        dest.writeString(subcategory);
        dest.writeString(brand);
        dest.writeString(_id);
        dest.writeString(gtin);
        dest.writeSerializable(imageurl);
        dest.writeSerializable(contains_animal_additives);
        dest.writeSerializable(contains_animal_milk);
        dest.writeSerializable(contains_bodyparts);
        dest.writeSerializable(contains_eggs);
        dest.writeSerializable(contains_honey);
        dest.writeSerializable(otherwise_animal_derived);
        dest.writeString(otherwise_animal_derived_detail);
        dest.writeSerializable(animal_tested);
        dest.writeParcelable(additivesDetails, 0);
        dest.writeString(user_commentary);
    }

}