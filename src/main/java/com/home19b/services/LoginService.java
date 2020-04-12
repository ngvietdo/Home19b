package com.home19b.services;

import com.home19b.common.ErrorCodeDefs;
import com.home19b.domain.dto.User;
import com.home19b.domain.request.LoginRequest;
import com.home19b.domain.response.BaseResponse;
import com.home19b.domain.response.ItemResponse;
import com.home19b.repo.dao.LoginDao;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

    final LoginDao loginDao;

    public LoginService(LoginDao loginDao) {
        this.loginDao = loginDao;
    }

    public BaseResponse logIn(LoginRequest request) {
        if (request == null || request.isInvalid()) {
            BaseResponse response = new BaseResponse();
            response.setRc(ErrorCodeDefs.ERR_CODE_PARAMS_INVALID);
            response.setRd("Vui lòng nhập đủ dữ liệu đầu vào");
            return response;
        }
        User user = loginDao.findUserLogin(request);
        if (user == null) {
            BaseResponse response = new BaseResponse();
            response.setItemNotFound("User không tồn tại !");
            return response;
        }
        return new ItemResponse<User>(ItemResponse.Builder.newInstance().setSuccess(user));
    }
}
