package no.vegetarguide.scanner.integration;

import com.google.gson.annotations.Expose;

import java.net.URL;

import no.vegetarguide.scanner.model.AdditivesDetails;
import no.vegetarguide.scanner.model.ProductLookupResponse;

import static org.apache.commons.lang3.StringUtils.trimToNull;

public class ModifyProductRequest {

    @Expose
    private String user_commentary;
    @Expose
    private AdditivesDetails additivesDetails;
    @Expose
    private URL imageurl;
    @Expose
    private String category;
    @Expose
    private String subcategory;
    @Expose
    private String otherwise_animal_derived_detail;
    @Expose
    private Boolean otherwise_animal_derived;
    @Expose
    private Boolean contains_animal_additives;
    @Expose
    private Boolean contains_animal_milk;
    @Expose
    private Boolean contains_bodyparts;
    @Expose
    private Boolean contains_eggs;
    @Expose
    private Boolean contains_honey;
    @Expose
    private Boolean animal_tested;
    @Expose
    private String brand;
    @Expose
    private String title;
    @Expose
    private String subtitle;
    @Expose
    private String objectId;
    @Expose
    private String gtin;

    @SuppressWarnings("unused")
    public ModifyProductRequest() {
        //used by Gson
    }

    public ModifyProductRequest(String title, String subtitle, String brand, String otherwise_animal_derived_detail,
                                String user_commentary, Boolean otherwise_animal_derived,
                                Boolean contains_animal_additives,
                                Boolean contains_animal_milk,
                                Boolean contains_bodyparts,
                                Boolean contains_eggs,
                                Boolean contains_honey,
                                Boolean animal_tested,
                                AdditivesDetails additivesDetails, ProductLookupResponse productDetails) {

        // Copy some fields that are not (yet) editable
        this.objectId = productDetails.get_id();
        this.gtin = productDetails.getGtin();
        this.imageurl = productDetails.getImageurl();
        this.category = productDetails.getCategory();
        this.subcategory = productDetails.getSubcategory();

        String trimmedTitle = trimToNull(title);
        this.title = trimmedTitle == null ? productDetails.getTitle() : trimmedTitle; // keep previous value if missing

        String trimmedSubtitle = trimToNull(subtitle);
        this.subtitle = trimmedSubtitle == null ? productDetails.getSubtitle() : trimmedSubtitle; // keep previous value if missing

        String trimmedBrand = trimToNull(brand);
        this.brand = trimmedBrand == null ? productDetails.getBrand() : trimmedBrand; // keep previous value if missing

        this.otherwise_animal_derived = otherwise_animal_derived == null ? productDetails.getOtherwise_animal_derived() : otherwise_animal_derived;
        this.otherwise_animal_derived_detail = trimToNull(otherwise_animal_derived_detail); //overwrite previous value
        this.user_commentary = trimToNull(user_commentary); //overwrite previous value

        this.contains_animal_additives = contains_animal_additives == null ? productDetails.getContains_animal_additives() : contains_animal_additives;
        this.contains_animal_milk = contains_animal_milk == null ? productDetails.getContains_animal_milk() : contains_animal_milk;
        this.contains_bodyparts = contains_bodyparts == null ? productDetails.getContains_bodyparts() : contains_bodyparts;
        this.contains_eggs = contains_eggs == null ? productDetails.getContains_eggs() : contains_eggs;
        this.contains_honey = contains_honey == null ? productDetails.getContains_honey() : contains_honey;
        this.animal_tested = animal_tested == null ? productDetails.getAnimal_tested() : animal_tested;

        this.additivesDetails = additivesDetails;
    }

    public URL getImageurl() {
        return imageurl;
    }

    public String getCategory() {
        return category;
    }

    public String getSubcategory() {
        return subcategory;
    }

    public String getOtherwise_animal_derived_detail() {
        return otherwise_animal_derived_detail;
    }

    public Boolean getOtherwise_animal_derived() {
        return otherwise_animal_derived;
    }

    public Boolean getContains_animal_additives() {
        return contains_animal_additives;
    }

    public Boolean getContains_animal_milk() {
        return contains_animal_milk;
    }

    public Boolean getContains_bodyparts() {
        return contains_bodyparts;
    }

    public Boolean getContains_eggs() {
        return contains_eggs;
    }

    public Boolean getContains_honey() {
        return contains_honey;
    }

    public Boolean getAnimal_tested() {
        return animal_tested;
    }

    public String getBrand() {
        return brand;
    }

    public String getTitle() {
        return title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public String getObjectId() {
        return objectId;
    }

    public String getGtin() {
        return gtin;
    }

    public String getUser_commentary() {
        return user_commentary;
    }

    public AdditivesDetails getAdditivesDetails() {
        return additivesDetails;
    }
}
