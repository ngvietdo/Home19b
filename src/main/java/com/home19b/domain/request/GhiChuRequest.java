package com.home19b.domain.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

@Data
public class GhiChuRequest {
    private String id;
    private String sdt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "Asia/Saigon")
    private String ngayGhiChu;
    private String buoi;
    private Long soTien;
    private String note;

    public boolean validate() {
        if (sdt == null || ngayGhiChu == null || buoi == null || soTien == null || soTien == 0) {
            return false;
        }
        return true;
    }
}
