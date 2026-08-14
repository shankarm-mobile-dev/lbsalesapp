package com.suguna.lbsalesperson.POJO;

public class SugOutStanding_POJO  {
    String NAME;
    String PRICE;
    String FOLLOWING;
    String CODE;
    String DRILLDOWN;
    String CREDIT_LIMIT;
    String OVERDUE_AFTERTOMORROW,OVERDUE_TOMORROW;

    public String getCREDIT_LIMIT() {
        return CREDIT_LIMIT;
    }

    public void setCREDIT_LIMIT(String CREDIT_LIMIT) {
        this.CREDIT_LIMIT = CREDIT_LIMIT;
    }

    public String getOVER_DUE() {
        return OVER_DUE;
    }

    public void setOVER_DUE(String OVER_DUE) {
        this.OVER_DUE = OVER_DUE;
    }

    String OVER_DUE;

    public String getNAME() {
        return NAME;
    }

    public void setNAME(String NAME) {
        this.NAME = NAME;
    }

    public String getPRICE() {
        return PRICE;
    }

    public void setPRICE(String PRICE) {
        this.PRICE = PRICE;
    }

    public String getFOLLOWING() {
        return FOLLOWING;
    }

    public void setFOLLOWING(String FOLLOWING) {
        this.FOLLOWING = FOLLOWING;
    }

    public String getCODE() {
        return CODE;
    }

    public void setCODE(String CODE) {
        this.CODE = CODE;
    }

    public String getDRILLDOWN() {
        return DRILLDOWN;
    }

    public void setDRILLDOWN(String DRILLDOWN) {
        this.DRILLDOWN = DRILLDOWN;
    }

    public String getOVERDUE_AFTERTOMORROW() {
        return OVERDUE_AFTERTOMORROW;
    }

    public void setOVERDUE_AFTERTOMORROW(String OVERDUE_AFTERTOMORROW) {
        this.OVERDUE_AFTERTOMORROW = OVERDUE_AFTERTOMORROW;
    }

    public String getOVERDUE_TOMORROW() {
        return OVERDUE_TOMORROW;
    }

    public void setOVERDUE_TOMORROW(String OVERDUE_TOMORROW) {
        this.OVERDUE_TOMORROW = OVERDUE_TOMORROW;
    }
}
