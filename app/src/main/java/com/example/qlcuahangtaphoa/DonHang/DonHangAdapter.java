package com.example.qlcuahangtaphoa.DonHang;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.example.qlcuahangtaphoa.MyDatabase;
import com.example.qlcuahangtaphoa.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;

public class DonHangAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<DonHang> donHangArrayList;
    private int layoutId;
    private LayoutInflater inflater;
    private MyDatabase database;
    private DonHangActivity donHangActivity;

    private static final int SORT_BY_TEN = 0;
    private static final int SORT_BY_DATE = 1;
    private static final int SORT_BY_TONGTIEN = 2;

    public DonHangAdapter(Context context, int layoutId, ArrayList<DonHang> donHangArrayList, DonHangActivity donHangActivity) {
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
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (donHangActivity != null) {
                    donHangActivity.showDetailDialog(donHang);
                }
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
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Xác nhận xóa")
                            .setMessage("Bạn có chắc muốn xóa đơn hàng này?")
                            .setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    deleteDonHang(donHang);
                                    dialog.dismiss();
                                }
                            })
                            .setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .show();
                    return true;
                }
                return false;
            }
        });
        popupMenu.show();
    }

    private void deleteDonHang(DonHang donHang) {
        boolean deleteResult = database.deleteDonHang(donHang.get_maDH());

        if (deleteResult) {
            Toast.makeText(context, "Xóa đơn hàng thành công", Toast.LENGTH_SHORT).show();
            donHangArrayList.remove(donHang);
            notifyDataSetChanged();
        } else {
            Toast.makeText(context, "Xóa đơn hàng thất bại", Toast.LENGTH_SHORT).show();
        }
    }

    public void sortDonHangList(int sortBy, boolean ascending){
        Collections.sort(donHangArrayList, new Comparator<DonHang>(){
            @Override
            public int compare(DonHang dh1, DonHang dh2) {
                switch (sortBy) {
                    case SORT_BY_TEN:
                        return ascending ? dh1.get_tendh().compareTo(dh2.get_tendh()) : dh2.get_tendh().compareTo(dh1.get_tendh());
                    case SORT_BY_DATE:
                        // Chuyển đổi chuỗi ngày thành đối tượng Date để so sánh
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                        try {
                            Date date1 = sdf.parse(dh1.get_thoiGian());
                            Date date2 = sdf.parse(dh2.get_thoiGian());
                            return ascending ? ((Date) date1).compareTo(date2) : date2.compareTo(date1);
                        } catch (ParseException e) {
                            e.printStackTrace();
                            return 0;
                        }
                    case SORT_BY_TONGTIEN:
                        return ascending ? Double.compare(dh1.get_tongTien(), dh2.get_tongTien()) : Double.compare(dh2.get_tongTien(), dh1.get_tongTien());
                    default:
                        return 0;
                }
            }
        });
        notifyDataSetChanged();
    }
}
