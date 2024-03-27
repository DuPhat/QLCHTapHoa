package com.example.qlcuahangtaphoa;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.qlcuahangtaphoa.DonHang.DonHang;
import com.example.qlcuahangtaphoa.HangHoa.HangHoa;
import com.example.qlcuahangtaphoa.HangHoa.FactoryMethod.HangHoaFactory;
import com.example.qlcuahangtaphoa.HangHoa.FactoryMethod.SimpleHangHoaFactory;
import com.example.qlcuahangtaphoa.KhanhHang.KhachHang;

import java.util.ArrayList;

public class MyDatabase {
    SQLiteDatabase database;
    DBHelper helper;
    private static MyDatabase instance;

    public MyDatabase(Context context) {
        helper = new DBHelper(context);
        database = helper.getWritableDatabase();
    }
    // Database User
    public User checkEmailPassword(String email, String password){
        User user = new User();
        Cursor cursor = database.rawQuery("select * from NguoiDung where EmailNguoiDung=? and MatKhau=?", new String[]{email, password});
        if(cursor.getCount() > 0){
            cursor.moveToFirst();
            user.set_id(cursor.getInt(0));
            user.set_ten(cursor.getString(1));
            user.set_email(cursor.getString(2));
            user.set_matkhau(cursor.getString(3));
            user.set_gioitinh(cursor.getString(4));
            user.set_ngaysinh(cursor.getString(5));
            user.set_sdt(cursor.getString(6));
            user.set_diachi(cursor.getString(7));
        }
        else {
            return null;
        }
        return user;
    }

    public Boolean insertUser(User user){
        ContentValues values = new ContentValues();

        values.put(DBHelper.emailNguoiDung, user.get_email());
        values.put(DBHelper.matKhauNguoiDung, user.get_matkhau());

        long result = database.insert(DBHelper.tenBangNguoiDung, null, values);

        if(result == -1){
            return false;
        }
        return true;
    }
    public Boolean updateUser(User user){
        ContentValues values = new ContentValues();
        values.put(DBHelper.tenNguoiDung, user.get_ten());
        values.put(DBHelper.emailNguoiDung, user.get_email());
        values.put(DBHelper.matKhauNguoiDung, user.get_matkhau());
        values.put(DBHelper.ngaySinhNguoiDung, user.get_ngaysinh());
        values.put(DBHelper.gioiTinhNguoiDung, user.get_gioitinh());
        values.put(DBHelper.sdtNguoiDung, user.get_sdt());
        values.put(DBHelper.diaChiNguoiDung, user.get_diachi());

        long result = database.update(DBHelper.tenBangNguoiDung, values, DBHelper.maNguoiDung + "=" +user.get_id(), null);
        if(result == -1){
            return false;
        }
        return true;
    }

