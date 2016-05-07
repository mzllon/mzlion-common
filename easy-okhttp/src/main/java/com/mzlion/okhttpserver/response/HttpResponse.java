package com.mzlion.okhttpserver.response;

import okhttp3.Response;

/**
 * Created by mzlion on 2016/4/17.
 */
public class HttpResponse<E> {

    private Response rawResponse;

    private E entity;

    private boolean isSuccess;

    private String errorMessage;

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public Response getRawResponse() {
        return rawResponse;
    }

    public void setRawResponse(Response rawResponse) {
        this.rawResponse = rawResponse;
    }

    public E getEntity() {
        return entity;
    }

    public void setEntity(E entity) {
        this.entity = entity;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("HttpResponse{");
        sb.append("rawResponse=").append(rawResponse);
        sb.append(", entity=").append(entity);
        sb.append('}');
        return sb.toString();
    }
}
