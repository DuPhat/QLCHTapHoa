package com.example.qlcuahangtaphoa;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class InfoActivity extends AppCompatActivity {

    public static User user;
    Bundle bundle;
    MyDatabase database;
    ImageView tvAvatar;
    TextView tvName;
    TextView tvBorn;
    TextView tvEmail;
    TextView tvSDT;
    TextView tvAddress;
    TextView tvBackText;
    TextView tvTitle;
    ImageButton btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        tvAvatar = (ImageView) findViewById(R.id.tvAvatar);
        tvName = (TextView) findViewById(R.id.tvName);
        tvBorn = (TextView) findViewById(R.id.tvBorn);
        tvEmail = (TextView) findViewById(R.id.tvEmail);
        tvSDT = (TextView) findViewById(R.id.tvSDT);
        tvAddress = (TextView) findViewById(R.id.tvDiaChi);
        btnBack = (ImageButton) findViewById(R.id.btnBack);

        Intent intent = getIntent();
        bundle = intent.getExtras();
        user = new User();

        if(bundle != null){
            // Nhận dữ liệu từ Bundle
            ArrayList<String> arrDataUser = bundle.getStringArrayList("dataUser");
            user = new User();
            user.convertArrayToUser(arrDataUser);

            // Hiển thị thông tin người dùng
            if("Nam".equals(user.get_gioitinh())){
                tvAvatar.setBackgroundResource(R.drawable.ic_nam);
            } else {
                tvAvatar.setBackgroundResource(R.drawable.ic_nu);
            }
            tvName.setText(user.get_ten());
            tvBorn.setText(user.get_ngaysinh());
            tvEmail.setText(user.get_email());
            tvSDT.setText(user.get_sdt());
            tvAddress.setText(user.get_diachi());
        }
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void btnEdit(View view) {
        Intent intent = new Intent(getApplicationContext(), EditInfoActivity.class);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            intent.putExtras(extras);
        }
        startActivity(intent);
    }
}