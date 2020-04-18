package com.home19b.controller;

import com.home19b.domain.request.FilterGhiChuRequest;
import com.home19b.domain.request.GhiChuRequest;
import com.home19b.domain.response.BaseResponse;
import com.home19b.domain.response.GetArrayResponse;
import com.home19b.services.QuanLyGhiChuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/quanLyGhiChu")
@Api(value = "Các api về quản lý ghi chú hàng tuần")
public class GhiChuController {

    @Autowired
    QuanLyGhiChuService quanLyGhiChuService;

    @PostMapping("/them")
    @ApiOperation("Thêm mới ghi chú")
    public BaseResponse createGhiChu(@RequestBody GhiChuRequest request) {
        BaseResponse baseResponse = new BaseResponse();
        if (request.validate()) {
            String id = quanLyGhiChuService.createGhiChu(request);
            baseResponse.setSuccess();
            baseResponse.setRd(id);
        } else {
            baseResponse.setParamsInvalid();
        }
        return baseResponse;
    }

    @PostMapping("/sua")
    @ApiOperation("Câp nhật ghi chú")
    public BaseResponse updateGhiChu(@RequestBody GhiChuRequest request) {
        BaseResponse baseResponse = new BaseResponse();
        if (request.validate()) {
            quanLyGhiChuService.updateGhiChu(request);
            baseResponse.setSuccess();
        } else {
            baseResponse.setParamsInvalid();
        }
        return baseResponse;
    }

    @PostMapping("/danhSach")
    @ApiOperation("Danh Sách ghi chú")
    public GetArrayResponse filterGhiChu(@RequestBody FilterGhiChuRequest request) {
        return quanLyGhiChuService.filterGhiChu(request);
    }

    @DeleteMapping("/xoa")
    @ApiOperation("Xóa ghi chú")
    public BaseResponse deleteGhiChu(@RequestParam String id) {
        BaseResponse response = new BaseResponse();
        quanLyGhiChuService.deleteGhiChu(id);
        response.setSuccess();
        return response;
    }
}
