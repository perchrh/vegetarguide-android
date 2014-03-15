package no.vegetarguide.scanner.integration;

import com.google.gson.annotations.Expose;

import no.vegetarguide.scanner.model.Product;

public class ModifyProductRequest {

    @Expose
    private Product product;

    @Expose
    private String objectId;

    @SuppressWarnings("unused")
    public ModifyProductRequest() {
        //used by Gson
    }

    public ModifyProductRequest(Product product) {
        this.product = product;
        this.objectId = product.get_id();
    }

    public Product getProduct() {
        return product;
    }

    public String getObjectId() {
        return objectId;
    }

}
