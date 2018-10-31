package com.exceptions;

public enum ValidationMessage {

    INVALID_VALUE(1422, "invalid_value", "invalid value provided");


    private final String status;
    private final String msg;

    private final Integer code;

    ValidationMessage(Integer code, String status, String msg){
        this.code = code;
        this.status = status;
        this.msg = msg;
    }

    public Integer getCode(){
        return code;
    }

    public String getStatus(){
        return status;
    }

    public String getMsg(){
        return msg;
    }

}
