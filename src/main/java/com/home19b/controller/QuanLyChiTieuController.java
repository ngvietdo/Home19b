package com.home19b.controller;

import com.home19b.domain.request.CheckInOutRequest;
import com.home19b.domain.request.FilterGhiChuRequest;
import com.home19b.domain.request.ThongTinCheckInRequest;
import com.home19b.domain.response.BaseResponse;
import com.home19b.domain.response.DetailResponse;
import com.home19b.domain.response.GetArrayResponse;
import com.home19b.services.QuanLyChiTieuService;
import com.home19b.services.QuanLyGhiChuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/quanLyChiTieu")
@Api(value = "Các api về quản lý chi tiêu hang tuần")
public class QuanLyChiTieuController {

    final QuanLyChiTieuService quanLyChiTieuService;

    public QuanLyChiTieuController(QuanLyChiTieuService quanLyChiTieuService) {
        this.quanLyChiTieuService = quanLyChiTieuService;
    }

    @PostMapping("/checkInOut")
    @ApiOperation("1 thằng checkin or uncheck trong ngày")
    public BaseResponse checkInOut(@RequestBody CheckInOutRequest request) {
        return quanLyChiTieuService.checkInOut(request);
    }

    @PostMapping("/thongTinCheckIn")
    @ApiOperation("Xem thông tin checkIn của cả nhà")
    public BaseResponse getInfoCheckIn(@RequestBody ThongTinCheckInRequest request) {
        return quanLyChiTieuService.getInfoCheckIn(request);
    }

    @PostMapping("/magicSkill")
    @ApiOperation("Magiccccccccc Skilllllllllllll")
    public BaseResponse tinhTongChiTieuTungNgay(@RequestBody ThongTinCheckInRequest request) throws ParseException {
        return quanLyChiTieuService.magicSkill(request);
    }

    @GetMapping("/generateDay")
    @ApiOperation("gen")
    public BaseResponse generateDay() {
        return quanLyChiTieuService.generateDay();
    }


    @PostMapping("/chiTietChiNop")
    @ApiOperation("Chi Tiết người chi, nộp")
    public GetArrayResponse detail(FilterGhiChuRequest request) {
        GetArrayResponse getArrayResponse = new GetArrayResponse();
        List<DetailResponse> responses = quanLyChiTieuService.detailSreach(request);
        getArrayResponse.setRows(responses);
        getArrayResponse.setSuccess();
        return getArrayResponse;
    }
}
