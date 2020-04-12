package com.home19b.domain.response;

import java.util.ArrayList;
import java.util.List;

public class GetArrayResponse<T> extends BaseResponse {
    private long total;
    private List<T> rows;
    private List<String> autoCompleteSugesstions;

    public GetArrayResponse() {
        super();
        this.total = 0;
        this.rows = new ArrayList<>();
    }

    public GetArrayResponse(long total, List<T> rows, String message, int code) {
        super(code, message);
        this.total = total;
        this.rows = rows;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public List<T> getRows() {
        return rows;
    }

    public void setRows(List<T> rows) {
        this.rows = rows;
    }

    public void setSuccess(List<T> rows, long total) {
        super.setSuccess();
        this.rows = rows;
        this.total = total;
    }

    public void setResult(List<T> rows, long total) {
        setResult(rows, total, "Không tìm thấy bản ghi nào");
    }

    public void setResult(List<T> rows, long total, String msgNotfound) {
        this.total = total;
        if (rows != null && !rows.isEmpty()) {
            super.setSuccess();
            this.rows = rows;
        } else {
            super.setItemNotFound(msgNotfound);
        }
    }


    public List<String> getAutoCompleteSugesstions() {
        return autoCompleteSugesstions;
    }

    public void setAutoCompleteSugesstions(List<String> autoCompleteSugesstions) {
        this.autoCompleteSugesstions = autoCompleteSugesstions;
    }

    public String baseInfo() {
        return "{"
                + "rc=" + rc
                + ", rd='" + rd + '\''
                + ", total=" + total
                + '}';
    }

    @Override
    public String toString() {
        return "GetArrayResponse{" +
                "total=" + total +
                ", rows=" + rows +
                ", rc=" + rc +
                ", rd='" + rd + '\'' +
                '}';
    }
}
