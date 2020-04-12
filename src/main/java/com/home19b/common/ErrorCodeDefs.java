package com.home19b.common;

public class ErrorCodeDefs {
    public static final int ERR_CODE_INPUT_INVALID = 999;
    public static final int ERR_CODE_PARAMS_INVALID = 5;
    public static final int ERR_CODE_SERVER_ERROR = -1;
    public static final int ERR_CODE_ITEM_NOT_FOUND = 2;
    public static final int ERR_CODE_FAILED = 1;
    public static final int ERR_CODE_OK = 0;
    public static final int UNKNOWN = -10;
    public static final int UNAUTHORIZED = -104;
    public static final int ERR_CODE_OTHER = -7;

    public static String getErrDesc(int errCode) {
        String str = "Lỗi chưa xác định";
        switch (errCode) {
            case UNKNOWN:
                break;

            case UNAUTHORIZED:
                str = "Không có quyền truy cập";
                break;

            case ERR_CODE_OTHER:
                str = "Lỗi không xác định";
                break;

            case ERR_CODE_SERVER_ERROR:
                str = "Server bị lỗi";
                break;

            case ERR_CODE_OK:
                str = "Thành công";
                break;

            case ERR_CODE_FAILED:
                str = "Có lỗi xảy ra";
                break;

            case ERR_CODE_ITEM_NOT_FOUND:
                str = "Không tìm thấy dữ liệu";
                break;


            case ERR_CODE_PARAMS_INVALID:
                str = "Tham số không hợp lệ";
                break;

            default:
                break;
        }
        return str;
    }
}
