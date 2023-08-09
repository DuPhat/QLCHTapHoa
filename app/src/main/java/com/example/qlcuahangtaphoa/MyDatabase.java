package com.example.qlcuahangtaphoa;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class MyDatabase {
    SQLiteDatabase database;
    DBHelper helper;

    public MyDatabase(Context context) {
        helper = new DBHelper(context);
        database = helper.getWritableDatabase();
    }
    //Data KhachHang
    public Cursor getCursorsKH(){
        Cursor cursor = database.rawQuery("Select * from KhachHang", null);
        return cursor;
    }
    public Boolean themKhachHang(KhachHang khachHang){
        ContentValues values = new ContentValues();
        values.put(DBHelper.tenKhachHang, khachHang.get_tenKH());
        values.put(DBHelper.emailKhachHang, khachHang.get_email());
        values.put(DBHelper.sdtKhachHang, khachHang.get_sdt());
        values.put(DBHelper.diaChiKhachHang, khachHang.get_diachi());
        values.put(DBHelper.ghiChu, khachHang.get_ghichu());

        long result = database.insert(DBHelper.tenBangKhachHang, null, values);
        if(result == -1) {
            return false;
        }
        return true;
    }

    public boolean xoaKhachHang(int maKhachHang) {

        database.delete(DBHelper.tenBangKhachHang, DBHelper.maKhachHang + "=?", new String[]{String.valueOf(maKhachHang)});
        return false;
    }

    public  Boolean suaKhachHang(KhachHang khachHang){
        ContentValues values = new ContentValues();
        values.put(DBHelper.tenKhachHang, khachHang.get_tenKH());
        values.put(DBHelper.emailKhachHang, khachHang.get_email());
        values.put(DBHelper.sdtKhachHang, khachHang.get_sdt());
        values.put(DBHelper.diaChiKhachHang, khachHang.get_diachi());
        values.put(DBHelper.ghiChu, khachHang.get_ghichu());

        long result = database.update(DBHelper.tenBangKhachHang, values,DBHelper.maKhachHang + "=" + khachHang.get_maKH(),null);
        if(result == -1) {
            return false;
        }
        return true;
    }
    public ArrayList<KhachHang> getAllKhachHang() {
        ArrayList<KhachHang> khachHangs = new ArrayList<>();
        Cursor cursor = null;

        try {
            cursor = database.rawQuery("SELECT * FROM  " + DBHelper.tenBangKhachHang, null);
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    int maKH = cursor.getInt(cursor.getColumnIndexOrThrow(DBHelper.maKhachHang));
                    String tenKH = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.tenKhachHang));
                    String emailKH = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.emailKhachHang));
                    String sdtKH = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.sdtKhachHang));
                    String diaChiKH = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.diaChiKhachHang));
                    String ghiChu = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.ghiChu));

                    KhachHang khachHang = new KhachHang(maKH, tenKH, emailKH, sdtKH, diaChiKH, ghiChu);
                    khachHangs.add(khachHang);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return khachHangs;
    }
    //Data HangHoa
    public Boolean themHangHoa(HangHoa hangHoa){
        ContentValues values = new ContentValues();
        values.put(DBHelper.tenHangHoa, hangHoa.get_tenHH());
        values.put(DBHelper.danhMucHangHoa, hangHoa.get_danhMucHH());
        values.put(DBHelper.giaBan, hangHoa.get_giaBan());
        values.put(DBHelper.giaNhap, hangHoa.get_giaNhap());
        values.put(DBHelper.soLuong, hangHoa.get_soLuong());
        values.put(DBHelper.ghiChuHH, hangHoa.get_ghiChuHH());

        long result = database.insert(DBHelper.tenBangHangHoa, null, values);
        if(result == -1) {
            return false;
        }

        return true;
    }
    public boolean xoaHangHoa(int maHangHoa) {
        SQLiteDatabase db = this.helper.getWritableDatabase();
        if (!db.isOpen()) {
            db = this.helper.getWritableDatabase();
        }
        int result = db.delete(DBHelper.tenBangHangHoa, DBHelper.maHangHoa + "=?", new String[]{String.valueOf(maHangHoa)});
        return result > 0;
    }
    public  Boolean suaHangHoa(HangHoa hangHoa){

        ContentValues values = new ContentValues();

        values.put(DBHelper.tenHangHoa, hangHoa.get_tenHH());
        values.put(DBHelper.danhMucHangHoa, hangHoa.get_danhMucHH());
        values.put(DBHelper.giaBan, hangHoa.get_giaBan());
        values.put(DBHelper.giaNhap, hangHoa.get_giaNhap());
        values.put(DBHelper.soLuong, hangHoa.get_soLuong());
        values.put(DBHelper.ghiChuHH, hangHoa.get_ghiChuHH());

        long result = database.update(DBHelper.tenBangHangHoa, values,DBHelper.maHangHoa + "=" + hangHoa.get_maHH(),null);
        if(result == -1) {
            return false;
        }
        return true;
    }

    public ArrayList<String> getAllDanhMucHangHoa() {
        ArrayList<String> danhMucList = new ArrayList<>();

        String query = "SELECT " + DBHelper.danhMucHangHoa + " FROM " + DBHelper.tenBangHangHoa;
        Cursor cursor = database.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                String danhMuc = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.danhMucHangHoa));
                danhMucList.add(danhMuc);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return danhMucList;
    }
    public ArrayList<HangHoa> getHangHoaByDanhMuc(String danhMuc) {
        ArrayList<HangHoa> hangHoaList = new ArrayList<>();

        String query = "SELECT * FROM " + DBHelper.tenBangHangHoa + " WHERE " + DBHelper.danhMucHangHoa + " = ?";
        Cursor cursor = database.rawQuery(query, new String[]{danhMuc});

        if (cursor.moveToFirst()) {
            do {
                int maHangHoa = cursor.getInt(cursor.getColumnIndexOrThrow(DBHelper.maHangHoa));
                String tenHangHoa = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.tenHangHoa));
                String danhMucHangHoa = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.danhMucHangHoa));
                double giaNhap = cursor.getDouble(cursor.getColumnIndexOrThrow(DBHelper.giaNhap));
                double giaBan = cursor.getDouble(cursor.getColumnIndexOrThrow(DBHelper.giaBan));
                int soLuong = cursor.getInt(cursor.getColumnIndexOrThrow(DBHelper.soLuong));
                String ghiChuHH = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.ghiChuHH));

                HangHoa hangHoa = new HangHoa(maHangHoa, tenHangHoa, danhMucHangHoa, giaBan, giaNhap, soLuong, ghiChuHH);
                hangHoaList.add(hangHoa);
            } while (cursor.moveToNext());
        }
        cursor.close();

        return hangHoaList;
    }
    //////////////Đơn hàng
    public Boolean insertDonHang(DonHang donHang) {
        SQLiteDatabase database = this.helper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(DBHelper.maKH, donHang.get_maKH());
        values.put(DBHelper.maHH, donHang.get_maHH());
        values.put(DBHelper.tenDonHang, donHang.get_tendh());
        values.put(DBHelper.thoiGian, donHang.get_thoiGian());
        values.put(DBHelper.phiGiao, donHang.get_phiGiao());
        values.put(DBHelper.trangThai, donHang.get_trangThai());
        values.put(DBHelper.tongTien, donHang.get_tongTien());
        values.put(DBHelper.ghiChuDH, donHang.get_ghichu());
        values.put(DBHelper.ptThanhToan, donHang.get_pTThanhToan());

        long result = database.insert(DBHelper.tenBangDonHang, null, values);
        if(result == -1) {
            return false;
        }

        return true;
    }
    // Phương thức trả về danh sách tên KhachHang
    public ArrayList<String> getAllKhachHangNames() {
        ArrayList<String> khachHangNames = new ArrayList<>();


        Cursor cursor = database.rawQuery("SELECT TenKhachHang FROM KhachHang", null);
        if (cursor.moveToFirst()) {
            do {
                String tenKhachHang = cursor.getString(cursor.getColumnIndexOrThrow("TenKhachHang"));
                khachHangNames.add(tenKhachHang);
            } while (cursor.moveToNext());
        }
        cursor.close();

        return khachHangNames;
    }

    // Phương thức trả về danh sách tên HangHoa
    public ArrayList<String> getAllHangHoaNames() {
        ArrayList<String> hangHoaNames = new ArrayList<>();


        Cursor cursor = database.rawQuery("SELECT TenHangHoa FROM HangHoa", null);
        if (cursor.moveToFirst()) {
            do {
                String tenHangHoa = cursor.getString(cursor.getColumnIndexOrThrow("TenHangHoa"));
                hangHoaNames.add(tenHangHoa);
            } while (cursor.moveToNext());
        }
        cursor.close();

        return hangHoaNames;
    }
    public String getTenKhachHang(int maKhachHang) {
        Cursor cursor = database.rawQuery("SELECT " + DBHelper.tenKhachHang + " FROM " + DBHelper.tenBangKhachHang + " WHERE " + DBHelper.maKhachHang + " = ?", new String[]{String.valueOf(maKhachHang)});
        if (cursor != null && cursor.moveToFirst()) {
            String tenKhachHang = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.tenKhachHang));
            cursor.close();
            return tenKhachHang;
        }
        return "";
    }
    public int getMaKhachHangByName(String tenKhachHang) {
        Cursor cursor = database.rawQuery("SELECT " + DBHelper.maKhachHang + " FROM " + DBHelper.tenBangKhachHang + " WHERE " + DBHelper.tenKhachHang + " = ?", new String[]{tenKhachHang});
        if (cursor.moveToFirst()) {
            return cursor.getInt(cursor.getColumnIndexOrThrow(DBHelper.maKhachHang));
        }
        return -1; // Trả về giá trị không hợp lệ nếu không tìm thấy
    }

    public int getMaHangHoaByName(String tenHangHoa) {
        Cursor cursor = database.rawQuery("SELECT " + DBHelper.maHangHoa + " FROM " + DBHelper.tenBangHangHoa + " WHERE " + DBHelper.tenHangHoa + " = ?", new String[]{tenHangHoa});
        if (cursor.moveToFirst()) {
            return cursor.getInt(cursor.getColumnIndexOrThrow(DBHelper.maHangHoa));
        }
        return -1; // Trả về giá trị không hợp lệ nếu không tìm thấy
    }
    public ArrayList<DonHang> getAllDonHang() {
        ArrayList<DonHang> donHangs = new ArrayList<>();
        Cursor cursor = null;

        try {
            cursor = database.rawQuery("SELECT * FROM " + DBHelper.tenBangDonHang, null);
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    int maDH = cursor.getInt(cursor.getColumnIndexOrThrow(DBHelper.maDonHang));
                    int maKH = cursor.getInt(cursor.getColumnIndexOrThrow(DBHelper.maKH));
                    int maHH = cursor.getInt(cursor.getColumnIndexOrThrow(DBHelper.maHH));
                    String tenDH = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.tenDonHang));
                    String thoiGian = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.thoiGian));
                    double phiGiao = cursor.getDouble(cursor.getColumnIndexOrThrow(DBHelper.phiGiao));
                    String trangThai = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.trangThai));
                    double tongTien = cursor.getDouble(cursor.getColumnIndexOrThrow(DBHelper.tongTien));
                    String ghiChu = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.ghiChuDH));
                    String ptThanhToan = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.ptThanhToan));

                    DonHang donHang = new DonHang(maDH, maKH, maHH, tenDH, thoiGian, phiGiao, trangThai, tongTien, ghiChu, ptThanhToan);
                    donHangs.add(donHang);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return donHangs;
    }
    public DonHang getDonHangById(int maDonHang) {
        DonHang donHang = null;
        Cursor cursor = null;

        try {
            cursor = database.rawQuery("SELECT * FROM " + DBHelper.tenBangDonHang + " WHERE " + DBHelper.maDonHang + " = ?", new String[]{String.valueOf(maDonHang)});
            if (cursor != null && cursor.moveToFirst()) {
                int maDH = cursor.getInt(cursor.getColumnIndexOrThrow(DBHelper.maDonHang));
                int maKH = cursor.getInt(cursor.getColumnIndexOrThrow(DBHelper.maKH));
                int maHH = cursor.getInt(cursor.getColumnIndexOrThrow(DBHelper.maHH));
                String tenDH = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.tenDonHang));
                String thoiGian = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.thoiGian));
                double phiGiao = cursor.getDouble(cursor.getColumnIndexOrThrow(DBHelper.phiGiao));
                String trangThai = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.trangThai));
                double tongTien = cursor.getDouble(cursor.getColumnIndexOrThrow(DBHelper.tongTien));
                String ghiChu = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.ghiChuDH));
                String ptThanhToan = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.ptThanhToan));

                donHang = new DonHang(maDH, maKH, maHH, tenDH, thoiGian, phiGiao, trangThai, tongTien, ghiChu, ptThanhToan);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return donHang;
    }
    public Boolean updateDonHang(DonHang donHang) {
        SQLiteDatabase database = this.helper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(DBHelper.maKH, donHang.get_maKH());
        values.put(DBHelper.maHH, donHang.get_maHH());
        values.put(DBHelper.tenDonHang, donHang.get_tendh());
        values.put(DBHelper.thoiGian, donHang.get_thoiGian());
        values.put(DBHelper.phiGiao, donHang.get_phiGiao());
        values.put(DBHelper.trangThai, donHang.get_trangThai());
        values.put(DBHelper.tongTien, donHang.get_tongTien());
        values.put(DBHelper.ghiChuDH, donHang.get_ghichu());
        values.put(DBHelper.ptThanhToan, donHang.get_pTThanhToan());

        long result = database.update(DBHelper.tenBangDonHang, values, DBHelper.maDonHang + "=" + donHang.get_maDH(), null);
        if(result == -1) {
            return false;
        }

        return true;
    }


}
