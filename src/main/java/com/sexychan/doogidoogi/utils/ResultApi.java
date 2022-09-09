package com.sexychan.doogidoogi.utils;

/**
 * @Author: SexyChan
 * @description:
 * @date: 2022-09-08 21:04
 */
public class ResultApi {
    private Integer code;
    private String message;

    public ResultApi(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
