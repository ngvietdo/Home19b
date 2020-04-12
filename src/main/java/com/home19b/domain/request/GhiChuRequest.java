package com.home19b.domain.request;

import lombok.Data;

@Data
public class GhiChuRequest {
    private String id;
    private String sdt;
    private String ngayGhiChu;
    private String buoi;
    private Long soTien;

    public boolean validate() {
        if (sdt == null || ngayGhiChu == null || buoi == null || soTien == null || soTien == 0) {
            return false;
        }
        return true;
    }
}
