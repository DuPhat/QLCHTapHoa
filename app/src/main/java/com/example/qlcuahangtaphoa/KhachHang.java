package com.example.qlcuahangtaphoa;

public class KhachHang {
    private int _maKH;
    private String _tenKH;
    private String _email;
    private String _sdt;
    private String _diachi;
    private String _ghichu;



    public int get_maKH() {
        return _maKH;
    }

    public void set_maKH(int _maKH) {
        this._maKH = _maKH;
    }

    public String get_tenKH() {
        return _tenKH;
    }

    public void set_tenKH(String _tenKH) {
        this._tenKH = _tenKH;
    }

    public String get_email() {
        return _email;
    }

    public void set_email(String _email) {
        this._email = _email;
    }

    public String get_sdt() {
        return _sdt;
    }

    public void set_sdt(String _sdt) {
        this._sdt = _sdt;
    }

    public String get_diachi() {
        return _diachi;
    }

    public void set_diachi(String _diachi) {
        this._diachi = _diachi;
    }
    public String get_ghichu() {
        return _ghichu;
    }

    public void set_ghichu(String _ghichu) {
        this._ghichu = _ghichu;
    }

    public KhachHang() {
    }

    public KhachHang(int _maKH, String _tenKH, String _email, String _sdt, String _diachi, String _ghichu) {
        this._maKH = _maKH;
        this._tenKH = _tenKH;
        this._email = _email;
        this._sdt = _sdt;
        this._diachi = _diachi;
        this._ghichu = _ghichu;
    }
}
