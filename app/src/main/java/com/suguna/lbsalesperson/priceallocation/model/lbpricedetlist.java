package com.suguna.lbsalesperson.priceallocation.model;

import java.util.List;

public class lbpricedetlist {
	 	String Status;
	    String StatusCode;
	    String Message;
	    String Token;
	    List<lbpricedet> result;

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

	    public String getToken() {
	        return Token;
	    }

	    public void setToken(String token) {
	        Token = token;
	    }

	    public List<lbpricedet> getResult() {
	        return result;
	    }

	    public void setResult(List<lbpricedet> result) {
	        this.result = result;
	    }
}
