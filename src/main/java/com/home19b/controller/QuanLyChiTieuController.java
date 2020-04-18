package com.home19b.controller;

import com.home19b.domain.request.CheckInOutRequest;
import com.home19b.domain.request.ThongTinCheckInRequest;
import com.home19b.domain.request.UpdateNoteRequest;
import com.home19b.domain.response.BaseResponse;
import com.home19b.services.QuanLyChiTieuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

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


}
