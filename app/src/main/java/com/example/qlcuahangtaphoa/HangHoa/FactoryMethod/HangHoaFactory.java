package com.example.qlcuahangtaphoa.HangHoa.FactoryMethod;

import com.example.qlcuahangtaphoa.HangHoa.HangHoa;

public interface HangHoaFactory {
    HangHoa createHangHoa(String tenHangHoa, String danhMucHangHoa, double giaBan, double giaNhap, int soLuong, String ghiChuHH);
}

