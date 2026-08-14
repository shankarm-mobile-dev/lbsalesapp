package com.suguna.lbsalesperson.priceallocation.model;

import java.util.List;

public class lbpricemaxlist {
    String Status;
    String StatusCode;
    String Message;
    List<lbpricemaxitem> result;

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
    public List<lbpricemaxitem> getResult() {
        return result;
    }
    public void setResult(List<lbpricemaxitem> result) {
        this.result = result;
    }
}
