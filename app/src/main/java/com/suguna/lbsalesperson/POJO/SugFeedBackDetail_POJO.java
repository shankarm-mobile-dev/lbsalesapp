package com.suguna.lbsalesperson.POJO;

import com.suguna.lbsalesperson.feedback.model.FeedbackResponseModel;

import java.util.ArrayList;

public class SugFeedBackDetail_POJO {
    String FEEDBACK_ID;
    String FEEDBACK_DATE;
    String FEEDBACK_TYPE;
    String FEEDBACK_DESC;
    String REFERENCE_TYPE;
    String FROM_USER;
    String REFERENCE_NO;
    String TO_USER;
    String REFERENCE_STATUS;
    String FINAL_COMMENTS;
    String FINAL_DATE;
    String ALLOW_UPDATE;
    String RATING;

    public String getLIFTING_DATE() {
        return LIFTING_DATE;
    }

    public void setLIFTING_DATE(String LIFTING_DATE) {
        this.LIFTING_DATE = LIFTING_DATE;
    }

    public String getCUSTOMER_NAME() {
        return CUSTOMER_NAME;
    }

    public void setCUSTOMER_NAME(String CUSTOMER_NAME) {
        this.CUSTOMER_NAME = CUSTOMER_NAME;
    }

    public String getLOCATION_NAME() {
        return LOCATION_NAME;
    }

    public void setLOCATION_NAME(String LOCATION_NAME) {
        this.LOCATION_NAME = LOCATION_NAME;
    }

    String LIFTING_DATE;
    String CUSTOMER_NAME;
    String LOCATION_NAME;

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

    public String getREFERENCE_TYPE() {
        return REFERENCE_TYPE;
    }

    public void setREFERENCE_TYPE(String REFERENCE_TYPE) {
        this.REFERENCE_TYPE = REFERENCE_TYPE;
    }

    public String getFROM_USER() {
        return FROM_USER;
    }

    public void setFROM_USER(String FROM_USER) {
        this.FROM_USER = FROM_USER;
    }

    public String getREFERENCE_NO() {
        return REFERENCE_NO;
    }

    public void setREFERENCE_NO(String REFERENCE_NO) {
        this.REFERENCE_NO = REFERENCE_NO;
    }

    public String getTO_USER() {
        return TO_USER;
    }

    public void setTO_USER(String TO_USER) {
        this.TO_USER = TO_USER;
    }

    public String getREFERENCE_STATUS() {
        return REFERENCE_STATUS;
    }

    public void setREFERENCE_STATUS(String REFERENCE_STATUS) {
        this.REFERENCE_STATUS = REFERENCE_STATUS;
    }

    public String getFINAL_COMMENTS() {
        return FINAL_COMMENTS;
    }

    public void setFINAL_COMMENTS(String FINAL_COMMENTS) {
        this.FINAL_COMMENTS = FINAL_COMMENTS;
    }

    public String getFINAL_DATE() {
        return FINAL_DATE;
    }

    public void setFINAL_DATE(String FINAL_DATE) {
        this.FINAL_DATE = FINAL_DATE;
    }

    public String getALLOW_UPDATE() {
        return ALLOW_UPDATE;
    }

    public void setALLOW_UPDATE(String ALLOW_UPDATE) {
        this.ALLOW_UPDATE = ALLOW_UPDATE;
    }

    public String getRATING() {
        return RATING;
    }

    public void setRATING(String RATING) {
        this.RATING = RATING;
    }

    public ArrayList<FeedbackResponseModel> getmResponseArray() {
        return mResponseArray;
    }

    public void setmResponseArray(ArrayList<FeedbackResponseModel> mResponseArray) {
        this.mResponseArray = mResponseArray;
    }

    public ArrayList<FeedbackResponseModel> mResponseArray = new ArrayList<FeedbackResponseModel>();
}
