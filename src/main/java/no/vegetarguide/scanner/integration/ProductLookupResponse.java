package no.vegetarguide.scanner.integration;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.Collections;

import no.vegetarguide.scanner.model.Category;
import no.vegetarguide.scanner.model.LookupErrorType;
import no.vegetarguide.scanner.model.Product;
import no.vegetarguide.scanner.model.ResultType;
import no.vegetarguide.scanner.model.StatusType;

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
    @Expose
    private ArrayList<Category> categories; // assigned

    @SuppressWarnings("unused")
    public ProductLookupResponse() {
        //used by Gson
    }

    public ProductLookupResponse(Parcel in) {
        result = (ResultType) in.readSerializable();
        status = (StatusType) in.readSerializable();
        error = (LookupErrorType) in.readSerializable();
        product = in.readParcelable(Product.class.getClassLoader());
        categories = new ArrayList<>();
        in.readList(categories, Category.class.getClassLoader());
        Collections.sort(categories);
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


    public ArrayList<Category> getCategories() {
        return categories;
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
        dest.writeParcelable(product, flags);
        dest.writeList(categories);
    }

}