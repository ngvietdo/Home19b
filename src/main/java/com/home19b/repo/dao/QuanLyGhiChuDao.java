package com.home19b.repo.dao;

import com.home19b.common.CollectionMongoUtils;
import com.home19b.domain.dto.CheckIn;
import com.home19b.domain.request.FilterGhiChuRequest;
import com.home19b.domain.request.GhiChuRequest;
import com.mongodb.Block;
import com.mongodb.client.model.Filters;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
@Slf4j
public class QuanLyGhiChuDao extends BaseDao {

    public String createGhiChu(GhiChuRequest request) {
        Document document = new Document();
        document.append("sdt", request.getSdt());
        document.append("ngayGhiChu", request.getNgayGhiChu());
        document.append("buoi", request.getBuoi());
        document.append("soTien", request.getSoTien());
        document.append("note", request.getNote());
        insertOne(CollectionMongoUtils.CLT_GHICHU, document);
        String id = document.get("_id").toString();
        return id;
    }

    public void delateGhiChu(String id) {
        Document document = new Document();
        ObjectId objectId = new ObjectId(id);
        document.put("_id", objectId);
        delete(CollectionMongoUtils.CLT_GHICHU, document);
    }

    public void updateGhiChu(GhiChuRequest request) {
        Document document = new Document();
        document.append("sdt", request.getSdt());
        document.append("ngayGhiChu", request.getNgayGhiChu());
        document.append("buoi", request.getBuoi());
        document.append("soTien", request.getSoTien());
        if (request.getNote() != null) {
            document.append("note", request.getNote());
        }

        Document condition = new Document();
        condition.put("_id", new ObjectId(request.getMa()));
        update(CollectionMongoUtils.CLT_GHICHU, condition, new Document("$set", document));
    }

    public List<GhiChuRequest> filterGhiChu(FilterGhiChuRequest request) {
        List<GhiChuRequest> result = new ArrayList<>();
        Bson requests;
        if (request.getSdt() == null) {
            requests = Filters.and(
                    Filters.gte("ngayGhiChu", request.getTuNgay()),
                    Filters.lte("ngayGhiChu", request.getDenNgay())
            );
        } else {
            requests = Filters.and(
                    Filters.gte("ngayGhiChu", request.getTuNgay()),
                    Filters.lte("ngayGhiChu", request.getDenNgay()),
                    Filters.eq("sdt", request.getSdt())
            );
        }

        log.info("request {}", request);
        getCollection(CollectionMongoUtils.CLT_GHICHU, GhiChuRequest.class)
                .find(requests).skip(request.getStart()).limit(request.getLimit()).forEach((Block<? super GhiChuRequest>) document -> {
            document.setMa(document.getId().toString());
            log.info("document {}", document);
            result.add(document);
        });
        return result;
    }

    public Long count(FilterGhiChuRequest request) {
        Bson requests;
        if (request.getSdt() == null) {
            requests = Filters.and(
                    Filters.gte("ngayGhiChu", request.getTuNgay()),
                    Filters.lte("ngayGhiChu", request.getDenNgay())
            );
        } else {
            requests = Filters.and(
                    Filters.gte("ngayGhiChu", request.getTuNgay()),
                    Filters.lte("ngayGhiChu", request.getDenNgay()),
                    Filters.eq("sdt", request.getSdt())
            );
        }
        return getCollection(CollectionMongoUtils.CLT_GHICHU, GhiChuRequest.class).countDocuments(requests);
    }
}
