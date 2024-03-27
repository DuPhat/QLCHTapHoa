package com.example.qlcuahangtaphoa;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class EditInfoActivity extends AppCompatActivity {

    public static User user;
    Bundle bundle;
    MyDatabase database;
    ArrayAdapter adapter;
    ImageButton btnBack;
    TextView tvTitle;
    Spinner boxRole;
    EditText edtName;
    EditText edtBorn;
    EditText edtSDT;
    EditText edtEmail;
    EditText edtDiaChi;
    Button btnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_info);

        btnBack = (ImageButton) findViewById(R.id.btnBack);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        boxRole = (Spinner) findViewById(R.id.boxRole);
        edtName = (EditText) findViewById(R.id.edtName);
        edtBorn = (EditText) findViewById(R.id.edtBorn);
        edtSDT = (EditText) findViewById(R.id.edtSDT);
        edtEmail = (EditText) findViewById(R.id.edtEmail);
        edtDiaChi = (EditText) findViewById(R.id.edtDiaChi);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);

        updateAppbar();

        Intent intent = getIntent();
        bundle = intent.getExtras();
        user = new User();
        database = new MyDatabase(this);

        String[] listRole = new String[]{"Nam", "Nữ"};
        adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, listRole);
        boxRole.setAdapter(adapter);


        if(bundle != null){
            ArrayList<String> arrDataUser = bundle.getStringArrayList("dataUser");
            user.convertArrayToUser(arrDataUser);
        }

        edtName.setText(user.get_ten());
        edtEmail.setText(user.get_email());
        edtBorn.setText(user.get_ngaysinh());
        edtSDT.setText(user.get_sdt());
        edtEmail.setText(user.get_email());
        edtDiaChi.setText(user.get_diachi());
    }

    public void updateAppbar(){
        tvTitle.setText("Chỉnh sửa thông tin");
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void btnSubmit(View view) {
        String name = edtName.getText().toString();
        String born = edtBorn.getText().toString();
        String sdt = edtSDT.getText().toString();
        String address = edtDiaChi.getText().toString();

        if(name.isEmpty() || born.isEmpty() || sdt.isEmpty() || address.isEmpty()){
            Toast.makeText(this, "Hãy điền đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
        }
        else{
            user.set_ten(name);
            user.set_ngaysinh(born);
            user.set_gioitinh(user.convertGioiTinh(boxRole.getSelectedItemId()));
            user.set_sdt(sdt);
            user.set_diachi(address);

            if(database.updateUser(user)){
                Intent intent = new Intent(getApplicationContext(), Index.class);
                Bundle bundle = new Bundle();
                bundle.putStringArrayList("dataUser", user.convertUserToArray());
                intent.putExtras(bundle);
                startActivity(intent);

                Toast.makeText(this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
            }
        }
    }
}