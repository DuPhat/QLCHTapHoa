package com.example.qlcuahangtaphoa;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    public static final String tenDB = "QLCuaHangTapHoa";
    //Người dùng
    public static final String tenBangNguoiDung = "NguoiDung";
    public static final String maNguoiDung = "MaNguoiDung";
    public static final String tenNguoiDung = "TenNguoiDung";
    public static final String emailNguoiDung = "EmailNguoiDung";
    public static final String matKhauNguoiDung = "MatKhau";
    public static final String gioiTinhNguoiDung = "GioiTinh";
    public static final String ngaySinhNguoiDung = "NgaySinh";
    public static final String sdtNguoiDung = "SDT";
    public static final String diaChiNguoiDung = "DiaChi";
    //Khách hàng
    public static final String tenBangKhachHang = "KhachHang";
    public static final String maKhachHang = "MaKhachHang";
    public static final String tenKhachHang = "TenKhachHang";
    public static final String sdtKhachHang = "SDT";
    public static final String diaChiKhachHang = "DiaChi";
    public static final String ghiChu = "GhiChu";
    //Hàng hóa
    public static final String tenBangHangHoa = "HangHoa";
    public static final String maHangHoa = "MaHangHoa";
    public static final String tenHangHoa = "TenHangHoa";
    public static final String danhMucHangHoa = "DanhMucHangHoa";
    public static final String giaNhap = "GiaNhap";
    public static final String giaBan = "GiaBan";
    public static final String soLuong = "SoLuong";
    public static final String ghiChuHH = "GhiChuHH";
    //Đơn hàng
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
    //Chi tiết Đơn Hàng
    public static final String tenBangChiTietDonHang = "ChiTietDonHang";
    public static final String maChiTietDonHang = "MaChiTietDonHang";
    public static final String maDonHangChiTiet = "MaDonHang";
    public static final String maHangHoaChiTiet = "MaHangHoa";
    public static final String soLuongChiTiet = "SoLuong";
    //Thống Kê
    public static final String tenBangThongKe = "ThongKe";
    public static final String maThongKe = "MaThongKe";
    public static final String ngayBatDau = "NgayBatDau";
    public static final String ngayKetThuc = "NgayKetThuc";
    public static final String tongDoanhThu = "TongDoanhThu";
    public static final String soLuongDonHang = "SoLuongDonHang";


    public DBHelper(Context context) {
        super(context, tenDB, null, 20);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL("create table NguoiDung(" +
                "MaNguoiDung integer primary key autoincrement," +
                "TenNguoiDung text," +
                "EmailNguoiDung text not null," +
                "MatKhau text not null," +
                "GioiTinh text," +
                "NgaySinh text," +
                "SDT text," +
                "DiaChi text)");
        database.execSQL("create table KhachHang(" +
                "MaKhachHang integer primary key autoincrement," +
                "MaNguoiDung integer," +
                "TenKhachHang text ," +
                "SDT text ," +
                "DiaChi text ," +
                "GhiChu text," +
                "FOREIGN KEY(MaNguoiDung) REFERENCES NguoiDung(MaNguoiDung))");
        database.execSQL("create table HangHoa(" +
                "MaHangHoa integer primary key autoincrement," +
                "MaNguoiDung integer," +
                "TenHangHoa text ," +
                "DanhMucHangHoa text, " +
                "GiaNhap real, " +
                "GiaBan real, " +
                "SoLuong integer, " +
                "GhiChuHH text," +
                "FOREIGN KEY(MaNguoiDung) REFERENCES NguoiDung(MaNguoiDung))");;
        database.execSQL("create table DonHang(" +
                "MaDonHang integer primary key autoincrement," +
                "MaNguoiDung integer," +
                "MaKH integer," +
                "MaHH integer," +
                "TenDonHang text," +
                "ThoiGian text," +
                "PhiGiao real," +
                "TrangThai text," +
                "TongTien real," +
                "GhiChu text," +
                "PTThanhToan text," +
                "FOREIGN KEY(MaNguoiDung) REFERENCES NguoiDung(MaNguoiDung)," +
                "FOREIGN KEY(MaKH) REFERENCES KhachHang(MaKhachHang)," +
                "FOREIGN KEY(MaHH) REFERENCES HangHoa(MaHangHoa))");
        database.execSQL("create table ChiTietDonHang(" +
                "MaChiTietDonHang integer primary key autoincrement," +
                "MaDonHang integer," +
                "MaHangHoa integer," +
                "SoLuong integer," +
                "FOREIGN KEY(MaDonHang) REFERENCES DonHang(MaDonHang)," +
                "FOREIGN KEY(MaHangHoa) REFERENCES HangHoa(MaHangHoa))");
        database.execSQL("create table ThongKe(" +
                "MaThongKe integer primary key autoincrement," +
                "MaDH integer,"+
                "NgayBatDau text," +
                "NgayKetThuc text," +
                "TongDoanhThu real," +
                "SoLuongDonHang integer," +
                "FOREIGN KEY(MaDH) REFERENCES DonHang(MaDonHang))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        database.execSQL("drop table if exists NguoiDung");
        database.execSQL("drop table if exists KhachHang");
        database.execSQL("drop table if exists HangHoa");
        database.execSQL("drop table if exists DonHang");
        database.execSQL("drop table if exists ChiTietDonHang");
        database.execSQL("drop table if exists ThongKe");
        onCreate(database);
    }



}
