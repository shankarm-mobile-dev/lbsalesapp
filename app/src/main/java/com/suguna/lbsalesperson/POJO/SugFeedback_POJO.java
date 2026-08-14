package com.suguna.lbsalesperson.POJO;

public class SugFeedback_POJO {
    String FEEDBACK_ID;
    String FEEDBACK_DATE;
    String FEEDBACK_TYPE;
    String FEEDBACK_DESC;
    String RESPONSE_STATUS;

    public String getCUSTOMER_NAME() {
        return CUSTOMER_NAME;
    }

    public void setCUSTOMER_NAME(String CUSTOMER_NAME) {
        this.CUSTOMER_NAME = CUSTOMER_NAME;
    }

    String CUSTOMER_NAME;

    public String getFEEDBACK_ID() {
        return FEEDBACK_ID;
    }

    public void setFEEDBACK_ID(String FEEDBACK_ID) {
        this.FEEDBACK_ID = FEEDBACK_ID;
    }

    public String getFEEDBACK_DATE() {
        return FEEDBACK_DATE;
    }

    public void setFEEDBACK_DATE(String FEEDBACK_DATE) {
        this.FEEDBACK_DATE = FEEDBACK_DATE;
    }

    public String getFEEDBACK_TYPE() {
        return FEEDBACK_TYPE;
    }

    public void setFEEDBACK_TYPE(String FEEDBACK_TYPE) {
        this.FEEDBACK_TYPE = FEEDBACK_TYPE;
    }

    public String getFEEDBACK_DESC() {
        return FEEDBACK_DESC;
    }

    public void setFEEDBACK_DESC(String FEEDBACK_DESC) {
        this.FEEDBACK_DESC = FEEDBACK_DESC;
    }

    public String getRESPONSE_STATUS() {
        return RESPONSE_STATUS;
    }

    public void setRESPONSE_STATUS(String RESPONSE_STATUS) {
        this.RESPONSE_STATUS = RESPONSE_STATUS;
    }
}
