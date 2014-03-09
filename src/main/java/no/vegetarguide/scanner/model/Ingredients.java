package no.vegetarguide.scanner.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Ingredients implements Parcelable {

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Ingredients createFromParcel(Parcel in) {
            return new Ingredients(in);
        }

        public Ingredients[] newArray(int size) {
            return new Ingredients[size];
        }
    };
    private Boolean contains_body_parts;
    private Boolean contains_animal_additives;
    private Boolean contains_possible_animal_additives;
    private Boolean contains_unspecified_possibly_animal_additives;
    private Boolean contains_insect_excretions;
    private Boolean contains_eggs;
    private Boolean contains_animal_milk;
    private String vegetarian_comment;

    private Boolean manufacturer_confirms_vegan;
    private Boolean manufacturer_confirms_vegetarian;

    private String confirmed_vegetarian_comment;
    private String confirmed_vegan_comment;

    public Ingredients() {
    }

    public Ingredients(Parcel in) {
        this.contains_body_parts = (Boolean) in.readSerializable();
        this.contains_animal_additives = (Boolean) in.readSerializable();
        this.contains_possible_animal_additives = (Boolean) in.readSerializable();
        this.contains_unspecified_possibly_animal_additives = (Boolean) in.readSerializable();
        this.contains_insect_excretions = (Boolean) in.readSerializable();
        this.contains_eggs = (Boolean) in.readSerializable();
        this.contains_animal_milk = (Boolean) in.readSerializable();
        this.vegetarian_comment = in.readString();
        this.manufacturer_confirms_vegan = (Boolean) in.readSerializable();
        this.manufacturer_confirms_vegetarian = (Boolean) in.readSerializable();
        this.confirmed_vegetarian_comment = in.readString();
        this.confirmed_vegan_comment = in.readString();
    }


    public boolean isVegan() {
        final boolean isCandidate = isMaybeVegan();
        final boolean isConfirmed = Boolean.TRUE.equals(manufacturer_confirms_vegan);

        return ((isCandidate && isConfirmed)
                || (isCandidate && Boolean.FALSE.equals(contains_possible_animal_additives)
                && Boolean.FALSE.equals(contains_unspecified_possibly_animal_additives)));
    }


    public boolean isMaybeVegan() {
        return isMaybeVegetarian()
                && Boolean.FALSE.equals(this.contains_insect_excretions)
                && Boolean.FALSE.equals(this.contains_eggs)
                && Boolean.FALSE.equals(this.contains_animal_milk);
    }

    public boolean isAnimalDerivedForCertain() {
        return Boolean.TRUE.equals(this.contains_body_parts)
                || Boolean.TRUE.equals(this.contains_animal_additives);
    }

    public boolean isMaybeVegetarian() {
        return !isAnimalDerivedForCertain();
    }

    public boolean isVegetarian() {
        return isMaybeVegetarian() && unspecifiedAdditivesAreVegetarian();
    }

    private boolean unspecifiedAdditivesAreVegetarian() {
        return Boolean.FALSE.equals(contains_unspecified_possibly_animal_additives)
                || Boolean.TRUE.equals(manufacturer_confirms_vegetarian);
    }

    public Boolean getContains_body_parts() {
        return contains_body_parts;
    }

    public void setContains_body_parts(Boolean contains_body_parts) {
        this.contains_body_parts = contains_body_parts;
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

    public String getVegetarian_comment() {
        return vegetarian_comment;
    }

    public void setVegetarian_comment(String vegetarian_comment) {
        this.vegetarian_comment = vegetarian_comment;
    }

    public Boolean getManufacturer_confirms_vegan() {
        return manufacturer_confirms_vegan;
    }

    public void setManufacturer_confirms_vegan(Boolean manufacturer_confirms_vegan) {
        this.manufacturer_confirms_vegan = manufacturer_confirms_vegan;
    }

    public Boolean getManufacturer_confirms_vegetarian() {
        return manufacturer_confirms_vegetarian;
    }

    public void setManufacturer_confirms_vegetarian(Boolean manufacturer_confirms_vegetarian) {
        this.manufacturer_confirms_vegetarian = manufacturer_confirms_vegetarian;
    }

    public String getConfirmed_vegetarian_comment() {
        return confirmed_vegetarian_comment;
    }

    public void setConfirmed_vegetarian_comment(String confirmed_vegetarian_comment) {
        this.confirmed_vegetarian_comment = confirmed_vegetarian_comment;
    }

    public String getConfirmed_vegan_comment() {
        return confirmed_vegan_comment;
    }

    public void setConfirmed_vegan_comment(String confirmed_vegan_comment) {
        this.confirmed_vegan_comment = confirmed_vegan_comment;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeSerializable(this.contains_body_parts);
        dest.writeSerializable(this.contains_animal_additives);
        dest.writeSerializable(this.contains_possible_animal_additives);
        dest.writeSerializable(this.contains_unspecified_possibly_animal_additives);
        dest.writeSerializable(this.contains_insect_excretions);
        dest.writeSerializable(this.contains_eggs);
        dest.writeSerializable(this.contains_animal_milk);
        dest.writeString(this.vegetarian_comment);
        dest.writeSerializable(this.manufacturer_confirms_vegan);
        dest.writeSerializable(this.manufacturer_confirms_vegetarian);
        dest.writeString(this.confirmed_vegetarian_comment);
        dest.writeString(this.confirmed_vegan_comment);
    }
}
