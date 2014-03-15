package no.vegetarguide.scanner.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;

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
    private Product product;


    @SuppressWarnings("unused")
    public ProductLookupResponse() {
        //used by Gson
    }

    public ProductLookupResponse(Parcel in) {
        result = (ResultType) in.readSerializable();
        status = (StatusType) in.readSerializable();
        error = (LookupErrorType) in.readSerializable();
        product = in.readParcelable(Product.class.getClassLoader());
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

    public Product getProduct() {
        return product;
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
        dest.writeParcelable(product, 0);
    }

}