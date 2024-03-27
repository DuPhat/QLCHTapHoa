package com.example.qlcuahangtaphoa;

import java.util.ArrayList;

public class User {
    private int _id;
    private String _ten;
    private String _email;
    private String _matkhau;
    private String _gioitinh;
    private String _ngaysinh;
    private String _sdt;
    private String _diachi;


    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String get_ten() {
        return _ten;
    }

    public void set_ten(String _ten) {
        this._ten = _ten;
    }

    public String get_email() {
        return _email;
    }

    public void set_email(String _email) {
        this._email = _email;
    }

    public String get_matkhau() {
        return _matkhau;
    }

    public void set_matkhau(String _matkhau) {
        this._matkhau = _matkhau;
    }

    public String get_gioitinh() {
        return _gioitinh;
    }

    public void set_gioitinh(String _gioitinh) {
        this._gioitinh = _gioitinh;
    }

    public String get_ngaysinh() {
        return _ngaysinh;
    }

    public void set_ngaysinh(String _ngaysinh) {
        this._ngaysinh = _ngaysinh;
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

    public String convertGioiTinh(long id){
        String sex = "";
        if(id == 0)
        {
            sex = "Nam";
        }
        else
        {
            sex = "Nữ";
        }
        return sex;
    }

    public ArrayList<String> convertUserToArray(){
        ArrayList<String> data = new ArrayList<String>();

        data.add(Integer.toString(_id));
        data.add(_ten);
        data.add(_email);
        data.add(_matkhau);
        data.add(_gioitinh);
        data.add(_ngaysinh);
        data.add(_sdt);
        data.add(_diachi);

        return data;
    }

    public void convertArrayToUser(ArrayList<String> data){
        _id = Integer.parseInt(data.get(0));
        _ten = data.get(1);
        _email = data.get(2);
        _matkhau = data.get(3);
        _gioitinh = data.get(4);
        _ngaysinh = data.get(5);
        _sdt = data.get(6);
        _diachi = data.get(7);
    }
}
