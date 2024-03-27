package com.example.qlcuahangtaphoa;

public class ThongKe {
    private int maThongKe;
    private int maDonHang;
    private String ngayBatDau;
    private String ngayKetThuc;
    private double tongDoanhThu;
    private int soLuongDonHang;

    public ThongKe() {}
    public ThongKe(int maDonHang, String ngayBatDau, String ngayKetThuc, double tongDoanhThu, int soLuongDonHang) {
        this.maDonHang = maDonHang;
        this.ngayBatDau = ngayBatDau;
        this.ngayKetThuc = ngayKetThuc;
        this.tongDoanhThu = tongDoanhThu;
        this.soLuongDonHang = soLuongDonHang;
    }

    // Các phương thức getter và setter
    public int getMaThongKe() {
        return maThongKe;
    }

    public void setMaThongKe(int maThongKe) {
        this.maThongKe = maThongKe;
    }

    public int getMaDonHang() {
        return maDonHang;
    }

    public void setMaDonHang(int maDonHang) {
        this.maDonHang = maDonHang;
    }

    public String getNgayBatDau() {
        return ngayBatDau;
    }

    public void setNgayBatDau(String ngayBatDau) {
        this.ngayBatDau = ngayBatDau;
    }

    public String getNgayKetThuc() {
        return ngayKetThuc;
    }

    public void setNgayKetThuc(String ngayKetThuc) {
        this.ngayKetThuc = ngayKetThuc;
    }

    public double getTongDoanhThu() {
        return tongDoanhThu;
    }

    public void setTongDoanhThu(double tongDoanhThu) {
        this.tongDoanhThu = tongDoanhThu;
    }

    public int getSoLuongDonHang() {
        return soLuongDonHang;
    }

    public void setSoLuongDonHang(int soLuongDonHang) {
        this.soLuongDonHang = soLuongDonHang;
    }
}
