package com.home19b.domain.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.base.Strings;
import lombok.Data;

@Data
public class CheckInOutRequest {
    private String sdt;
    private String hoTen;
    private Integer isEat = 1; // 0.0 1. có
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "Asia/Saigon")
    private String ngayCheck;
    private Integer buoi; // 0.sang 1.trưa 2.tối

    @JsonIgnore
    public boolean isInvalid() {
        return Strings.isNullOrEmpty(sdt) || Strings.isNullOrEmpty(ngayCheck);
    }
}
