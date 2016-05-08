package com.mzlion.okhttp.vo;

/**
 * <p>
 * 2016-05-08 The base response class.
 * </p>
 *
 * @author mzlion
 */
public class CommonResponse<E> {

    private String resultCode;

    private String resultDesc;

    private E result;

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

    public E getResult() {
        return result;
    }

    public void setResult(E result) {
        this.result = result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("CommonResponse{");
        sb.append("resultCode='").append(resultCode).append('\'');
        sb.append(", resultDesc='").append(resultDesc).append('\'');
        sb.append(", result=").append(result);
        sb.append('}');
        return sb.toString();
    }
}
