package com.home19b.domain.dto;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class CheckIn {
    private String sdt;
    private String hoTen;
    private String ngayCheckIn;
    private Integer buoi; // 0.sang 1.trưa 2.tối 3.chung
    private Integer isEat; // 0.0 1. có
//    private Double soTienChi;
//    private String idTuan;
//    private String ghiChu;
}
