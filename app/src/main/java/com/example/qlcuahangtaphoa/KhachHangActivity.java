package com.example.qlcuahangtaphoa;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class KhachHangActivity extends AppCompatActivity {
    public ListView listView;
    public static ArrayList<KhachHang> khachHangArrayList;

    public static MyDatabase database;
    private EditText edtTen, edtDC, edtSDT, edtGhiChu, edtEmail;
    ImageButton btnBack, btnAdd;
    Bundle bundle;
    KhachHangAdapter adapter;
    Dialog dialog;
    private static final int id = -1;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_khach_hang);
        Intent intent = getIntent();
        bundle = intent.getExtras();

        initViews();
        loadKhachHangData();
        //----------
        registerForContextMenu(listView);
        updateAppbar();

        adapter = new KhachHangAdapter(this, R.layout.layout_item_khachhang, khachHangArrayList, listView);
        adapter.setOnItemClickListener(new KhachHangAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(KhachHang khachHang) {
                // Xử lý khi người dùng click vào một item trong ListView ở đây
                // Ví dụ: Hiển thị thông tin khách hàng
                showKhachHangInfo(khachHang);
            }
        });
        listView.setAdapter(adapter);
    }
    private void initViews() {
        listView = findViewById(R.id.lvKH);
        btnBack = findViewById(R.id.btnBack);
        btnAdd = findViewById(R.id.btnAdd);
        context = this;
        khachHangArrayList = new ArrayList<>();
        database = new MyDatabase(this);
        adapter = new KhachHangAdapter(this, R.layout.layout_item_khachhang, khachHangArrayList, listView);
        listView.setAdapter(adapter);
    }

    public void updateAppbar(){
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void loadKhachHangData() {
        khachHangArrayList.clear();

        // Truy vấn dữ liệu từ cơ sở dữ liệu
        ArrayList<KhachHang> khachHangs = database.getAllKhachHang();

        // Thêm khách hàng vào danh sách
        if (khachHangs != null) {
            khachHangArrayList.addAll(khachHangs);
        }
        // Log thông tin danh sách khách hàng
        for (KhachHang khachHang : khachHangArrayList) {
            Log.d("KhachHangActivity", "KhachHang: " + khachHang.get_tenKH());
        }

        // Cập nhật lại adapter sau khi có dữ liệu mới
        adapter.notifyDataSetChanged();
    }
    public void showKhachHangDialog(){

        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_dialog_insert_khachhang);

        Window window = dialog.getWindow();
        if(window == null){
            return;
        }

        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);


        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = Gravity.CENTER;
        window.setAttributes(windowAttributes);
    }

    public void btnInsertKH(View view) {
        showKhachHangDialog();
        edtTen = dialog.findViewById(R.id.edtTen);
        edtDC = dialog.findViewById(R.id.edtDC);
        edtEmail = dialog.findViewById(R.id.edtEmail);
        edtSDT = dialog.findViewById(R.id.edtSDT);
        edtGhiChu = dialog.findViewById(R.id.edtGhiChu);

        Button btnThemKH = dialog.findViewById(R.id.btnThem);

        btnThemKH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tenKH = edtTen.getText().toString();
                String sdtKH = edtSDT.getText().toString();
                String emailKH = edtEmail.getText().toString();
                String diachiKH = edtDC.getText().toString();
                String ghichuKH = edtGhiChu.getText().toString();

                KhachHang khachHang = new KhachHang();
                khachHang.set_maKH(id);
                khachHang.set_tenKH(tenKH);
                khachHang.set_sdt(sdtKH);
                khachHang.set_email(emailKH);
                khachHang.set_diachi(diachiKH);
                khachHang.set_ghichu(ghichuKH);

                Boolean insertKhachHang = database.themKhachHang(khachHang);

                if(insertKhachHang){
                    Toast.makeText(KhachHangActivity.this, "Thêm thành công!", Toast.LENGTH_SHORT).show();

                    loadKhachHangData();
                    adapter.notifyDataSetChanged();

                    dialog.dismiss();
                }else{
                    Toast.makeText(KhachHangActivity.this, "Thêm thất bại!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        dialog.show();
    }
    
    //----------------------------------------------------------------
    

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menuItem_remove) {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
            int position = info.position;
            KhachHang khachHang = khachHangArrayList.get(position);
            xoaKhachHang(khachHang);
            return true;
        }
        return super.onContextItemSelected(item);
    }

    private void xoaKhachHang(KhachHang khachHang) {
        // Xử lý xóa khách hàng trong cơ sở dữ liệu
        boolean result = database.xoaKhachHang(khachHang.get_maKH());
        if (result) {
            Toast.makeText(this, "Xóa khách hàng thành công", Toast.LENGTH_SHORT).show();
            loadKhachHangData(); // Tải lại dữ liệu sau khi xóa
        } else {
            Toast.makeText(this, "Xóa khách hàng thất bại", Toast.LENGTH_SHORT).show();
        }
    }
    //------------------Sự kiện onClickItem hiển thị thông tin khách hàng-----


    private void showKhachHangInfo(KhachHang khachHang) {
        adapter.showInfoDialog(khachHang);
    }
    public void btnCancelKH(View view) {
        dialog.dismiss();
    }
}