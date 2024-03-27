package com.example.qlcuahangtaphoa.DangKy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.qlcuahangtaphoa.DangNhapActivity;
import com.example.qlcuahangtaphoa.MyDatabase;
import com.example.qlcuahangtaphoa.R;
import com.example.qlcuahangtaphoa.User;

public class DangKyActivity extends AppCompatActivity {
    public static MyDatabase database;
    EditText edtEmail;
    EditText edtPassword;
    EditText edtRePassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_ky);

        edtEmail = (EditText) findViewById(R.id.edtEmail);
        edtPassword = (EditText) findViewById(R.id.edtPassword);
        edtRePassword = (EditText) findViewById(R.id.edtRePassword);

        database = new MyDatabase(this);
    }

    public void Register(View view) {
        String email = edtEmail.getText().toString();
        String password = edtPassword.getText().toString();
        String rePassword = edtRePassword.getText().toString();

        if(email.isEmpty() || password.isEmpty() || rePassword.isEmpty()){
            Toast.makeText(this, "Các ô nhập không bỏ trống!", Toast.LENGTH_SHORT).show();
        } else {
            if(password.equals(rePassword)){
                Boolean checkEmail = database.checkEmail(email);

                if(checkEmail == false){
                    User user = new User();
                    user.set_email(email);
                    user.set_matkhau(password);
                    Boolean insert = database.insertUser(user);

                    if(insert == true)
                    {
                        Toast.makeText(this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), DangNhapActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(this, "Email không đúng định dạng", Toast.LENGTH_SHORT).show();
                    }
                } else{
                    Toast.makeText(this, "Email đã tồn tại!", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Mật khẩu không trùng khớp!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}