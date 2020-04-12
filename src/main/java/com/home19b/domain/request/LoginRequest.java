package com.home19b.domain.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.base.Strings;
import lombok.Data;

@Data
public class LoginRequest {
    private String sdt;
    private String password;

    @JsonIgnore
    public boolean isInvalid() {
        return Strings.isNullOrEmpty(sdt) || Strings.isNullOrEmpty(password);
    }
}
