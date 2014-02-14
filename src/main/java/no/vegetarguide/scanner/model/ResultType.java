package no.vegetarguide.scanner.model;

import no.vegetarguide.scanner.R;

public enum ResultType {
    UNKNOWN_PRODUCT(R.string.resulttype_unknown_product),
    UNKNOWN_STATUS(R.string.resulttype_unknown_status),
    KNOWN_STATUS(R.string.resulttype_known);

    private final int descriptionResource;

    private ResultType(int stringResource) {
        this.descriptionResource = stringResource;
    }

    public int getDescriptionResource() {
        return descriptionResource;
    }
}
