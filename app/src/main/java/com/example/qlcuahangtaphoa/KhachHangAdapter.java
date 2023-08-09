package com.example.qlcuahangtaphoa;

import android.app.Activity;
import android.app.Dialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class KhachHangAdapter extends ArrayAdapter<KhachHang> {
    Activity context = null;
    LayoutInflater inflater;
    ArrayList<KhachHang> khachHangArrayList = null;
    int layoutId;
    ListView listView;
    TextView textView;
    MyDatabase database;

    public KhachHangAdapter(Activity context, int layoutId, ArrayList<KhachHang> khachHangArrayList, ListView listView) {
        super(context, layoutId, khachHangArrayList);

        this.context = context;
        this.khachHangArrayList = khachHangArrayList;
        this.layoutId = layoutId;
        this.listView = listView;
        database = new MyDatabase(context);

        inflater = LayoutInflater.from(context);
    }


    @Override
    public int getCount() {
        return KhachHangActivity.khachHangArrayList.size();
    }

    @Override
    public KhachHang getItem(int position) {
        return KhachHangActivity.khachHangArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return KhachHangActivity.khachHangArrayList.get(position).get_maKH();
    }


    @Override
    public View getView( final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(layoutId, parent, false);

            holder = new ViewHolder();

            holder.tvTen = convertView.findViewById(R.id.tvTen);
            holder.tvSDT = convertView.findViewById(R.id.tvSDT);
            holder.tvDC = convertView.findViewById(R.id.tvDC);
            holder.btnShowPopup = convertView.findViewById(R.id.btnShowPopup);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final KhachHang khachHang = khachHangArrayList.get(position);

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Gọi phương thức onItemClick khi người dùng nhấp vào một mục
                if (itemClickListener != null) {
                    itemClickListener.onItemClick(khachHang);
                }
            }
        });

        holder.tvTen.setText(khachHang.get_tenKH());
        holder.tvSDT.setText(khachHang.get_sdt());
        holder.tvDC.setText(khachHang.get_diachi());

        // Gắn sự kiện onClick vào button Show Popup
        holder.btnShowPopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenu(view, khachHang);
            }
        });

        return convertView;
    }
    static class ViewHolder {
        TextView tvTen;
        TextView tvSDT;
        TextView tvDC;
        TextView tvGhiChu;
        ImageButton btnShowPopup;
    }
    private void xoaKhachHang(KhachHang khachHang) {
        database.xoaKhachHang(khachHang.get_maKH());
        Toast.makeText(context, "Xóa khách hàng thành công!", Toast.LENGTH_SHORT).show();
        khachHangArrayList.remove(khachHang);
        notifyDataSetChanged();
    }
    private void showPopupMenu(View view, final KhachHang khachHang) {
        PopupMenu popupMenu = new PopupMenu(context, view);
        MenuInflater inflater = popupMenu.getMenuInflater();
        inflater.inflate(R.menu.menu_khachhang_popup, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.menuItem_edit) {
                    // Xử lý sự kiện khi người dùng chọn menu Sửa
                    showEditDialog(khachHang);
                    return true;
                } else if (item.getItemId() == R.id.menuItem_remove) {
                    // Xử lý sự kiện khi người dùng chọn menu Xóa
                    xoaKhachHang(khachHang);
                    return true;
                }
                return false;
            }
        });
        popupMenu.show();
    }
    private void showEditDialog(final KhachHang khachHang) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_dialog_edit_khachhang);

        Window window = dialog.getWindow();
        if (window == null) {
            return;
        }

        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = Gravity.CENTER;
        window.setAttributes(windowAttributes);

        // Hiển thị thông tin khách hàng hiện tại trong dialog
        EditText edtTen = dialog.findViewById(R.id.edtTen);
        EditText edtDC = dialog.findViewById(R.id.edtDC);
        EditText edtEmail = dialog.findViewById(R.id.edtEmail);
        EditText edtSDT = dialog.findViewById(R.id.edtSDT);
        EditText edtGhiChu = dialog.findViewById(R.id.edtGhiChu);

        edtTen.setText(khachHang.get_tenKH());
        edtSDT.setText(khachHang.get_sdt());
        edtEmail.setText(khachHang.get_email());
        edtDC.setText(khachHang.get_diachi());
        edtGhiChu.setText(khachHang.get_ghichu());

        Button btnCapNhat = dialog.findViewById(R.id.btnCapNhat);


        btnCapNhat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tenKH = edtTen.getText().toString();
                String sdtKH = edtSDT.getText().toString();
                String emailKH = edtEmail.getText().toString();
                String diachiKH = edtDC.getText().toString();
                String ghichuKH = edtGhiChu.getText().toString();

                // Cập nhật thông tin mới vào đối tượng khachHang
                khachHang.set_tenKH(tenKH);
                khachHang.set_sdt(sdtKH);
                khachHang.set_email(emailKH);
                khachHang.set_diachi(diachiKH);
                khachHang.set_ghichu(ghichuKH);

                // Xử lý cập nhật thông tin khách hàng trong cơ sở dữ liệu
                boolean result = database.suaKhachHang(khachHang);
                if (result) {
                    Toast.makeText(context, "Sửa thông tin khách hàng thành công", Toast.LENGTH_SHORT).show();

                    // Cập nhật dữ liệu trong danh sách khachHangArrayList
                    for (int i = 0; i < khachHangArrayList.size(); i++) {
                        if (khachHangArrayList.get(i).get_maKH() == khachHang.get_maKH()) {
                            khachHangArrayList.set(i, khachHang);
                            break;
                        }
                    }

                    notifyDataSetChanged(); // Cập nhật giao diện ListView
                    dialog.dismiss();
                } else {
                    Toast.makeText(context, "Sửa thông tin khách hàng thất bại", Toast.LENGTH_SHORT).show();
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
    public void showEditDialogForActivity(KhachHang khachHang) {
        showEditDialog(khachHang);
    }

    // -----------------DetailKhachHang-----------------------------
    // Phương thức hiển thị thông tin khách hàng khi nhấp vào item trong ListView
    public void showInfoDialog(final KhachHang khachHang) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_dialog_info_khachhang);

        Window window = dialog.getWindow();
        if (window == null) {
            return;
        }

        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = Gravity.CENTER;
        window.setAttributes(windowAttributes);

        // Hiển thị thông tin khách hàng trong dialog
        TextView tvTen = dialog.findViewById(R.id.tvTen);
        TextView tvSDT = dialog.findViewById(R.id.tvSDT);
        TextView tvDC = dialog.findViewById(R.id.tvDC);
        TextView tvEmail = dialog.findViewById(R.id.tvEmail);
        TextView tvGhiChu = dialog.findViewById(R.id.tvGhiChu);

        tvTen.setText(khachHang.get_tenKH());
        tvSDT.setText(khachHang.get_sdt());
        tvEmail.setText(khachHang.get_email());
        tvDC.setText(khachHang.get_diachi());
        tvGhiChu.setText(khachHang.get_ghichu());

        Button btnHuy = dialog.findViewById(R.id.btnHuy);
        btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss(); // Tắt dialog khi nhấn nút đóng
            }
        });

        dialog.show();
    }
    public interface OnItemClickListener {
        void onItemClick(KhachHang khachHang);
    }

    private OnItemClickListener itemClickListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.itemClickListener = listener;
    }

}

