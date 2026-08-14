package com.suguna.lbsalesperson.priceallocation.model;

import java.util.List;

public class lbitemlist {
	
	String Status;
    String StatusCode;
    String Message;
    List<lbitem> result;
    
	public String getStatus() {
		return Status;
	}
	public void setStatus(String status) {
		Status = status;
	}
	public String getStatusCode() {
		return StatusCode;
	}
	public void setStatusCode(String statusCode) {
		StatusCode = statusCode;
	}
	public String getMessage() {
		return Message;
	}
	public void setMessage(String message) {
		Message = message;
	}
	public List<lbitem> getResult() {
		return result;
	}
	public void setResult(List<lbitem> result) {
		this.result = result;
	}
    
    

}
