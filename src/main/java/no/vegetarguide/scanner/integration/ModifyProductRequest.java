package no.vegetarguide.scanner.integration;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;

import no.vegetarguide.scanner.model.Product;

public class ModifyProductRequest implements Parcelable {

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public ModifyProductRequest createFromParcel(Parcel in) {
            return new ModifyProductRequest(in);
        }

        public ModifyProductRequest[] newArray(int size) {
            return new ModifyProductRequest[size];
        }
    };
    @Expose
    private Product product;

    @Expose
    private String objectId;

    @SuppressWarnings("unused")
    public ModifyProductRequest() {
        //used by Gson
    }

    public ModifyProductRequest(ProductLookupResponse productLookupResponse) {
        this.product = productLookupResponse.getProduct();
        this.objectId = productLookupResponse.getObjectId();
    }

    public ModifyProductRequest(Parcel in) {
        this.product = in.readParcelable(Product.class.getClassLoader());
        this.objectId = in.readString();
    }

    public Product getProduct() {
        return product;
    }

    public String getObjectId() {
        return objectId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.product, flags);
        dest.writeString(this.objectId);
    }
}
