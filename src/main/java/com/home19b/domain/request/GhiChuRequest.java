package com.home19b.domain.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

@Data
public class GhiChuRequest {
    @BsonProperty("_id")
    @Id
    private ObjectId id;
    private String ma;
    private String sdt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "Asia/Saigon")
    private String ngayGhiChu;
    private String buoi;
    private Long soTien;
    private String note;

    public boolean validate() {
        if (sdt == null || ngayGhiChu == null || buoi == null || soTien == null) {
            return false;
        }
        return true;
    }
}
