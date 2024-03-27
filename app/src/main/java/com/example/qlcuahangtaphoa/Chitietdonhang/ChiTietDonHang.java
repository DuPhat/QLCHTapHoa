package com.example.qlcuahangtaphoa.Chitietdonhang;

public class ChiTietDonHang {
    private int maChiTietDonHang;
    private int maDonHang;
    private int maHangHoa;
    private int soLuong;

    public ChiTietDonHang() {
    }

    public ChiTietDonHang(int maChiTietDonHang, int maDonHang, int maHangHoa, int soLuong) {
        this.maChiTietDonHang = maChiTietDonHang;
        this.maDonHang = maDonHang;
        this.maHangHoa = maHangHoa;
        this.soLuong = soLuong;
    }

    public int getMaChiTietDonHang() {
        return maChiTietDonHang;
    }

    public void setMaChiTietDonHang(int maChiTietDonHang) {
        this.maChiTietDonHang = maChiTietDonHang;
    }

    public int getMaDonHang() {
        return maDonHang;
    }

    public void setMaDonHang(int maDonHang) {
        this.maDonHang = maDonHang;
    }

    public int getMaHangHoa() {
        return maHangHoa;
    }

    public void setMaHangHoa(int maHangHoa) {
        this.maHangHoa = maHangHoa;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }
}
