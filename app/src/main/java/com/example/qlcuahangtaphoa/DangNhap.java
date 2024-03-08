package com.example.qlcuahangtaphoa;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class DangNhapActivity extends AppCompatActivity {
    public static MyDatabase database;
    EditText edtEmail, edtPassword;
    Button btnLogin;
    CheckBox cbRemember;
    User user;
    SharedPreferences sp;
    SharedPreferences.Editor editor;
    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_nhap);

        database = new MyDatabase(this);
        edtEmail = (EditText) findViewById(R.id.edtEmail);
        edtPassword = (EditText) findViewById(R.id.edtPassword);
        Button btnLogin = (Button) findViewById(R.id.btnLogin);
        cbRemember = (CheckBox) findViewById(R.id.cbRemember);
        sp = getSharedPreferences("Data", MODE_PRIVATE);
        editor = sp.edit();

        Boolean login = sp.getBoolean("DaDangNhap", false);

        if (login == true) {
            user = database.checkEmailPassword(sp.getString("email", "email"), sp.getString("password", "password"));
            switchView();
        }
    }

    public void btnRegister(View view) {
        Intent intent = new Intent(getApplicationContext(), DangKyActivity.class);
        startActivity(intent);
    }

    public void btnLogin(View view) {
        String email = edtEmail.getText().toString();
        String password = edtPassword.getText().toString();
        //String email = "tk1";
        //String password = "123";

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Các ô nhập không bỏ trống!", Toast.LENGTH_SHORT).show();
        } else {
            user = database.checkEmailPassword(email, password);
            if (user != null)
            {
                if (cbRemember.isChecked()) {
                    editor.putBoolean("DaDangNhap", true);
                    editor.putString("email", user.get_email());
                    editor.putString("password", user.get_matkhau());
                    editor.apply();
                }
                Toast.makeText(this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                switchView();
            }
            else {
                Toast.makeText(this, "Email hoặc mật khẩu không chính xác", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void switchView(){
        Intent intent;
        bundle = new Bundle();
        bundle.putStringArrayList("dataUser", user.convertUserToArray());
        if(user.get_ten() != null){
            intent = new Intent(getApplicationContext(), Index.class);
        }
        else {
            intent = new Intent(getApplicationContext(), InfoActivity.class);
        }
        intent.putExtras(bundle);
        startActivity(intent);
    }
}
