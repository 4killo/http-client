package com.test;


import java.util.ArrayList;
import java.util.List;


/**
 * FavoriteStoreRequest
 */
public class FavoriteStoreRequest {

    private String brandId = null;
    private String emailId = null;
    private List<String> stores = new ArrayList<>();


    /**
     * store brand
     **/
    public FavoriteStoreRequest brandId(String brandId) {
        this.brandId = brandId;
        return this;
    }


    public String getBrandId() {
        return brandId;
    }

    public void setBrandId(String brandId) {
        this.brandId = brandId;
    }


    /**
     * email Id
     **/
    public FavoriteStoreRequest emailId(String emailId) {
        this.emailId = emailId;
        return this;
    }


    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }


    /**
     * Set of Store Ids
     **/
    public FavoriteStoreRequest stores(List<String> stores) {
        this.stores = stores;
        return this;
    }


    public List<String> getStores() {
        return stores;
    }

    public void setStores(List<String> stores) {
        this.stores = stores;
    }


}

