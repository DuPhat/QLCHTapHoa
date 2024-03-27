    package com.example.qlcuahangtaphoa.HangHoa;

    import android.app.Dialog;
    import android.os.Bundle;
    import android.view.Gravity;
    import android.view.View;
    import android.view.Window;
    import android.view.WindowManager;
    import android.widget.AdapterView;
    import android.widget.ArrayAdapter;
    import android.widget.Button;
    import android.widget.EditText;
    import android.widget.ImageButton;
    import android.widget.ListView;
    import android.widget.Spinner;
    import android.widget.TextView;
    import android.widget.Toast;

    import androidx.appcompat.app.AppCompatActivity;

    import com.example.qlcuahangtaphoa.MyDatabase;
    import com.example.qlcuahangtaphoa.R;
    import com.example.qlcuahangtaphoa.Singleton.InventoryManager;
    import com.example.qlcuahangtaphoa.Singleton.Product;

    import java.util.ArrayList;
    import java.util.Collections;
    import java.util.Comparator;
    import java.util.List;

    public class HangHoaActivity extends AppCompatActivity {

        ImageButton btnBack, btnAdd, btnMenu;
        private EditText edtTenHH, edtGiaNhap, edtGiaBan, edtSoLuong, edtGhiChuHH;
        private Spinner spnHH;
        ArrayAdapter<String> spinnerAdapter;
        public ListView listView;
        public MyDatabase database;
        public static ArrayList<HangHoa> hangHoaArrayList;
        HangHoaAdapter adapter;
        private static final int id = -1;

        Dialog dialog;
        private int SORT_BY_NAME;
        private int SORT_BY_DATE;
        private int SORT_BY_TOTAL;
        private HangHoaActivity donHangAdapter;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_hang_hoa);
            btnBack =(ImageButton) findViewById(R.id.btnBack);
            btnAdd = (ImageButton) findViewById(R.id.btnAdd);
            btnMenu = (ImageButton) findViewById(R.id.btnMenu);

            spnHH = findViewById(R.id.spnHH);

            // Khởi tạo database và danh sách hàng hóa
            database = new MyDatabase(this);
            hangHoaArrayList = new ArrayList<>();

            spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, new ArrayList<>());
            // Gán adapter cho Spinner
            spnHH.setAdapter(spinnerAdapter);
            // Cập nhật danh sách hàng hóa và danh mục hàng hóa
            loadDanhMucHangHoaData(spnHH); // Gọi trước khi đặt Adapter cho Spinner
            // Gán adapter cho listView
            listView = findViewById(R.id.lvHH);
            adapter = new HangHoaAdapter(HangHoaActivity.this, R.layout.layout_item_hanghoa, hangHoaArrayList, listView);
            listView.setAdapter(adapter);

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


            // Cập nhật danh sách hàng hóa khi người dùng chọn một danh mục hàng hóa từ Spinner
            spnHH.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                    // Load dữ liệu hàng hóa dựa trên danh mục được chọn
                    loadHangHoaData();
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                    Toast.makeText(HangHoaActivity.this, "Bạn chưa chọn hàng hóa", Toast.LENGTH_SHORT).show();
                }
            });
            // Gán adapter cho listView và thiết lập lắng nghe sự kiện sửa hàng hóa
            adapter = new HangHoaAdapter(HangHoaActivity.this, R.layout.layout_item_hanghoa, hangHoaArrayList, listView);
            adapter.setOnEditHangHoaListener(new HangHoaAdapter.OnEditHangHoaListener() {
                @Override
                public void onEditHangHoa(HangHoa hangHoa) {
                    showEditDialog(hangHoa);
                }
            });
            listView.setAdapter(adapter);

            //Tìm Kiếm Hàng Hóa
            EditText edtTimKiemHH = findViewById(R.id.edtTimKiemHH);
            Button btnTimKiemHH = findViewById(R.id.btnTimKiemHH);
            btnTimKiemHH.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Lấy chuỗi tìm kiếm từ EditText
                    String searchQuery = edtTimKiemHH.getText().toString().trim();

                    // Nếu chuỗi tìm kiếm không rỗng, thực hiện tìm kiếm
                    if (!searchQuery.isEmpty()) {
                        // Xóa danh sách hàng hóa hiện tại
                        hangHoaArrayList.clear();

                        // Thực hiện tìm kiếm hàng hóa theo tên
                        ArrayList<HangHoa> searchResults = database.TiemKiemHangHoa(searchQuery);

                        // Kiểm tra kết quả tìm kiếm và cập nhật danh sách hàng hóa
                        if (!searchResults.isEmpty()) {
                            hangHoaArrayList.addAll(searchResults);
                            adapter.notifyDataSetChanged();
                        } else {
                            // Hiển thị thông báo nếu không tìm thấy kết quả
                            Toast.makeText(HangHoaActivity.this, "Không tìm thấy hàng hóa", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        // Nếu chuỗi tìm kiếm rỗng, hiển thị thông báo
                        Toast.makeText(HangHoaActivity.this, "Vui lòng nhập tên hàng hóa cần tìm kiếm", Toast.LENGTH_SHORT).show();
                    }
                }
            });


            loadHangHoaData();
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
        public void showHangHoaDialog(){
            dialog = new Dialog(this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.layout_dialog_insert_hanghoa);

            Window window = dialog.getWindow();
            if(window == null){
                return;
            }
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
            WindowManager.LayoutParams windowAttributes = window.getAttributes();
            windowAttributes.gravity = Gravity.CENTER;
            window.setAttributes(windowAttributes);
        }
        public void btnInsertHH(View view) {
            showHangHoaDialog();
            edtTenHH = dialog.findViewById(R.id.edtTenHH);
            edtGiaNhap = dialog.findViewById(R.id.edtGiaNhap);
            edtGiaBan = dialog.findViewById(R.id.edtGiaBan);
            edtSoLuong = dialog.findViewById(R.id.edtSoLuong);
            edtGhiChuHH = dialog.findViewById(R.id.edtGhiChuHH);

            Spinner spnDialogHH = dialog.findViewById(R.id.spnHH);

            // Pass the danhMucHangHoaList to the dialog and set up the Spinner
            loadDanhMucHangHoaData(spnDialogHH);

            Button btnThemHH = dialog.findViewById(R.id.btnThemHH);
            btnThemHH.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String tenHH = edtTenHH.getText().toString();
                    String danhMucHH = spnDialogHH.getSelectedItem().toString(); // Lấy danh mục được chọn từ Spinner trong dialog
                    double giaNhap = Double.parseDouble(edtGiaNhap.getText().toString());
                    double giaBan = Double.parseDouble(edtGiaBan.getText().toString());
                    int soLuong = Integer.parseInt(edtSoLuong.getText().toString());
                    String ghiChuHH = edtGhiChuHH.getText().toString();

                    HangHoa hangHoa = new HangHoa(id, tenHH, danhMucHH, giaBan, giaNhap, soLuong, ghiChuHH);

                    boolean insertHangHoa = database.themHangHoa(hangHoa);

                    if (insertHangHoa) {
                        themHangHoa(hangHoa);
                        Toast.makeText(HangHoaActivity.this, "Thêm hàng hóa thành công!", Toast.LENGTH_SHORT).show();
                        loadHangHoaData();
                        adapter.capNhatListView();
                        dialog.dismiss();
                    } else {
                        Toast.makeText(HangHoaActivity.this, "Thêm hàng hóa thất bại!", Toast.LENGTH_SHORT).show();
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


        // Thêm phương thức để thêm hàng hóa vào danh sách
        public void themHangHoa(HangHoa hangHoa) {
            hangHoaArrayList.add(hangHoa);
            sapXepTheoDanhMuc();
            adapter.capNhatHangHoa(hangHoaArrayList); // Cập nhật lại danh sách hàng hóa trong adapter
            adapter.capNhatListView(); // Cập nhật ListView
            // Sau khi thêm hàng hóa thành công, cập nhật lại danh sách danh mục hàng hóa
            loadDanhMucHangHoaData(spnHH);
        }

        // Sắp xếp danh sách hàng hóa theo danh mục
        public void sapXepTheoDanhMuc() {
            Collections.sort(hangHoaArrayList, new Comparator<HangHoa>() {
                @Override
                public int compare(HangHoa hh1, HangHoa hh2) {
                    return hh1.get_danhMucHH().compareTo(hh2.get_danhMucHH());
                }
            });
            adapter.capNhatHangHoa(hangHoaArrayList);
            adapter.capNhatListView();
        }
        // Cập nhật lại danh sách ListView hiển thị
        private void capNhatListView() {
            adapter.capNhatListView();
        }
        private void loadHangHoaData() {
            String selectedDanhMuc = spnHH.getSelectedItem() != null ? spnHH.getSelectedItem().toString() : "";

            // Kiểm tra xem selectedDanhMuc có null hay không trước khi thực hiện truy vấn dữ liệu
            if (!selectedDanhMuc.isEmpty()) {
                // Thực hiện truy vấn và cập nhật danh sách hàng hóa dựa trên danh mục được chọn
                ArrayList<HangHoa> danhSachHangHoaTheoDanhMuc = database.getHangHoaByDanhMuc(selectedDanhMuc);
                if (danhSachHangHoaTheoDanhMuc != null) {
                    hangHoaArrayList.clear();
                    hangHoaArrayList.addAll(danhSachHangHoaTheoDanhMuc);
                } else {
                    hangHoaArrayList.clear();
                }
                // Cập nhật lại ListView
                adapter.capNhatListView();
            }
        }
        private void loadDanhMucHangHoaData(Spinner spnHH) {
            ArrayList<String> danhMucHangHoaList = new ArrayList<>();
            danhMucHangHoaList.add("Đồ ăn");
            danhMucHangHoaList.add("Nước uống");
            danhMucHangHoaList.add("Dầu ăn");
            danhMucHangHoaList.add("Đồ gia dụng");
            danhMucHangHoaList.add("Kẹo");
            danhMucHangHoaList.add("Khác");
            // Tiếp tục cập nhật adapter cho Spinner
            spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, danhMucHangHoaList);
            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spnHH.setAdapter(spinnerAdapter);

        }
        public void showEditDialog(HangHoa hangHoa) {
            final Dialog dialog = new Dialog(this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.layout_dialog_edit_hanghoa);

            // Tải danh mục hàng hóa vào Spinner trong hộp thoại chỉnh sửa
            Spinner spnDialogHH = dialog.findViewById(R.id.spnHH);
            loadDanhMucHangHoaData(spnDialogHH);

            Window window = dialog.getWindow();
            if (window == null) {
                return;
            }
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
            WindowManager.LayoutParams windowAttributes = window.getAttributes();
            windowAttributes.gravity = Gravity.CENTER;
            window.setAttributes(windowAttributes);

            EditText edtTenHH = dialog.findViewById(R.id.edtTenHH);
            EditText edtGiaNhap = dialog.findViewById(R.id.edtGiaNhap);
            EditText edtGiaBan = dialog.findViewById(R.id.edtGiaBan);
            EditText edtSoLuong = dialog.findViewById(R.id.edtSoLuong);
            EditText edtGhiChuHH = dialog.findViewById(R.id.edtGhiChuHH);

            edtTenHH.setText(hangHoa.get_tenHH());
            edtGiaNhap.setText(String.valueOf(hangHoa.get_giaNhap()));
            edtGiaBan.setText(String.valueOf(hangHoa.get_giaBan()));
            edtSoLuong.setText(String.valueOf(hangHoa.get_soLuong()));
            edtGhiChuHH.setText(hangHoa.get_ghiChuHH());

            Button btnCapNhatHH = dialog.findViewById(R.id.btnCapNhatHH);
            btnCapNhatHH.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String tenHH = edtTenHH.getText().toString();
                    String danhMucHH = spnHH.getSelectedItem().toString();
                    double giaNhap = Double.parseDouble(edtGiaNhap.getText().toString());
                    double giaBan = Double.parseDouble(edtGiaBan.getText().toString());
                    int soLuong = Integer.parseInt(edtSoLuong.getText().toString());
                    String ghiChuHH = edtGhiChuHH.getText().toString();

                    HangHoa hangHoaSua = new HangHoa(hangHoa.get_maHH(), tenHH, danhMucHH, giaBan, giaNhap, soLuong, ghiChuHH);

                    boolean updateHangHoa = database.suaHangHoa(hangHoaSua);
                    if (updateHangHoa) {
                        // Sửa thông tin hàng hóa và cập nhật lại danh sách hàng hóa sau khi sửa
                        suaHangHoa(hangHoaSua);
                        Toast.makeText(HangHoaActivity.this, "Sửa hàng hóa thành công!", Toast.LENGTH_SHORT).show();
                        loadHangHoaData();
                        adapter.capNhatListView();
                        dialog.dismiss();
                    } else {
                        Toast.makeText(HangHoaActivity.this, "Sửa hàng hóa thất bại!", Toast.LENGTH_SHORT).show();
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
        // Thêm phương thức để sửa hàng hóa trong danh sách
        public void suaHangHoa(HangHoa hangHoa) {
            for (int i = 0; i < hangHoaArrayList.size(); i++) {
                if (hangHoaArrayList.get(i).get_maHH() == hangHoa.get_maHH()) {
                    hangHoaArrayList.set(i, hangHoa);
                    sapXepTheoDanhMuc();
                    adapter.capNhatHangHoa(hangHoaArrayList); // Cập nhật lại danh sách hàng hóa trong adapter
                    adapter.capNhatListView(); // Cập nhật ListView
                    break;
                }
            }
            // Sau khi sửa hàng hóa thành công, cập nhật lại danh sách danh mục hàng hóa
            loadDanhMucHangHoaData(spnHH);
        }
        public void xoaHangHoa(HangHoa hangHoa) {
            boolean deleteHangHoa = database.xoaHangHoa(hangHoa.get_maHH());
            if (deleteHangHoa) {
                // Xóa hàng hóa và cập nhật lại danh sách
                hangHoaArrayList.remove(hangHoa);
                adapter.capNhatListView();
                Toast.makeText(HangHoaActivity.this, "Xóa hàng hóa thành công!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(HangHoaActivity.this, "Xóa hàng hóa thất bại!", Toast.LENGTH_SHORT).show();
            }
        }
    }