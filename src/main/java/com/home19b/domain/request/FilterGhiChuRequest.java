package com.home19b.domain.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.base.Strings;
import lombok.Data;

@Data
public class FilterGhiChuRequest {
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "Asia/Saigon")
    private String tuNgay;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "Asia/Saigon")
    private String denNgay;
    private String sdt;
    private Integer start = 0;
    private Integer limit = 20;

    @JsonIgnore
    public boolean isInvalid() {
        return Strings.isNullOrEmpty(sdt) || Strings.isNullOrEmpty(tuNgay) || Strings.isNullOrEmpty(denNgay);
    }
}
