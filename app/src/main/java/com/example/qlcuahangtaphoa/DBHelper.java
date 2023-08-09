package com.example.qlcuahangtaphoa;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {

    public static final String tenDB = "QLCuaHangTapHoa";
    public static final String tenBangKhachHang = "KhachHang";
    public static final String maKhachHang = "MaKhachHang";
    public static final String tenKhachHang = "TenKhachHang";
    public static final String emailKhachHang = "Email";
    public static final String sdtKhachHang = "SDT";
    public static final String diaChiKhachHang = "DiaChi";
    public static final String ghiChu = "GhiChu";

    public static final String tenBangHangHoa = "HangHoa";
    public static final String maHangHoa = "MaHangHoa";
    public static final String tenHangHoa = "TenHangHoa";
    public static final String danhMucHangHoa = "DanhMucHangHoa";
    public static final String giaNhap = "GiaNhap";
    public static final String giaBan = "GiaBan";
    public static final String soLuong = "SoLuong";
    public static final String ghiChuHH = "GhiChuHH";

    public static final String tenBangDonHang = "DonHang";
    public static final String maDonHang = "MaDonHang";
    public static final String maKH = "MaKH";
    public static final String maHH = "MaHH";
    public static final String tenDonHang = "TenDonHang";
    public static final String thoiGian = "ThoiGian";
    public static final String trangThai = "TrangThai";
    public static final String phiGiao = "PhiGiao";
    public static final String tongTien = "TongTien";
    public static final String ghiChuDH = "GhiChu";
    public static final String ptThanhToan = "PTThanhToan";
    public DBHelper(Context context) {
        super(context, tenDB, null, 8);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL("create table KhachHang(" +
                "MaKhachHang integer primary key autoincrement," +
                "TenKhachHang text ," +
                "Email text ," +
                "SDT text ," +
                "DiaChi text ," +
                "GhiChu text)");
        database.execSQL("create table HangHoa(" +
                "MaHangHoa integer primary key autoincrement," +
                "TenHangHoa text ," +
                "DanhMucHangHoa text, " +
                "GiaNhap real, " +
                "GiaBan real, " +
                "SoLuong integer, " +
                "GhiChuHH text)" );
        database.execSQL("create table DonHang(" +
                "MaDonHang integer primary key autoincrement," +
                "MaKH integer," +
                "MaHH integer," +
                "TenDonHang text," +
                "ThoiGian text," +
                "PhiGiao real," +
                "TrangThai text," +
                "TongTien real," +
                "GhiChu text," +
                "PTThanhToan text," +
                "FOREIGN KEY(MaKH) REFERENCES KhachHang(MaKhachHang)," +
                "FOREIGN KEY(MaHH) REFERENCES HangHoa(MaHangHoa))");

    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int i, int i1) {
        database.execSQL("drop table if exists KhachHang");
        database.execSQL("drop table if exists HangHoa");
        database.execSQL("drop table if exists DonHang");
        onCreate(database);
    }
}
