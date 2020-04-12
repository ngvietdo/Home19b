package com.home19b.domain.model;

import lombok.Data;

@Data
public class ThongTinChiThu {
    private String sdt;
    private String hoTen;
    private Double soTienChiTrua;
    private Double soTienChiToi;
    private Double soTienChiChung;
    private Double soTienPhaiDongTrua;
    private Double soTienPhaiDongToi;
    private Double soTienPhaiDongChung;

    public ThongTinChiThu(Double soTienChiTrua, Double soTienChiToi, Double soTienChiChung, Double soTienPhaiDongTrua, Double soTienPhaiDongToi, Double soTienPhaiDongChung) {
        this.soTienChiTrua = soTienChiTrua;
        this.soTienChiToi = soTienChiToi;
        this.soTienChiChung = soTienChiChung;
        this.soTienPhaiDongTrua = soTienPhaiDongTrua;
        this.soTienPhaiDongToi = soTienPhaiDongToi;
        this.soTienPhaiDongChung = soTienPhaiDongChung;
    }

    public ThongTinChiThu(String sdt,String hoTen) {
        this.sdt = sdt;
        this.hoTen = hoTen;
        this.soTienChiChung = 0d;
        this.soTienChiToi = 0d;
        this.soTienChiTrua = 0d;
        this.soTienPhaiDongToi = 0d;
        this.soTienPhaiDongTrua = 0d;
        this.soTienPhaiDongChung = 0d;
    }
}
