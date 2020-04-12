package com.home19b.domain.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.base.Strings;
import lombok.Data;

@Data
public class UpdateNoteRequest {
    private String sdt;
    private String ngayCheck;
    private Integer buoi;  // 0.sang 1.trưa 2.tối
    private String note;
    private Double soTienChi;

    @JsonIgnore
    public boolean isInvalid() {
        return Strings.isNullOrEmpty(sdt) || soTienChi == null || Strings.isNullOrEmpty(ngayCheck) || buoi == null;
    }
}
