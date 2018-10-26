package com.exceptions;

public class SARValidationException extends Exception{

    private Integer code;
    private String status;
    private String msg;

    public SARValidationException(ValidationMessage vm){
        this.code = vm.getCode();
        this.status = vm.getStatus();
        this.msg = vm.getMsg();
    }

    public SARValidationException(ValidationMessage vm, String customMsg){
        this.code = vm.getCode();
        this.status = vm.getStatus();
        this.msg = customMsg;
    }

    @Override
    public String getMessage() {
        return msg;
    }
}
