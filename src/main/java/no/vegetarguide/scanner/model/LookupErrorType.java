package no.vegetarguide.scanner.model;

import no.vegetarguide.scanner.R;

public enum LookupErrorType {
    RESTRICTED_DISTRIBUTION(R.string.error_restricted_distribution),
    PAPER_PRODUCT(R.string.error_paper_product),
    INVALID_PRODUCT_CODE(R.string.error_invalid_product_code),
    UNKNOWN_ERROR(R.string.error_unknown_lookup_error);

    private final int descriptionResource;

    private LookupErrorType(int stringResource) {
        this.descriptionResource = stringResource;
    }

    public int getDescriptionResource() {
        return descriptionResource;
    }
}
