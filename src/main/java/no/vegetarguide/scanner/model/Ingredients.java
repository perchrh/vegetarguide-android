package no.vegetarguide.scanner.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;

public class Ingredients implements Parcelable {

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Ingredients createFromParcel(Parcel in) {
            return new Ingredients(in);
        }

        public Ingredients[] newArray(int size) {
            return new Ingredients[size];
        }
    };
    @Expose
    private Boolean contains_bodyparts;
    @Expose
    private Boolean contains_animal_additives;
    @Expose
    private Boolean contains_possible_animal_additives;
    @Expose
    private Boolean contains_unspecified_possibly_animal_additives;
    @Expose
    private Boolean contains_insect_excretions;
    @Expose
    private Boolean contains_eggs;
    @Expose
    private Boolean contains_animal_milk;

    @SuppressWarnings("unused")
    public Ingredients() {
        // Used by GSON
    }

    public Ingredients(Parcel in) {
        this.contains_bodyparts = (Boolean) in.readSerializable();
        this.contains_animal_additives = (Boolean) in.readSerializable();
        this.contains_possible_animal_additives = (Boolean) in.readSerializable();
        this.contains_unspecified_possibly_animal_additives = (Boolean) in.readSerializable();
        this.contains_insect_excretions = (Boolean) in.readSerializable();
        this.contains_eggs = (Boolean) in.readSerializable();
        this.contains_animal_milk = (Boolean) in.readSerializable();
    }

    public Boolean getContains_bodyparts() {
        return contains_bodyparts;
    }

    public void setContains_bodyparts(Boolean contains_bodyparts) {
        this.contains_bodyparts = contains_bodyparts;
    }

    public Boolean getContains_animal_additives() {
        return contains_animal_additives;
    }

    public void setContains_animal_additives(Boolean contains_animal_additives) {
        this.contains_animal_additives = contains_animal_additives;
    }

    public Boolean getContains_possible_animal_additives() {
        return contains_possible_animal_additives;
    }

    public void setContains_possible_animal_additives(Boolean contains_possible_animal_additives) {
        this.contains_possible_animal_additives = contains_possible_animal_additives;
    }

    public Boolean getContains_unspecified_possibly_animal_additives() {
        return contains_unspecified_possibly_animal_additives;
    }

    public void setContains_unspecified_possibly_animal_additives(Boolean contains_unspecified_possibly_animal_additives) {
        this.contains_unspecified_possibly_animal_additives = contains_unspecified_possibly_animal_additives;
    }

    public Boolean getContains_insect_excretions() {
        return contains_insect_excretions;
    }

    public void setContains_insect_excretions(Boolean contains_insect_excretions) {
        this.contains_insect_excretions = contains_insect_excretions;
    }

    public Boolean getContains_eggs() {
        return contains_eggs;
    }

    public void setContains_eggs(Boolean contains_eggs) {
        this.contains_eggs = contains_eggs;
    }

    public Boolean getContains_animal_milk() {
        return contains_animal_milk;
    }

    public void setContains_animal_milk(Boolean contains_animal_milk) {
        this.contains_animal_milk = contains_animal_milk;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeSerializable(this.contains_bodyparts);
        dest.writeSerializable(this.contains_animal_additives);
        dest.writeSerializable(this.contains_possible_animal_additives);
        dest.writeSerializable(this.contains_unspecified_possibly_animal_additives);
        dest.writeSerializable(this.contains_insect_excretions);
        dest.writeSerializable(this.contains_eggs);
        dest.writeSerializable(this.contains_animal_milk);
    }
}
