package no.vegetarguide.scanner.model;

import no.vegetarguide.scanner.R;

public enum StatusType {
    ANIMAL_DERIVED(R.color.holo_red_light, R.string.statustype_animal_derived),
    VEGAN(R.color.holo_green_light, R.string.statustype_vegan),
    VEGETARIAN(R.color.holo_orange_light, R.string.statustype_vegetarian),
    MAYBE_VEGAN(R.color.holo_orange_light, R.string.statustype_maybe_vegan),
    MAYBE_VEGETARIAN(R.color.holo_purple, R.string.statustype_maybe_vegetarian);

    private final int colorResource;
    private final int descriptionResource;

    private StatusType(int colorResource, int stringResource) {
        this.colorResource = colorResource;
        this.descriptionResource = stringResource;
    }

    public int getColorResource() {
        return colorResource;
    }

    public int getDescriptionResource() {
        return descriptionResource;
    }
}
