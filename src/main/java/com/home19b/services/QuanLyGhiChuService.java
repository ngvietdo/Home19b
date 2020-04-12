package com.home19b.services;

import com.home19b.domain.request.FilterGhiChuRequest;
import com.home19b.domain.request.GhiChuRequest;
import com.home19b.domain.response.BaseResponse;
import com.home19b.domain.response.GetArrayResponse;
import com.home19b.repo.dao.QuanLyGhiChuDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class QuanLyGhiChuService {
    @Autowired
    QuanLyGhiChuDao quanLyGhiChuDao;


    public void createGhiChu(GhiChuRequest request) {
        quanLyGhiChuDao.createGhiChu(request);
    }

    public GetArrayResponse filterGhiChu(FilterGhiChuRequest request) {
        GetArrayResponse getArrayResponse = new GetArrayResponse();

        if (!request.isInvalid()) {
            List<GhiChuRequest> ghiChuRequests = quanLyGhiChuDao.filterGhiChu(request);
            getArrayResponse.setSuccess(ghiChuRequests, ghiChuRequests.size());
        }else {
            getArrayResponse.setFailed();
        }
        return getArrayResponse;
    }
}
