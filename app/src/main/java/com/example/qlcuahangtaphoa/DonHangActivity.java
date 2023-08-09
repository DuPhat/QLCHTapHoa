package com.example.qlcuahangtaphoa;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class DonHangActivity extends AppCompatActivity {
    ImageButton btnBack, btnCalendar, btnAdd;
    TextView tvDate, edtTenDonHang, edtPhiGiao, edtTongTien, edtGhiChuDH;
    Spinner spnTrangThai, spnHH, spnKH, spnPTThanhToan;
    Calendar calendar;
    DBHelper dbHelper;
    ArrayAdapter<String> spinnerAdapter;
    private DonHangAdapter adapter;
    public ListView listView;
    public  MyDatabase database;
    Dialog dialog;
    private static final int id = -1;
    // Create ArrayLists to hold data for Spinners
    private ArrayList<String> trangThaiList = new ArrayList<>();
    private ArrayList<String> hhList = new ArrayList<>();
    private ArrayList<String> khList = new ArrayList<>();
    private ArrayList<String> ptThanhToanList = new ArrayList<>();
    private ArrayList<DonHang> donHangList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_don_hang);
        btnBack = findViewById(R.id.btnBack);
        database = new MyDatabase(this);
        // Gọi và sử dụng phương thức getAllDonHang()
        ArrayList<DonHang> donHangList = database.getAllDonHang();
        // Ở đây, bạn có danh sách các đơn hàng và có thể thực hiện các xử lý với chúng.

        // Ví dụ: In thông tin của từng đơn hàng trong danh sách
        for (DonHang donHang : donHangList) {
            Log.d("DonHang", "Tên đơn hàng: " + donHang.get_tendh());
            // ... Các thông tin khác tương tự
        }

        // Gọi và sử dụng phương thức getDonHangById()
        int maDonHang = 1; // Thay thế bằng mã đơn hàng bạn muốn tìm
        DonHang donHangById = database.getDonHangById(maDonHang);
        if (donHangById != null) {
            Log.d("DonHangById", "Tên đơn hàng: " + donHangById.get_tendh());
            // ... Các thông tin khác tương tự
        } else {
            Log.d("DonHangById", "Không tìm thấy đơn hàng với mã " + maDonHang);
        }
        trangThaiList.add("Hoàn thành");
        trangThaiList.add("Đang chờ");

        ptThanhToanList.add("Tiền mặt");
        ptThanhToanList.add("Chuyển khoản ngân hàng");

        khList = database.getAllKhachHangNames(); // Replace with your actual method to get KhachHang data
        hhList = database.getAllHangHoaNames();

        showDonHangDialog();
        listView = findViewById(R.id.lvDH);
        adapter = new DonHangAdapter(this, R.layout.layout_item_donhang, donHangList);
        listView.setAdapter(adapter);
        setupCalendar();
        updateAppbar();

    }
    public void updateAppbar(){
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    public void setupCalendar() {

        calendar = Calendar.getInstance();
        updateCalendar();
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                updateCalendar();

            }
        };

        btnCalendar = dialog.findViewById(R.id.btnCalendar);
        btnCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog dpd = new DatePickerDialog(DonHangActivity.this, date, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH));
                dpd.show();
            }
        });
    }

    private void updateCalendar() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-20yy", Locale.getDefault());
        TextView tvDate = dialog.findViewById(R.id.tvDate);
        tvDate.setText(sdf.format(calendar.getTime()));
    }


    public void showDonHangDialog(){
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_dialog_insert_donhang);

        Window window = dialog.getWindow();
        if(window == null){
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = Gravity.CENTER;
        window.setAttributes(windowAttributes);

        // Khởi tạo các Spinner và thiết lập adapter tại đây
        spnTrangThai = dialog.findViewById(R.id.spnTrangThai);
        spnHH = dialog.findViewById(R.id.spnHH);
        spnKH = dialog.findViewById(R.id.spnKH);
        spnPTThanhToan = dialog.findViewById(R.id.spnThanhToan);
        ArrayAdapter<String> trangThaiAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, trangThaiList);
        ArrayAdapter<String> hhAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, hhList);
        ArrayAdapter<String> khAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, khList);
        ArrayAdapter<String> ptThanhToanAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, ptThanhToanList);

        trangThaiAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        hhAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        khAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ptThanhToanAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spnTrangThai.setAdapter(trangThaiAdapter);
        spnHH.setAdapter(hhAdapter);
        spnKH.setAdapter(khAdapter);
        spnPTThanhToan.setAdapter(ptThanhToanAdapter);
        setupCalendar();

    }


    public void btnInsertDH(View view) {
        edtTenDonHang = dialog.findViewById(R.id.edtTenDH);
        tvDate = dialog.findViewById(R.id.tvDate);
        edtPhiGiao = dialog.findViewById(R.id.edtPhiGiao);
        edtTongTien = dialog.findViewById(R.id.edtTongTien);
        edtGhiChuDH = dialog.findViewById(R.id.edtGhiChuDH);
        spnTrangThai = dialog.findViewById(R.id.spnTrangThai);
        spnHH = dialog.findViewById(R.id.spnHH);
        spnKH = dialog.findViewById(R.id.spnKH);
        spnPTThanhToan = dialog.findViewById(R.id.spnThanhToan);

        Button btnThemDH = dialog.findViewById(R.id.btnThemDH);
        btnThemDH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lấy tên Khách Hàng và Hàng Hóa từ Spinner
                String tenKhachHang = spnKH.getSelectedItem().toString();
                String tenHangHoa = spnHH.getSelectedItem().toString();

                // Tìm mã Khách Hàng và Hàng Hóa từ tên
                int maKH = database.getMaKhachHangByName(tenKhachHang);
                int maHH = database.getMaHangHoaByName(tenHangHoa);
                String tenDH = edtTenDonHang.getText().toString();
                String thoiGian = tvDate.getText().toString();
                Double phiGiao = Double.valueOf(edtPhiGiao.getText().toString());
                Double tongTien = Double.valueOf(edtTongTien.getText().toString());
                String ghiChuDH = edtGhiChuDH.getText().toString();
                String trangThai = spnTrangThai.getSelectedItem().toString();
                String thanhToan = spnPTThanhToan.getSelectedItem().toString();

                DonHang donHang = new DonHang(id, maKH, maHH, tenDH, thoiGian, phiGiao, trangThai, tongTien, ghiChuDH, thanhToan);
                boolean insertDonHang = database.insertDonHang(donHang);
                if (insertDonHang) {
                    Toast.makeText(DonHangActivity.this, "Thêm đơn hàng thành công", Toast.LENGTH_SHORT).show();
                    // Cập nhật danh sách donHangList
                    DonHang newDonHang = new DonHang(id, maKH, maHH, tenDH, thoiGian, phiGiao, trangThai, tongTien, ghiChuDH, thanhToan);
                    donHangList.add(newDonHang);
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(DonHangActivity.this, "Thêm đơn hàng thất bại", Toast.LENGTH_SHORT).show();
                }

            }
        });

        Button btnHuy = dialog.findViewById(R.id.btnHuy);
        btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss(); // Tắt dialog khi nhấn nút đóng
            }
        });
        dialog.show();
    }
    //--------Edit DonHang
    // Phương thức để hiển thị hộp thoại chỉnh sửa thông tin đơn hàng
    public void showEditDialog(DonHang donHang) {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_dialog_edit_donhang);

        Window window = dialog.getWindow();
        if (window == null) {
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = Gravity.CENTER;
        window.setAttributes(windowAttributes);

        // Khởi tạo các thành phần trong dialogView
        edtTenDonHang = dialog.findViewById(R.id.edtTenDH);
        tvDate = dialog.findViewById(R.id.tvDate);
        edtPhiGiao = dialog.findViewById(R.id.edtPhiGiao);
        edtTongTien = dialog.findViewById(R.id.edtTongTien);
        edtGhiChuDH = dialog.findViewById(R.id.edtGhiChuDH);
        spnTrangThai = dialog.findViewById(R.id.spnTrangThai);
        spnPTThanhToan = dialog.findViewById(R.id.spnThanhToan);

        // Đặt giá trị ban đầu cho các thành phần trong dialogView
        edtTenDonHang.setText(donHang.get_tendh());
        tvDate.setText(donHang.get_thoiGian());
        edtPhiGiao.setText(String.valueOf(donHang.get_phiGiao()));
        edtTongTien.setText(String.valueOf(donHang.get_tongTien()));
        edtGhiChuDH.setText(donHang.get_ghichu());

        ArrayAdapter<String> trangThaiAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, trangThaiList);
        ArrayAdapter<String> ptThanhToanAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, ptThanhToanList);

        trangThaiAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ptThanhToanAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spnTrangThai.setAdapter(trangThaiAdapter);
        spnPTThanhToan.setAdapter(ptThanhToanAdapter);

        // Đặt giá trị ban đầu cho Spinner Trạng thái và Phương thức thanh toán
        int trangThaiPosition = trangThaiAdapter.getPosition(donHang.get_trangThai());
        int ptThanhToanPosition = ptThanhToanAdapter.getPosition(donHang.get_pTThanhToan());
        spnTrangThai.setSelection(trangThaiPosition);
        spnPTThanhToan.setSelection(ptThanhToanPosition);

        // Xử lý sự kiện khi nhấn nút Cập nhật
        Button btnCapNhatDH = dialog.findViewById(R.id.btnCapNhatDH);
        btnCapNhatDH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lấy thông tin từ các thành phần trong dialogView
                String tenDH = edtTenDonHang.getText().toString();
                String thoiGian = tvDate.getText().toString();
                Double phiGiao = Double.valueOf(edtPhiGiao.getText().toString());
                Double tongTien = Double.valueOf(edtTongTien.getText().toString());
                String ghiChuDH = edtGhiChuDH.getText().toString();
                String trangThai = spnTrangThai.getSelectedItem().toString();
                String ptthanhToan = spnPTThanhToan.getSelectedItem().toString();

                // Gọi phương thức cập nhật đơn hàng trong MyDatabase
                boolean updateResult = database.updateDonHang(new DonHang(
                        donHang.get_maDH(), donHang.get_maKH(), donHang.get_maHH(),
                        tenDH, thoiGian, phiGiao, trangThai, tongTien, ghiChuDH, ptthanhToan)
                );

                if (updateResult) {
                    Toast.makeText(DonHangActivity.this, "Cập nhật đơn hàng thành công", Toast.LENGTH_SHORT).show();
                    // Cập nhật lại danh sách donHangList
                    donHang.set_tendh(tenDH);
                    donHang.set_thoiGian(thoiGian);
                    donHang.set_phiGiao(phiGiao);
                    donHang.set_tongTien(tongTien);
                    donHang.set_ghichu(ghiChuDH);
                    donHang.set_trangThai(trangThai);
                    donHang.set_pTThanhToan(ptthanhToan);
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(DonHangActivity.this, "Cập nhật đơn hàng thất bại", Toast.LENGTH_SHORT).show();
                }
                adapter.notifyDataSetChanged();
                dialog.dismiss(); // Đóng dialog sau khi cập nhật
            }
        });

        // Xử lý sự kiện khi nhấn nút Hủy
        Button btnHuy = dialog.findViewById(R.id.btnHuy);
        btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss(); // Tắt dialog khi nhấn nút Hủy
            }
        });

        dialog.show(); // Hiển thị dialog

    }
}