package com.suguna.lbsalesperson.priceallocation.model;

public class lbitem {

	String CURRENT_DATE;
	String ITEM_CODE;
	String ITEM_NAME;
	String UOM;
	String sub_inventory_name;
	String location_code;

	String TARGET;

	public String getCURRENT_DATE() {
		return CURRENT_DATE;
	}

	public void setCURRENT_DATE(String cURRENT_DATE) {
		CURRENT_DATE = cURRENT_DATE;
	}

	public String getITEM_CODE() {
		return ITEM_CODE;
	}

	public void setITEM_CODE(String iTEM_CODE) {
		ITEM_CODE = iTEM_CODE;
	}

	public String getITEM_NAME() {
		return ITEM_NAME;
	}

	public void setITEM_NAME(String iTEM_NAME) {
		ITEM_NAME = iTEM_NAME;
	}

	public String getUOM() {
		return UOM;
	}

	public void setUOM(String uOM) {
		UOM = uOM;
	}

	public String getSub_inventory_name() {
		return sub_inventory_name;
	}

	public void setSub_inventory_name(String sub_inventory_name) {
		this.sub_inventory_name = sub_inventory_name;
	}

	public String getLocation_code() {
		return location_code;
	}

	public void setLocation_code(String location_code) {
		this.location_code = location_code;
	}

	public String getTARGET() {
		return TARGET;
	}

	public void setTARGET(String tARGET) {
		TARGET = tARGET;
	}
}