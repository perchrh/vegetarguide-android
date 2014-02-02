package no.vegetarguide.scanner.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;

import static org.apache.commons.lang3.StringUtils.trimToNull;

public class AdditivesDetails implements Parcelable {

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public AdditivesDetails createFromParcel(Parcel in) {
            return new AdditivesDetails(in);
        }

        public AdditivesDetails[] newArray(int size) {
            return new AdditivesDetails[size];
        }
    };
    @Expose
    private final boolean unspecified_aromas_verified;
    @Expose
    private final boolean unspecified_oils_verified;
    @Expose
    private final boolean maybe_animal_derived_e_numbers_verified;
    @Expose
    private final boolean maybe_other_animal_derived_additives_verified;
    @Expose
    private final String additives_verification_detail;

    public AdditivesDetails(boolean unspecified_aromas_verified, boolean unspecified_oils_verified, boolean maybe_animal_derived_e_numbers_verified, boolean maybe_other_animal_derived_additives_verified, String additives_verification_detail) {
        this.maybe_animal_derived_e_numbers_verified = maybe_animal_derived_e_numbers_verified;
        this.unspecified_aromas_verified = unspecified_aromas_verified;
        this.unspecified_oils_verified = unspecified_oils_verified;
        this.maybe_other_animal_derived_additives_verified = maybe_other_animal_derived_additives_verified;
        this.additives_verification_detail = trimToNull(additives_verification_detail);
    }

    public AdditivesDetails(Parcel in) {
        this.additives_verification_detail = in.readString();
        boolean[] temp = new boolean[4];
        in.readBooleanArray(temp);
        unspecified_aromas_verified = temp[0];
        unspecified_oils_verified = temp[1];
        maybe_animal_derived_e_numbers_verified = temp[2];
        maybe_other_animal_derived_additives_verified = temp[3];
    }

    public boolean isMaybe_animal_derived_e_numbers_verified() {
        return maybe_animal_derived_e_numbers_verified;
    }

    public boolean isUnspecified_aromas_verified() {
        return unspecified_aromas_verified;
    }

    public boolean isUnspecified_oils_verified() {
        return unspecified_oils_verified;
    }

    public boolean isMaybe_other_animal_derived_additives_verified() {
        return maybe_other_animal_derived_additives_verified;
    }

    public String getAdditives_verification_detail() {
        return additives_verification_detail;
    }

    public boolean allVerified() {
        return maybe_animal_derived_e_numbers_verified && unspecified_aromas_verified && unspecified_oils_verified && maybe_other_animal_derived_additives_verified;
    }


    @Override
    public String toString() {
        return "AdditivesDetails{" +
                "maybe_animal_derived_e_numbers_verified=" + maybe_animal_derived_e_numbers_verified +
                ", maybe_other_animal_derived_additives_verified=" + maybe_other_animal_derived_additives_verified +
                ", unspecified_aromas_verified=" + unspecified_aromas_verified +
                ", unspecified_oils_verified=" + unspecified_oils_verified +
                ", additives_verification_detail='" + additives_verification_detail + '\'' +
                '}';
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.additives_verification_detail);
        dest.writeBooleanArray(new boolean[]{unspecified_aromas_verified, unspecified_oils_verified, maybe_animal_derived_e_numbers_verified, maybe_other_animal_derived_additives_verified});
    }
}
