package com.suguna.lbsalesperson.priceallocation.model;

import java.util.ArrayList;

public class lbsavelist {

	String username;
	String imei;
	String userid;
	String trx_date;
	String itemcode;
	String uom;
	String pricetype;
	String reason;
	String sub_inventory_name;
	String location_code;
	String ledgerid;
	
	
	ArrayList<lbpricedet> result;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getImei() {
		return imei;
	}

	public void setImei(String imei) {
		this.imei = imei;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getTrx_date() {
		return trx_date;
	}

	public void setTrx_date(String trx_date) {
		this.trx_date = trx_date;
	}

	public String getItemcode() {
		return itemcode;
	}

	public void setItemcode(String itemcode) {
		this.itemcode = itemcode;
	}

	public String getUom() {
		return uom;
	}

	public void setUom(String uom) {
		this.uom = uom;
	}

	public ArrayList<lbpricedet> getResult() {
		return result;
	}

	public void setResult(ArrayList<lbpricedet> result) {
		this.result = result;
	}

	public String getPricetype() {
		return pricetype;
	}

	public void setPricetype(String pricetype) {
		this.pricetype = pricetype;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
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
	
	public String getLedgerid() {
		return ledgerid;
	}

	public void setLedgerid(String ledgerid) {
		this.ledgerid = ledgerid;
	}
}
