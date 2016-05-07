package com.mzlion.okhttpserver.entity;

import java.io.Serializable;

/**
 * Created by mzlion on 2016/1/9.
 */
public class CommonResponse<E> implements Serializable {

    private String resultCode;

    private String resultDesc;

    private E result;

    public CommonResponse() {
    }

    public CommonResponse(String resultCode, String resultDesc) {
        this.resultCode = resultCode;
        this.resultDesc = resultDesc;
    }

    public CommonResponse(E result, String resultCode, String resultDesc) {
        this.result = result;
        this.resultCode = resultCode;
        this.resultDesc = resultDesc;
    }

    public E getResult() {
        return result;
    }

    public void setResult(E result) {
        this.result = result;
    }

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public String getResultDesc() {
        return resultDesc;
    }

    public void setResultDesc(String resultDesc) {
        this.resultDesc = resultDesc;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("CommonResponse{");
        sb.append("result=").append(result);
        sb.append(", resultCode='").append(resultCode).append('\'');
        sb.append(", resultDesc='").append(resultDesc).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
