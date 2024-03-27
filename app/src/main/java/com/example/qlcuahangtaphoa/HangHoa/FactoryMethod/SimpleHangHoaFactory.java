package com.example.qlcuahangtaphoa.HangHoa.FactoryMethod;

import com.example.qlcuahangtaphoa.HangHoa.HangHoa;

public class SimpleHangHoaFactory implements HangHoaFactory {
    @Override
    public HangHoa createHangHoa(String tenHangHoa, String danhMucHangHoa, double giaBan, double giaNhap, int soLuong, String ghiChuHH) {
        return new HangHoa(tenHangHoa, danhMucHangHoa, giaBan, giaNhap, soLuong, ghiChuHH);
    }
}
