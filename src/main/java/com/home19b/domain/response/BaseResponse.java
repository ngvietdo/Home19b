package com.home19b.domain.response;

import com.home19b.common.ErrorCodeDefs;

public class BaseResponse {
    protected int rc;
    protected String rd;

    public BaseResponse(int rc, String rd) {
        this.rc = rc;
        this.rd = rd;
    }

    public BaseResponse() {
        rc = ErrorCodeDefs.UNKNOWN;
        rd = "UNKNOWN";
    }

    public int getRc() {
        return rc;
    }

    public void setRc(int rc) {
        this.rc = rc;
    }

    public String getRd() {
        return rd;
    }

    public void setRd(String rd) {
        this.rd = rd;
    }

    public void setSuccess() {
        this.rc = ErrorCodeDefs.ERR_CODE_OK;
        this.rd = "OK";
    }

    public void setFailed(String msg) {
        this.rc = ErrorCodeDefs.ERR_CODE_FAILED;
        this.rd = msg;
    }

    public void setFailed(int code, String msg) {
        this.rc = code;
        this.rd = msg;
    }

    public void setFailed(int code) {
        this.rc = code;
        this.rd = ErrorCodeDefs.getErrDesc(code);
    }

    public void setFailed() {
        this.setFailed("Action failed !");
    }

    public void setParamsInvalid() {
        this.setFailed(ErrorCodeDefs.ERR_CODE_PARAMS_INVALID, "Params invalid!");
    }

    public void setServerError() {
        this.setFailed(ErrorCodeDefs.ERR_CODE_SERVER_ERROR, "Server error!");
    }

    public void setItemNotFound(String msg) {
        this.rc = ErrorCodeDefs.ERR_CODE_ITEM_NOT_FOUND;
        this.rd = msg;
    }

    public void setItemNotFound() {
        this.setItemNotFound("Không tìm thấy bản ghi nào");
    }

    @Override
    public String toString() {
        return "{" +
                "rc=" + rc +
                ", rd='" + rd + '\'' +
                '}';
    }

    public BaseResponse(Builder builder) {
        rc = builder.rc;
        rd = builder.rd;
    }

    public static class Builder {
        private int rc;
        private String rd;

        public static Builder newInstance() {
            return new Builder();
        }

        public Builder setSuccess(){
            this.rc = ErrorCodeDefs.ERR_CODE_OK;
            this.rd = "OK";
            return this;
        }

        public BaseResponse build(){
            return new BaseResponse(this);
        }
    }
}
