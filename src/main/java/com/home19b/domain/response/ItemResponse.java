package com.home19b.domain.response;

import com.home19b.common.ErrorCodeDefs;

public class ItemResponse<T> extends BaseResponse {
    protected T item;

    public T getItem() {
        return item;
    }

    public void setItem(T item) {
        this.item = item;
    }
    public ItemResponse(Builder<T> builder) {
        rc = builder.rc;
        rd = builder.rd;
        item = builder.item;
    }

    public static class Builder<T> {
        private int rc;
        private String rd;
        private T item;

        public static Builder newInstance() {
            return new Builder();
        }

        public Builder setSuccess(T item){
            this.rc = ErrorCodeDefs.ERR_CODE_OK;
            this.rd = "OK";
            this.item = item;
            return this;
        }

        public ItemResponse<T> build(){
            return new ItemResponse(this);
        }
    }
}
