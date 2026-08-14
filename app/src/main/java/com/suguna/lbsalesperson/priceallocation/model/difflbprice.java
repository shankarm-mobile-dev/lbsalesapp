package com.suguna.lbsalesperson.priceallocation.model;

import java.util.List;

public class difflbprice {

    String Status;
    String StatusCode;
    String Message;
    String Token;
    List<difflblist> RESULT;

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

    public List<difflblist> getRESULT() {
        return RESULT;
    }
    public void setRESULT(List<difflblist> RESULT) {
        this.RESULT = RESULT;
    }



}