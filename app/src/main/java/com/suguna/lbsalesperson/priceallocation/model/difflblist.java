package com.suguna.lbsalesperson.priceallocation.model;

public class difflblist {
    String ITEM_CODE;
    String PRODUCT_CATEGORY;
    String SEQ_NO;
    String LOW_WT;
    String HIGH_WT;
    String STATUS;
    String RATE;
    String PROD_NAME;
    String PROV_PRICE;
    String PROV_STATUS;
    String ENTRY_PROV_STATUS;
    String PREVIOUS_PRICE;

    String BD_GOVT_RATE;
    String COBB_WT;
    Boolean CHECK=false;


    public String getSTATUS() {
        return STATUS;
    }

    public void setSTATUS(String STATUS) {
        this.STATUS = STATUS;
    }

    public String getRATE() {
        return RATE;
    }

    public void setRATE(String RATE) {
        this.RATE = RATE;
    }

    public String getITEM_CODE() {
        return ITEM_CODE;
    }

    public void setITEM_CODE(String ITEM_CODE) {
        this.ITEM_CODE = ITEM_CODE;
    }

    public String getPRODUCT_CATEGORY() {
        return PRODUCT_CATEGORY;
    }

    public void setPRODUCT_CATEGORY(String PRODUCT_CATEGORY) {
        this.PRODUCT_CATEGORY = PRODUCT_CATEGORY;
    }

    public String getSEQ_NO() {
        return SEQ_NO;
    }

    public void setSEQ_NO(String SEQ_NO) {
        this.SEQ_NO = SEQ_NO;
    }

    public String getLOW_WT() {
        return LOW_WT;
    }

    public void setLOW_WT(String LOW_WT) {
        this.LOW_WT = LOW_WT;
    }

    public String getHIGH_WT() {
        return HIGH_WT;
    }

    public void setHIGH_WT(String HIGH_WT) {
        this.HIGH_WT = HIGH_WT;
    }

    public String getPROD_NAME() {
        return PROD_NAME;
    }

    public void setPROD_NAME(String PROD_NAME) {
        this.PROD_NAME = PROD_NAME;
    }

    public String getPROV_PRICE() {
        return PROV_PRICE;
    }

    public void setPROV_PRICE(String PROV_PRICE) {
        this.PROV_PRICE = PROV_PRICE;
    }

    public String getPROV_STATUS() {
        return PROV_STATUS;
    }

    public void setPROV_STATUS(String PROV_STATUS) {
        this.PROV_STATUS = PROV_STATUS;
    }

    public String getENTRY_PROV_STATUS() {
        return ENTRY_PROV_STATUS;
    }

    public void setENTRY_PROV_STATUS(String ENTRY_PROV_STATUS) {
        this.ENTRY_PROV_STATUS = ENTRY_PROV_STATUS;
    }

    public Boolean getCHECK() {
        return CHECK;
    }

    public void setCHECK(Boolean CHECK) {
        this.CHECK = CHECK;
    }

    public String getPREVIOUS_PRICE() {
        return PREVIOUS_PRICE;
    }

    public void setPREVIOUS_PRICE(String PREVIOUS_PRICE) {
        this.PREVIOUS_PRICE = PREVIOUS_PRICE;
    }

    public String getCOBB_WT() {
        return COBB_WT;
    }

    public void setCOBB_WT(String COBB_WT) {
        this.COBB_WT = COBB_WT;
    }
}
