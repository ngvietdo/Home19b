package com.home19b.domain.dto;

import lombok.Data;

@Data
public class GhiChu {
    private String sdt;
    private String ngayGhiChu;
    private String buoi; // 0.sang 1.trưa 2.tối 3.chung
    private Double soTienChi;
    private String note;
}
