package com.home19b.services;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.home19b.common.AppUtils;
import com.home19b.common.CollectionMongoUtils;
import com.home19b.domain.dto.CheckIn;
import com.home19b.domain.dto.GhiChu;
import com.home19b.domain.dto.User;
import com.home19b.domain.model.ThongTinChiThu;
import com.home19b.domain.model.ThongTinChiThuTrongNgay;
import com.home19b.domain.request.CheckInOutRequest;
import com.home19b.domain.request.ThongTinCheckInRequest;
import com.home19b.domain.request.UpdateNoteRequest;
import com.home19b.domain.response.BaseResponse;
import com.home19b.domain.response.GetArrayResponse;
import com.home19b.repo.dao.QuanLyChiTieuDao;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.LocalDate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class QuanLyChiTieuService {
    final QuanLyChiTieuDao quanLyChiTieuDao;

    public QuanLyChiTieuService(QuanLyChiTieuDao quanLyChiTieuDao) {
        this.quanLyChiTieuDao = quanLyChiTieuDao;
    }

    public BaseResponse checkInOut(CheckInOutRequest request) {
        BaseResponse response = new BaseResponse();
        if (request == null || request.isInvalid()) {
            response.setParamsInvalid();
            return response;
        }

        if (request.getIsEat() < 0 || request.getIsEat() > 1) {
            response.setFailed("Vui lòng chọn đúng kiểu check !");
            return response;
        }

        if (quanLyChiTieuDao.checkIn(request)) {
            response.setSuccess();
        } else {
            response.setFailed("Check In thất bại");
        }
        return response;
    }

    public BaseResponse getInfoCheckIn(ThongTinCheckInRequest request) {
        GetArrayResponse response = new GetArrayResponse();
        if (request == null || request.isInvalid()) {
            response.setParamsInvalid();
            return response;
        }
        List<CheckIn> result = quanLyChiTieuDao.getInfoCheckIn(request);
        response.setSuccess(result, result.size());
        return response;
    }

    public BaseResponse updateNote(UpdateNoteRequest request) {
        BaseResponse response = new BaseResponse();
        if (request == null || request.isInvalid()) {
            response.setParamsInvalid();
            return response;
        }
        if (quanLyChiTieuDao.updateNote(request)) {
            response.setSuccess();
        } else {
            response.setFailed("Cập nhật thất bại");
        }
        return response;
    }

    public BaseResponse magicSkill(ThongTinCheckInRequest request) throws ParseException {
        GetArrayResponse response = new GetArrayResponse();
        if (request == null || request.isInvalid()) {
            response.setParamsInvalid();
            return response;
        }
        List<CheckIn> result = quanLyChiTieuDao.getInfoCheckIn(request);
        if (CollectionUtils.isEmpty(result)) {
            response.setItemNotFound("Không có ngày nào để tính toán cả");
            return response;
        }

        List<GhiChu> resultGhiChu = quanLyChiTieuDao.getInfoGhiChu(request);
        if (CollectionUtils.isEmpty(resultGhiChu)) {
            response.setItemNotFound("Không có ngày nào để tính toán cả");
            return response;
        }

        LocalDate dateBefore = LocalDate.fromDateFields(AppUtils.parseDate(request.getTuNgay(), AppUtils.DATE_ONLY_PATTERN));
        LocalDate dateAfter = LocalDate.fromDateFields(AppUtils.parseDate(request.getDenNgay(), AppUtils.DATE_ONLY_PATTERN));

        //tinh tổng số tiền chi
        Map<String, Double> mapTongTienTruaChi = new HashMap<>();
        Map<String, Double> mapTongTienToiChi = new HashMap<>();
        Map<String, Double> mapTongTienChungChi = new HashMap<>();

        Map<String, Set<String>> mapSoNguoiAnTrongBuaTrua = new HashMap<>();
        Map<String, Set<String>> mapSoNguoiAnTrongBuaToi = new HashMap<>();

        Map<String, List<ThongTinChiThuTrongNgay>> mapNguoiChiBuoiTrua = new HashMap<>();
        Map<String, List<ThongTinChiThuTrongNgay>> mapNguoiChiBuoiToi = new HashMap<>();
        Map<String, List<ThongTinChiThuTrongNgay>> mapNguoiChiBuoiChung = new HashMap<>();

        Map<String, ThongTinChiThu> mapFinal = convertListUserTo(quanLyChiTieuDao.findAllUser());

        while (!dateBefore.isEqual(dateAfter)) {
            String dateCur = AppUtils.formatDate(dateBefore.toDate(), AppUtils.DATE_ONLY_PATTERN);
            resultGhiChu.stream().forEach(obj -> {
                if (dateCur.equals(obj.getNgayGhiChu()) && obj.getBuoi() == CollectionMongoUtils.BUOI_TRUA) {
                    // tinh tổng chi
                    if (mapTongTienTruaChi.containsKey(dateCur)) {
                        Double tongTienTrua = mapTongTienTruaChi.get(dateCur);
                        tongTienTrua += obj.getSoTienChi();
                        mapTongTienTruaChi.put(dateCur, tongTienTrua);
                    } else {
                        mapTongTienTruaChi.put(dateCur, obj.getSoTienChi());
                    }

                    if (mapSoNguoiAnTrongBuaTrua.containsKey(dateCur)) {
                        mapSoNguoiAnTrongBuaTrua.get(dateCur).add(obj.getSdt());
                    } else {
                        mapSoNguoiAnTrongBuaTrua.put(dateCur, Sets.newHashSet(obj.getSdt()));
                    }

                    if (mapNguoiChiBuoiTrua.containsKey(dateCur)) {
                        ThongTinChiThuTrongNgay tt = new ThongTinChiThuTrongNgay();
                        tt.setSdt(obj.getSdt());
                        tt.setSoTienChi(obj.getSoTienChi());
                        mapNguoiChiBuoiTrua.get(dateCur).add(tt);
                    } else {
                        ThongTinChiThuTrongNgay tt = new ThongTinChiThuTrongNgay();
                        tt.setSdt(obj.getSdt());
                        tt.setSoTienChi(obj.getSoTienChi());
                        mapNguoiChiBuoiTrua.put(dateCur, Lists.newArrayList(tt));
                    }
                }

                if (dateCur.equals(obj.getNgayGhiChu()) && obj.getBuoi() == CollectionMongoUtils.BUOI_TOI) {
                    if (mapTongTienToiChi.containsKey(dateCur)) {
                        Double tongTienTrua = mapTongTienToiChi.get(dateCur);
                        tongTienTrua += obj.getSoTienChi();
                        mapTongTienToiChi.put(dateCur, tongTienTrua);
                    } else {
                        mapTongTienToiChi.put(dateCur, obj.getSoTienChi());
                    }

                    if (mapSoNguoiAnTrongBuaToi.containsKey(dateCur)) {
                        mapSoNguoiAnTrongBuaToi.get(dateCur).add(obj.getSdt());
                    } else {
                        mapSoNguoiAnTrongBuaToi.put(dateCur, Sets.newHashSet(obj.getSdt()));
                    }

                    if (mapNguoiChiBuoiToi.containsKey(dateCur)) {
                        ThongTinChiThuTrongNgay tt = new ThongTinChiThuTrongNgay();
                        tt.setSdt(obj.getSdt());
                        tt.setSoTienChi(obj.getSoTienChi());
                        mapNguoiChiBuoiToi.get(dateCur).add(tt);
                    } else {
                        ThongTinChiThuTrongNgay tt = new ThongTinChiThuTrongNgay();
                        tt.setSdt(obj.getSdt());
                        tt.setSoTienChi(obj.getSoTienChi());
                        mapNguoiChiBuoiToi.put(dateCur, Lists.newArrayList(tt));
                    }
                }

                if (dateCur.equals(obj.getNgayGhiChu()) && obj.getBuoi() == CollectionMongoUtils.BUOI_CHUNG) {
                    if (mapTongTienChungChi.containsKey(dateCur)) {
                        Double tongTienTrua = mapTongTienChungChi.get(dateCur);
                        tongTienTrua += obj.getSoTienChi();
                        mapTongTienChungChi.put(dateCur, tongTienTrua);
                    } else {
                        mapTongTienChungChi.put(dateCur, obj.getSoTienChi());
                    }

                    if (mapNguoiChiBuoiChung.containsKey(dateCur)) {
                        ThongTinChiThuTrongNgay tt = new ThongTinChiThuTrongNgay();
                        tt.setSdt(obj.getSdt());
                        tt.setSoTienChi(obj.getSoTienChi());
                        mapNguoiChiBuoiChung.get(dateCur).add(tt);
                    } else {
                        ThongTinChiThuTrongNgay tt = new ThongTinChiThuTrongNgay();
                        tt.setSdt(obj.getSdt());
                        tt.setSoTienChi(obj.getSoTienChi());
                        mapNguoiChiBuoiChung.put(dateCur, Lists.newArrayList(tt));
                    }
                }
            });
            dateBefore.plusDays(1);
        }

        // tinh tong tien bua trua cho tung user
        for (Map.Entry<String, Double> entry : mapTongTienTruaChi.entrySet()) {
            String dateCur = entry.getKey();
            Double totalCur = entry.getValue();
            if (totalCur == 0) {
                continue;
            }
            Set<String> setNguoiAn = mapSoNguoiAnTrongBuaTrua.get(dateCur);
            if (setNguoiAn.isEmpty()) {
                continue;
            }
            Double moneyPerPerson = totalCur / setNguoiAn.size();
            setNguoiAn.forEach(ngAn -> {
                ThongTinChiThu thongTinChiThu = mapFinal.get(ngAn);
                thongTinChiThu.setSoTienPhaiDongTrua(thongTinChiThu.getSoTienPhaiDongTrua() + moneyPerPerson);
                List<ThongTinChiThuTrongNgay> lstTtChiThuTrongNgay = mapNguoiChiBuoiTrua.get(dateCur).stream().filter(ngChi -> ngChi.getSdt().equals(ngAn)).collect(Collectors.toList());
                lstTtChiThuTrongNgay.forEach(o -> {
                    thongTinChiThu.setSoTienChiTrua(thongTinChiThu.getSoTienChiTrua() + o.getSoTienChi());
                });
                mapFinal.put(ngAn, thongTinChiThu);
            });
        }

        // tinh tong tien bua toi cho tung user
        for (Map.Entry<String, Double> entry : mapTongTienToiChi.entrySet()) {
            String dateCur = entry.getKey();
            Double totalCur = entry.getValue();
            if (totalCur == 0) {
                continue;
            }
            Set<String> setNguoiAn = mapSoNguoiAnTrongBuaToi.get(dateCur);
            if (setNguoiAn.isEmpty()) {
                continue;
            }
            Double moneyPerPerson = totalCur / setNguoiAn.size();
            setNguoiAn.forEach(ngAn -> {
                ThongTinChiThu thongTinChiThu = mapFinal.get(ngAn);
                thongTinChiThu.setSoTienPhaiDongToi(thongTinChiThu.getSoTienPhaiDongToi() + moneyPerPerson);
                List<ThongTinChiThuTrongNgay> lstTtChiThuTrongNgay = mapNguoiChiBuoiToi.get(dateCur).stream().filter(ngChi -> ngChi.getSdt().equals(ngAn)).collect(Collectors.toList());
                lstTtChiThuTrongNgay.forEach(o -> {
                    thongTinChiThu.setSoTienChiToi(thongTinChiThu.getSoTienChiToi() + o.getSoTienChi());
                });
                mapFinal.put(ngAn, thongTinChiThu);
            });
        }

        // tinh tong tien bua chung cho tung user
        for (Map.Entry<String, Double> entry : mapTongTienChungChi.entrySet()) {
            String dateCur = entry.getKey();
            Double totalCur = entry.getValue();
            if (totalCur == 0) {
                continue;
            }
            long soNguoiAn = quanLyChiTieuDao.countUser();
            Double moneyPerPerson = totalCur / soNguoiAn;
            for (Map.Entry<String, ThongTinChiThu> entry1 : mapFinal.entrySet()) {
                String user = entry1.getKey();
                ThongTinChiThu thongTinChiThu = entry1.getValue();
                thongTinChiThu.setSoTienPhaiDongChung(thongTinChiThu.getSoTienPhaiDongChung() + moneyPerPerson);
                List<ThongTinChiThuTrongNgay> lstTtChiThuTrongNgay = mapNguoiChiBuoiChung.get(dateCur).stream().filter(ngChi -> ngChi.getSdt().equals(user)).collect(Collectors.toList());
                lstTtChiThuTrongNgay.forEach(o -> {
                    thongTinChiThu.setSoTienChiChung(thongTinChiThu.getSoTienChiChung() + o.getSoTienChi());
                });
            }
        }

        List<ThongTinChiThu> userChiThuList = (List<ThongTinChiThu>) mapFinal.values();
        response.setSuccess(userChiThuList, userChiThuList.size());
        return response;
    }


    public BaseResponse generateDay() {
        BaseResponse response = new BaseResponse();
        String curDayStr = AppUtils.formatDate(new Date(), AppUtils.DATE_ONLY_PATTERN);
        if (quanLyChiTieuDao.generatedDay(curDayStr)) {
            response.setFailed("Tuần này đã được gen . Không thể gen nữa !");
            return response;
        }

        List<String> lstDayCheckIn = new ArrayList<>();
        Date startWeek = AppUtils.getFromStartWeek();
        Date endWeek = AppUtils.getToEndWeek();
        while (startWeek.before(endWeek)) {
            String startWeekStr = AppUtils.formatDate(startWeek, AppUtils.DATE_ONLY_PATTERN);
            lstDayCheckIn.add(startWeekStr);
            startWeek = AppUtils.appendDay(startWeek, 1);
        }
        lstDayCheckIn.add(AppUtils.formatDate(endWeek, AppUtils.DATE_ONLY_PATTERN));
        quanLyChiTieuDao.generateDay(lstDayCheckIn);
        response.setSuccess();
        return response;
    }

    public Map<String, ThongTinChiThu> convertListUserTo(List<User> lst) {
        Map<String, ThongTinChiThu> result = new HashMap<>();
        lst.forEach(obj -> {
            result.put(obj.getSdt(), new ThongTinChiThu(obj.getSdt(), obj.getHoTen()));
        });
        return result;
    }
}
