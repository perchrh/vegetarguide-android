package no.vegetarguide.scanner.integration;

import com.google.gson.annotations.Expose;

import java.net.URL;

import no.vegetarguide.scanner.model.AdditivesDetails;
import no.vegetarguide.scanner.model.Product;
import no.vegetarguide.scanner.model.ProductLookupResponse;

import static org.apache.commons.lang3.StringUtils.trimToNull;

public class ModifyProductRequest {

    @Expose
    private Product product;

    @SuppressWarnings("unused")
    public ModifyProductRequest() {
        //used by Gson
    }

    public ModifyProductRequest(Product product) {
        this.product = product;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

}
