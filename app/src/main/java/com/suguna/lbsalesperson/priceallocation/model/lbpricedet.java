package com.suguna.lbsalesperson.priceallocation.model;

import java.util.List;

public class lbpricedet {

	String BRANCH_CODE;
	String BRANCH_NAME;
	String STATUS;
	String RATE;
	String CONCESSIONRATE;
	String CONCESSIONSTATUS;
	String SUB_INVENTORY_NAME;
	String LOCATION_CODE;
	String OPM_DIVISION;
	int ID;
	String ZONE_NAME;
	String REGIONCODE;
	int SORTING_ORDER;
	String COBB_PRICE;
	String LOCAL_PRICE;
	String PROV_PRICE;
	String PROV_STATUS;
	String ENTRY_PROV_STATUS;
	Boolean CHECK=false;
	String PREVIOUS_PRICE;
	String BD_GOVT_RATE;
	String COBB_WT;
	List<difflblist> DIFFPRICE;

	
	public String getBRANCH_CODE() {
		return BRANCH_CODE;
	}

	public void setBRANCH_CODE(String BRANCH_CODE) {
		this.BRANCH_CODE = BRANCH_CODE;
	}

	public String getBRANCH_NAME() {
		return BRANCH_NAME;
	}

	public void setBRANCH_NAME(String BRANCH_NAME) {
		this.BRANCH_NAME = BRANCH_NAME;
	}

	public String getZONE_NAME() {
		return ZONE_NAME;
	}

	public void setZONE_NAME(String zONE_NAME) {
		ZONE_NAME = zONE_NAME;
	}

	public String getRATE() {
		return RATE;
	}

	public void setRATE(String RATE) {
		this.RATE = RATE;
	}

	public String getSTATUS() {
		return STATUS;
	}

	public void setSTATUS(String sTATUS) {
		STATUS = sTATUS;
	}

	public String getCONCESSIONRATE() {
		return CONCESSIONRATE;
	}

	public void setCONCESSIONRATE(String cONCESSIONRATE) {
		CONCESSIONRATE = cONCESSIONRATE;
	}

	public String getCONCESSIONSTATUS() {
		return CONCESSIONSTATUS;
	}

	public void setCONCESSIONSTATUS(String cONCESSIONSTATUS) {
		CONCESSIONSTATUS = cONCESSIONSTATUS;
	}

	public String getSUB_INVENTORY_NAME() {
		return SUB_INVENTORY_NAME;
	}

	public void setSUB_INVENTORY_NAME(String sUB_INVENTORY_NAME) {
		SUB_INVENTORY_NAME = sUB_INVENTORY_NAME;
	}

	public String getLOCATION_CODE() {
		return LOCATION_CODE;
	}

	public void setLOCATION_CODE(String lOCATION_CODE) {
		LOCATION_CODE = lOCATION_CODE;
	}

	public String getOPM_DIVISION() {
		return OPM_DIVISION;
	}

	public void setOPM_DIVISION(String oPM_DIVISION) {
		OPM_DIVISION = oPM_DIVISION;
	}

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public String getREGIONCODE() {
		return REGIONCODE;
	}

	public void setREGIONCODE(String rEGIONCODE) {
		REGIONCODE = rEGIONCODE;
	}

	public int getSORTING_ORDER() {
		return SORTING_ORDER;
	}

	public void setSORTING_ORDER(int sORTING_ORDER) {
		SORTING_ORDER = sORTING_ORDER;
	}

	public String getCOBB_PRICE() {
		return COBB_PRICE;
	}

	public void setCOBB_PRICE(String COBB_PRICE) {
		this.COBB_PRICE = COBB_PRICE;
	}

	public String getLOCAL_PRICE() {
		return LOCAL_PRICE;
	}

	public void setLOCAL_PRICE(String LOCAL_PRICE) {
		this.LOCAL_PRICE = LOCAL_PRICE;
	}

	public List<difflblist> getDIFFPRICE() {
		return DIFFPRICE;
	}
	public void setDIFFPRICE(List<difflblist> DIFFPRICE) {
		this.DIFFPRICE = DIFFPRICE;
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

	public String getBD_GOVT_RATE() {
		return BD_GOVT_RATE;
	}

	public void setBD_GOVT_RATE(String BD_GOVT_RATE) {
		this.BD_GOVT_RATE = BD_GOVT_RATE;
	}

	public String getCOBB_WT() {
		return COBB_WT;
	}

	public void setCOBB_WT(String COBB_WT) {
		this.COBB_WT = COBB_WT;
	}
}
