package com.example.qlcuahangtaphoa;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qlcuahangtaphoa.DonHang.DonHangActivity;
import com.example.qlcuahangtaphoa.HangHoa.HangHoaActivity;
import com.example.qlcuahangtaphoa.KhanhHang.KhachHangActivity;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

public class Index extends AppCompatActivity {
    MyDatabase database;
    User user;
    Bundle bundle;
    SharedPreferences sp;
    SharedPreferences.Editor editor;
    private DrawerLayout drawerLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav);
        View headerView = navigationView.getHeaderView(0);

        TextView tvHello = (TextView) headerView.findViewById(R.id.tvHello);
        ImageView imgAvatar = (ImageView) headerView.findViewById(R.id.imgAvatar);
        sp = getSharedPreferences("Data", MODE_PRIVATE);

        database = new MyDatabase(this);
        user = new User();

        Intent intent = getIntent();
        bundle = intent.getExtras();

        if(bundle != null){
            ArrayList<String> arrDataUser = bundle.getStringArrayList("dataUser");
            user.convertArrayToUser(arrDataUser);
        }

        if(user.get_gioitinh().equals("nam")){
            imgAvatar.setBackgroundResource(R.drawable.ic_nam);
        } else {
            imgAvatar.setBackgroundResource(R.drawable.ic_nu);
        }
        tvHello.setText(user.get_ten());
        drawerLayout = findViewById(R.id.drawLayout);
        navigationView = findViewById(R.id.nav);
        Button btnMenu = findViewById(R.id.btnMenu);
        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.openDrawer(GravityCompat.START);
                } else {
                    drawerLayout.closeDrawer(GravityCompat.START);
                }
            }
        });
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.nav_KhachHang) {
                    btnKhachHag(null);
                    return true;
                } else if (id == R.id.nav_HangHoa) {
                    btnHangHoa(null);
                    return true;
                } else if (id == R.id.nav_DonHang) {
                    btnDonHang(null);
                    return true;
                } else if (id == R.id.nav_ThongTin) {
                    btnInfo();
                    return true;
                } else if (id == R.id.nav_DangXuat) {
                    btnDangXuat();
                    return true;
                }
                // Thêm các điều kiện khác nếu cần
                return false;
            }
        });
    }
    public void btnInfo() {
        Intent intent = new Intent(getApplicationContext(), InfoActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }
    public void btnDangXuat() {
        editor = sp.edit();
        editor.clear();
        editor.commit();
        finish();
        Toast.makeText(this, "Đã đăng xuất", Toast.LENGTH_SHORT).show();
    }
    public void btnKhachHag(View view) {
        Intent intent = new Intent(getApplicationContext(), KhachHangActivity.class);
        startActivity(intent);
    }

    public void btnHangHoa(View view) {
        Intent intent = new Intent(getApplicationContext(), HangHoaActivity.class);
        startActivity(intent);
    }

    public void btnDonHang(View view) {
        Intent intent = new Intent(getApplicationContext(), DonHangActivity.class);
        startActivity(intent);
    }

    public void btnThongKe(View view) {
        Intent intent = new Intent(getApplicationContext(), ThongKeDonHangActivity.class);
        startActivity(intent);
    }
}