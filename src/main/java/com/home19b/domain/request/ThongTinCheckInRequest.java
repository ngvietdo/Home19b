package com.home19b.domain.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.base.Strings;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ThongTinCheckInRequest {
    private String tuNgay;
    private String denNgay;

    @JsonIgnore
    public boolean isInvalid() {
        return Strings.isNullOrEmpty(tuNgay) || Strings.isNullOrEmpty(denNgay);
    }
}
