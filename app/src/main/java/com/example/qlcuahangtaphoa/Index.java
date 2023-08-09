package com.example.qlcuahangtaphoa;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Index extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
}