    public Boolean checkEmail(String email){
        Cursor cursor = database.rawQuery("select * from NguoiDung where EmailNguoiDung=?", new String[]{email});
        if(cursor.getCount() > 0)
            return true;
        return false;
    }
    // Khach Hang
    public Boolean themKhachHang(KhachHang khachHang){
        ContentValues values = new ContentValues();
        values.put(DBHelper.tenKhachHang, khachHang.get_tenKH());
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
                    String sdtKH = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.sdtKhachHang));
                    String diaChiKH = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.diaChiKhachHang));
                    String ghiChu = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.ghiChu));

                    KhachHang khachHang = new KhachHang(maKH, tenKH, sdtKH, diaChiKH, ghiChu);
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
    public String getTenKhachHang(int maKhachHang) {
        Cursor cursor = database.rawQuery("SELECT " + DBHelper.tenKhachHang + " FROM " + DBHelper.tenBangKhachHang + " WHERE " + DBHelper.maKhachHang + " = ?", new String[]{String.valueOf(maKhachHang)});
        if (cursor != null && cursor.moveToFirst()) {
            String tenKhachHang = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.tenKhachHang));
            cursor.close();
            return tenKhachHang;
        }
        return "";
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
    public int getMaKhachHangByName(String tenKhachHang) {
        Cursor cursor = database.rawQuery("SELECT " + DBHelper.maKhachHang + " FROM " + DBHelper.tenBangKhachHang + " WHERE " + DBHelper.tenKhachHang + " = ?", new String[]{tenKhachHang});
        if (cursor.moveToFirst()) {
            return cursor.getInt(cursor.getColumnIndexOrThrow(DBHelper.maKhachHang));
        }
        return -1; // Trả về giá trị không hợp lệ nếu không tìm thấy
    }
    //Hàng Hóa Factory Method
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

    // Sử dụng factory method để tạo sản phẩm
    public Boolean themHangHoa(String tenHangHoa, String danhMucHangHoa, double giaBan, double giaNhap, int soLuong, String ghiChuHH) {
        HangHoaFactory factory = new SimpleHangHoaFactory();
        HangHoa hangHoa = factory.createHangHoa(tenHangHoa, danhMucHangHoa, giaBan, giaNhap, soLuong, ghiChuHH);
        return themHangHoa(hangHoa);
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

    public String getTenHangHoa(int maHangHoa) {
        Cursor cursor = database.rawQuery("SELECT " + DBHelper.tenHangHoa + " FROM " + DBHelper.tenBangHangHoa + " WHERE " + DBHelper.maHangHoa + " = ?", new String[]{String.valueOf(maHangHoa)});
        if (cursor != null && cursor.moveToFirst()) {
            String tenHangHoa = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.tenHangHoa));
            cursor.close();
            return tenHangHoa;
        }
        return "";
    }


    public int getMaHangHoaByName(String tenHangHoa) {
        Cursor cursor = database.rawQuery("SELECT " + DBHelper.maHangHoa + " FROM " + DBHelper.tenBangHangHoa + " WHERE " + DBHelper.tenHangHoa + " = ?", new String[]{tenHangHoa});
        if (cursor.moveToFirst()) {
            return cursor.getInt(cursor.getColumnIndexOrThrow(DBHelper.maHangHoa));
        }
        return -1; // Trả về giá trị không hợp lệ nếu không tìm thấy
    }
    // Phương thức tìm kiếm hàng hóa theo tên
    public ArrayList<HangHoa> TiemKiemHangHoa(String tenHH) {
        ArrayList<HangHoa> results = new ArrayList<>();

        SQLiteDatabase db = this.helper.getWritableDatabase();
        Cursor cursor = null;

        try {
            // Truy vấn cơ sở dữ liệu để tìm kiếm hàng hóa theo tên
            cursor = db.rawQuery("SELECT * FROM " + DBHelper.tenBangHangHoa + " WHERE " + DBHelper.tenHangHoa + " LIKE '%" + tenHH + "%'", null);

            // Đọc dữ liệu từ con trỏ và thêm vào danh sách kết quả
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    int maHangHoa = cursor.getInt(cursor.getColumnIndexOrThrow(DBHelper.maHangHoa));
                    String tenHangHoa = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.tenHangHoa));
                    String danhMucHangHoa = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.danhMucHangHoa));
                    double giaNhap = cursor.getDouble(cursor.getColumnIndexOrThrow(DBHelper.giaNhap));
                    double giaBan = cursor.getDouble(cursor.getColumnIndexOrThrow(DBHelper.giaBan));
                    int soLuong = cursor.getInt(cursor.getColumnIndexOrThrow(DBHelper.soLuong));
                    String ghiChuHH = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.ghiChuHH));

                    HangHoa hangHoa = new HangHoa(maHangHoa, tenHangHoa, danhMucHangHoa, giaBan, giaNhap, soLuong, ghiChuHH);
                    results.add(hangHoa);
                } while (cursor.moveToNext());
            }
        } catch (SQLException e) {
            Log.e("MyDatabase", "Error while searching for HangHoa: " + e.getMessage());
        } finally {
            // Đóng con trỏ và đóng kết nối cơ sở dữ liệu
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }
        return results;
    }
    //Đơn hàng
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
    // Phương thức xóa đơn hàng
    public boolean deleteDonHang(int maDonHang) {
        SQLiteDatabase db = this.helper.getWritableDatabase();
        if (!db.isOpen()) {
            db = this.helper.getWritableDatabase();
        }
        int result = db.delete(DBHelper.tenBangDonHang, DBHelper.maDonHang + "=?", new String[]{String.valueOf(maDonHang)});
        return result > 0;
    }
    // Phương thức tìm kiếm đơn hàng theo tên
    public ArrayList<DonHang> TimKiemDonHang(String tenDH) {
        ArrayList<DonHang> resultList = new ArrayList<>();
        SQLiteDatabase db = helper.getReadableDatabase();

        String query = "SELECT * FROM " + DBHelper.tenBangDonHang + " WHERE " + DBHelper.tenDonHang + " LIKE '%" + tenDH + "%'";
        Cursor cursor = db.rawQuery(query, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int maDH = cursor.getInt(cursor.getColumnIndexOrThrow(DBHelper.maDonHang));
                int maKH = cursor.getInt(cursor.getColumnIndexOrThrow(DBHelper.maKH));
                int maHH = cursor.getInt(cursor.getColumnIndexOrThrow(DBHelper.maHH));
                String tenDonHang = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.tenDonHang));
                String thoiGian = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.thoiGian));
                double phiGiao = cursor.getDouble(cursor.getColumnIndexOrThrow(DBHelper.phiGiao));
                String trangThai = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.trangThai));
                double tongTien = cursor.getDouble(cursor.getColumnIndexOrThrow(DBHelper.tongTien));
                String ghiChu = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.ghiChuDH));
                String ptThanhToan = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.ptThanhToan));

                // Tạo đối tượng DonHang và thêm vào danh sách kết quả
                DonHang donHang = new DonHang(maDH, maKH, maHH, tenDonHang, thoiGian, phiGiao, trangThai, tongTien, ghiChu, ptThanhToan);
                resultList.add(donHang);
            } while (cursor.moveToNext());
        }

        // Đóng kết nối và trả về danh sách kết quả
        if (cursor != null) {
            cursor.close();
        }
        db.close();
        return resultList;
    }
    public boolean capNhatThongKe(ThongKe thongKe) {
        SQLiteDatabase database = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBHelper.maThongKe, thongKe.getMaThongKe());
        values.put(DBHelper.ngayBatDau, thongKe.getNgayBatDau());
        values.put(DBHelper.ngayKetThuc, thongKe.getNgayKetThuc());
        values.put(DBHelper.tongDoanhThu, thongKe.getTongDoanhThu());
        values.put(DBHelper.soLuongDonHang, thongKe.getSoLuongDonHang());

        long result = database.insertWithOnConflict(DBHelper.tenBangThongKe, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        return result != -1;
    }

    public static synchronized MyDatabase getInstance(Context context) {
        if (instance == null) {
            instance = new MyDatabase(context.getApplicationContext());
        }
        return instance;
    }
}
