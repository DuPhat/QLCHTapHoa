package com.example.qlcuahangtaphoa;

public class HangHoa {
    private int _maHH;
    private String _tenHH;
    private String _danhMucHH;
    private double _giaBan;
    private double _giaNhap;
    private int _soLuong;
    private String _ghiChuHH;

    public HangHoa() {
    }

    public HangHoa(int _maHH, String _tenHH, String _danhMucHH, double _giaBan, double _giaNhap, int _soLuong, String _ghiChuHH) {
        this._maHH = _maHH;
        this._tenHH = _tenHH;
        this._danhMucHH = _danhMucHH;
        this._giaBan = _giaBan;
        this._giaNhap = _giaNhap;
        this._soLuong = _soLuong;
        this._ghiChuHH = _ghiChuHH;
    }

    public int get_maHH() {
        return _maHH;
    }

    public void set_maHH(int _maHH) {
        this._maHH = _maHH;
    }

    public String get_tenHH() {
        return _tenHH;
    }

    public void set_tenHH(String _tenHH) {
        this._tenHH = _tenHH;
    }

    public String get_danhMucHH() {
        return _danhMucHH;
    }

    public void set_danhMucHH(String _danhMucHH) {
        this._danhMucHH = _danhMucHH;
    }

    public double get_giaBan() {
        return _giaBan;
    }

    public void set_giaBan(double _giaBan) {
        this._giaBan = _giaBan;
    }

    public double get_giaNhap() {
        return _giaNhap;
    }

    public void set_giaNhap(double _giaNhap) {
        this._giaNhap = _giaNhap;
    }

    public int get_soLuong() {
        return _soLuong;
    }

    public void set_soLuong(int _soLuong) {
        this._soLuong = _soLuong;
    }

    public String get_ghiChuHH() {
        return _ghiChuHH;
    }

    public void set_ghiChuHH(String _ghiChuHH) {
        this._ghiChuHH = _ghiChuHH;
    }



}
