package com.home19b.repo.dao;

import com.home19b.common.AppUtils;
import com.home19b.common.CollectionMongoUtils;
import com.home19b.domain.dto.CheckIn;
import com.home19b.domain.dto.GhiChu;
import com.home19b.domain.dto.User;
import com.home19b.domain.request.CheckInOutRequest;
import com.home19b.domain.request.ThongTinCheckInRequest;
import com.mongodb.BasicDBObject;
import com.mongodb.Block;
import com.mongodb.client.model.Filters;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.joda.time.LocalDate;
import org.springframework.stereotype.Repository;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

@Repository
@Slf4j
public class QuanLyChiTieuDao extends BaseDao {

    public boolean checkIn(CheckInOutRequest request) {
        Bson condition = Filters.and(
                Filters.eq("sdt", request.getSdt()),
                Filters.eq("ngayCheckIn", request.getNgayCheck()),
                Filters.eq("buoi", request.getBuoi())
        );
        Bson valueWillUpdate = new BasicDBObject("$set", new BasicDBObject("isEat", request.getIsEat()));
        long result = update(CollectionMongoUtils.CLT_CHECKIN, condition, valueWillUpdate);
        return result > 0;
    }

    public long countUser() {
        long count = countAll(CollectionMongoUtils.CLT_USER, null);
        return count;
    }

    public List<User> findAllUser() {
        List<User> result = new ArrayList<>();
        getCollection(CollectionMongoUtils.CLT_USER, User.class).find().forEach((Block<? super User>) document -> {
            result.add(document);
        });
        return result;
    }

    public List<CheckIn> getInfoCheckIn(ThongTinCheckInRequest request) throws ParseException {
        List<CheckIn> result = new ArrayList<>();
        //
        List<String> lstNgay = new ArrayList<>();
        LocalDate dateBefore = LocalDate.fromDateFields(AppUtils.parseDate(request.getTuNgay(), AppUtils.DATE_ONLY_PATTERN));
        LocalDate dateAfter = LocalDate.fromDateFields(AppUtils.parseDate(request.getDenNgay(), AppUtils.DATE_ONLY_PATTERN));
        while (!dateBefore.isAfter(dateAfter)) {
            String dateCur = AppUtils.formatDate(dateBefore.toDate(), AppUtils.DATE_ONLY_PATTERN);
            lstNgay.add(dateCur);
            dateBefore = dateBefore.plusDays(1);
        }
        //
        getCollection(CollectionMongoUtils.CLT_CHECKIN, CheckIn.class)
                .find(Filters.in("ngayCheckIn", lstNgay
                )).forEach((Block<? super CheckIn>) document -> {
            result.add(document);
        });
        return result;
    }

    public List<GhiChu> getInfoGhiChu(ThongTinCheckInRequest request) throws ParseException {
        List<GhiChu> result = new ArrayList<>();
        //
        List<String> lstNgay = new ArrayList<>();
        LocalDate dateBefore = LocalDate.fromDateFields(AppUtils.parseDate(request.getTuNgay(), AppUtils.DATE_ONLY_PATTERN));
        LocalDate dateAfter = LocalDate.fromDateFields(AppUtils.parseDate(request.getDenNgay(), AppUtils.DATE_ONLY_PATTERN));
        while (!dateBefore.isAfter(dateAfter)) {
            String dateCur = AppUtils.formatDate(dateBefore.toDate(), AppUtils.DATE_ONLY_PATTERN);
            lstNgay.add(dateCur);
            dateBefore = dateBefore.plusDays(1);
        }
        //
        getCollection(CollectionMongoUtils.CLT_GHICHU, GhiChu.class)
                .find(Filters.in("ngayGhiChu", lstNgay
                )).forEach((Block<? super GhiChu>) document -> {
            result.add(document);
        });
        return result;
    }

    public boolean generatedDay(String ngayMai) {
        Bson condition = Filters.eq("ngayCheckIn", ngayMai);
        Document document = findOne(CollectionMongoUtils.CLT_CHECKIN, condition);
        return document != null;
    }

    public void generateDay(List<String> lstNgay) {
        List<Document> lstDoc = new ArrayList<>();
        List<User> lstUser = findAllUser();
        if (lstUser.isEmpty()) {
            return;
        }
        for (User user : lstUser) {
            for (String str : lstNgay) {
                lstDoc.add(genBuoiTrua(user, str));
                lstDoc.add(genBuoiToi(user, str));
            }
        }
        insertMany(CollectionMongoUtils.CLT_CHECKIN, lstDoc);
    }

    public Document genBuoiTrua(User user, String date) {
        Document document = new Document();
        document.append("sdt", user.getSdt());
        document.append("hoTen", user.getHoTen());
        document.append("ngayCheckIn", date);
        document.append("buoi", 1);
        document.append("isEat", 0);
        document.append("chucVuName", user.getChucVuName());
        return document;
    }

    public Document genBuoiToi(User user, String date) {
        Document document = new Document();
        document.append("sdt", user.getSdt());
        document.append("hoTen", user.getHoTen());
        document.append("ngayCheckIn", date);
        document.append("buoi", 2);
        document.append("isEat", 1);
        document.append("chucVuName", user.getChucVuName());
        return document;
    }
}
