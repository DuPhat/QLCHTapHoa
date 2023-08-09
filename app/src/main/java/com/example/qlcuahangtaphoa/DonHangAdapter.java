package com.example.qlcuahangtaphoa;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;

import java.util.ArrayList;

public class DonHangAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<DonHang> donHangArrayList;
    private int layoutId;
    private LayoutInflater inflater;
    private MyDatabase database;
    private DonHangActivity donHangActivity;

    public DonHangAdapter(Context context, int layoutId, ArrayList<DonHang> donHangArrayList) {
        this.context = context;
        this.donHangArrayList = donHangArrayList;
        this.layoutId = layoutId;
        this.donHangActivity = donHangActivity;
        inflater = LayoutInflater.from(context);
        database = new MyDatabase(context);
    }

    @Override
    public int getCount() {
        return donHangArrayList.size();
    }

    @Override
    public DonHang getItem(int position) {
        return donHangArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(layoutId, parent, false);
            holder = new ViewHolder();
            holder.tvTenHD = convertView.findViewById(R.id.tvTenDH);
            holder.tvTenKH = convertView.findViewById(R.id.tvTenKH);
            holder.tvDate = convertView.findViewById(R.id.tvDate);
            holder.tvTongTien = convertView.findViewById(R.id.tvTongTien);
            holder.tvTrangThai = convertView.findViewById(R.id.tvTrangThai);
            holder.btnShowPopupMenu = convertView.findViewById(R.id.btnShowPopupDH);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // Lấy thông tin đơn hàng tại vị trí position
        DonHang donHang = donHangArrayList.get(position);

        // Hiển thị thông tin đơn hàng lên giao diện
        holder.tvTenHD.setText(donHang.get_tendh());
        String tenKH = database.getTenKhachHang(donHang.get_maKH());
        holder.tvTenKH.setText(tenKH);
        holder.tvDate.setText(donHang.get_thoiGian());
        holder.tvTongTien.setText(String.valueOf(donHang.get_tongTien()));
        holder.tvTrangThai.setText(donHang.get_trangThai());


        holder.btnShowPopupMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenu(view, donHangArrayList.get(position));

            }
        });


        return convertView;
    }

    static class ViewHolder {
        TextView tvTenHD, tvTenKH, tvDate, tvTongTien, tvTrangThai;
        ImageButton btnShowPopupMenu;
    }
    private void showPopupMenu(View view, final DonHang donHang) {
        PopupMenu popupMenu = new PopupMenu(context, view);
        MenuInflater inflater = popupMenu.getMenuInflater();
        inflater.inflate(R.menu.menu_khachhang_popup, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.menuItem_edit) {
                    // Xử lý sự kiện khi người dùng chọn menu Sửa
                    donHangActivity.showEditDialog(donHang);
                    return true;
                } else if (item.getItemId() == R.id.menuItem_remove) {
                    // Xử lý sự kiện khi người dùng chọn menu Xóa
                    // ...
                    return true;
                }
                return false;
            }
        });
        popupMenu.show();
    }
}
