package com.example.qlcuahangtaphoa.DonHang;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.qlcuahangtaphoa.DBHelper;
import com.example.qlcuahangtaphoa.MyDatabase;
import com.example.qlcuahangtaphoa.R;
import com.example.qlcuahangtaphoa.Singleton.InventoryManager;
import com.example.qlcuahangtaphoa.Singleton.Product;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class DonHangActivity extends AppCompatActivity {
    ImageButton btnBack, btnCalendar, btnAdd;
    TextView tvDate, edtTenDonHang, edtPhiGiao, edtTongTien, edtGhiChuDH;
    Spinner spnTrangThai, spnHH, spnKH, spnPTThanhToan;
    Button btnTimKiem;
    Calendar calendar;
    DBHelper dbHelper;
    ArrayAdapter<String> spinnerAdapter;
    private DonHangAdapter adapter;
    public ListView listView;
    public MyDatabase database;
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


        // Sử dụng InventoryManager để thêm và truy xuất sản phẩm
        InventoryManager inventoryManager = InventoryManager.getInstance();
        inventoryManager.addProduct(new Product("Sữa", 2.5));
        inventoryManager.addProduct(new Product("Bánh quy", 1.0));
        List<Product> products = inventoryManager.getProducts();

        // Hiển thị danh sách sản phẩm lên giao diện
        StringBuilder productList = new StringBuilder();
        for (Product product : products) {
            productList.append(product.getName()).append(" - ").append(product.getPrice()).append(" USD\n");
        }
        TextView productsTextView = findViewById(R.id.productsTextView);
        productsTextView.setText(productList.toString());

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
        adapter = new DonHangAdapter(this, R.layout.layout_item_donhang, donHangList, this);
        listView.setAdapter(adapter);
        setupCalendar();
        updateAppbar();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DonHang selectedDonHang = donHangList.get(position);
                showEditDialog(selectedDonHang);
            }
        });
        // Tìm kiếm đơn hàng
        EditText edtTimKiemDH = findViewById(R.id.edtTimKiemDH);

        // Lắng nghe sự kiện khi người dùng nhấn nút tìm kiếm
        btnTimKiem = findViewById(R.id.btnTimKiemDH);
        btnTimKiem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lấy chuỗi tìm kiếm từ EditText
                String searchQuery = edtTimKiemDH.getText().toString().trim();

                // Nếu chuỗi tìm kiếm không rỗng, thực hiện tìm kiếm
                if (!searchQuery.isEmpty()) {
                    // Xóa danh sách đơn hàng hiện tại
                    donHangList.clear();

                    // Thực hiện tìm kiếm đơn hàng theo tên
                    ArrayList<DonHang> searchResults = database.TimKiemDonHang(searchQuery);

                    // Kiểm tra kết quả tìm kiếm và cập nhật danh sách đơn hàng
                    if (searchResults != null && !searchResults.isEmpty()) {
                        donHangList.addAll(searchResults);
                        adapter.notifyDataSetChanged();
                    } else {
                        // Hiển thị thông báo nếu không tìm thấy kết quả
                        Toast.makeText(DonHangActivity.this, "Không tìm thấy đơn hàng", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Nếu chuỗi tìm kiếm rỗng, hiển thị thông báo
                    Toast.makeText(DonHangActivity.this, "Vui lòng nhập tên đơn hàng cần tìm kiếm", Toast.LENGTH_SHORT).show();
                }
            }
        });
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
                    dialog.dismiss();
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
        ArrayAdapter<String> hhAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, hhList);
        ArrayAdapter<String> khAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, khList);



        spnHH = dialog.findViewById(R.id.spnHH);
        spnKH = dialog.findViewById(R.id.spnKH);

        khAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        hhAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnKH.setAdapter(khAdapter);
        spnHH.setAdapter(hhAdapter);

        // Lấy tên Khách Hàng và Hàng Hóa từ mã
        String tenKhachHang = database.getTenKhachHang(donHang.get_maKH());
        String tenHangHoa = database.getTenHangHoa(donHang.get_maHH());

        // Đặt vị trí cho Spinner Khách Hàng và Hàng Hóa
        int khPosition = khAdapter.getPosition(tenKhachHang);
        int hhPosition = hhAdapter.getPosition(tenHangHoa);
        spnKH.setSelection(khPosition);
        spnHH.setSelection(hhPosition);

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
    public void showDetailDialog(DonHang donHang) {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_dialog_info_donhang);
        Window window = dialog.getWindow();
        if (window == null) {
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = Gravity.CENTER;
        window.setAttributes(windowAttributes);
        TextView tvTenDH = dialog.findViewById(R.id.tvTenDH);
        tvDate = dialog.findViewById(R.id.tvDate);
        TextView tvPhiGiao = dialog.findViewById(R.id.tvPhiGiao);
        TextView tvTrangThai = dialog.findViewById(R.id.tvTrangThai);
        TextView tvHH = dialog.findViewById(R.id.tvHH);
        TextView tvKH = dialog.findViewById(R.id.tvKH);
        TextView tvGhiChuDH = dialog.findViewById(R.id.tvGhiChuDH);
        TextView tvTongTien = dialog.findViewById(R.id.tvTongTien);
        TextView tvThanhToan = dialog.findViewById(R.id.tvThanhToan);
        // Hiển thị thông tin chi tiết đơn hàng
        // Lấy tên Khách Hàng và Hàng Hóa từ danh sách khList và hhList dựa trên mã Khách Hàng và mã Hàng Hóa
        String tenKhachHang = database.getTenKhachHang(donHang.get_maKH());
        String tenHangHoa = database.getTenHangHoa(donHang.get_maHH());

        tvTenDH.setText(donHang.get_tendh());
        tvDate.setText(donHang.get_thoiGian());
        tvPhiGiao.setText(String.valueOf(donHang.get_phiGiao()));
        tvTrangThai.setText(donHang.get_trangThai());
        tvHH.setText(tenHangHoa);
        tvKH.setText(tenKhachHang);
        tvGhiChuDH.setText(donHang.get_ghichu());
        tvTongTien.setText(String.valueOf(donHang.get_tongTien()));
        tvThanhToan.setText(donHang.get_pTThanhToan());

        // Xử lý sự kiện đóng dialog khi nhấn nút Hủy
        Button btnHuy = dialog.findViewById(R.id.btnHuy);
        btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }
}