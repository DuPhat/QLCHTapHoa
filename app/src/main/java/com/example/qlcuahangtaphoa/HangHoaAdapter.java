package com.example.qlcuahangtaphoa;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class HangHoaAdapter extends ArrayAdapter<HangHoa> {
    Activity context = null;
    LayoutInflater inflater;
    ArrayList<HangHoa> hangHoaArrayList = null;
    int layoutId;
    ListView listView;

    MyDatabase database;

    public HangHoaAdapter(Activity context, int layoutId, ArrayList<HangHoa> hangHoaArrayList, ListView listView) {
        super(context, layoutId, hangHoaArrayList);
        this.context = context;
        this.hangHoaArrayList = hangHoaArrayList;
        this.layoutId = layoutId;
        this.listView = listView;
        database = new MyDatabase(context);

        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return hangHoaArrayList.size();
    }

    @Override
    public HangHoa getItem(int position) {
        return hangHoaArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return hangHoaArrayList.get(position).get_maHH();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(layoutId, null);
            holder = new ViewHolder();
            holder.tvTenHH = convertView.findViewById(R.id.tvTenHH);

            holder.tvGiaBan = convertView.findViewById(R.id.tvGiaBan);
            holder.tvSoLuong = convertView.findViewById(R.id.tvSoLuong);
            holder.btnShowPopupMenu = convertView.findViewById(R.id.btnShowPopupHH);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // Lấy thông tin hàng hóa tại vị trí position
        HangHoa hangHoa = hangHoaArrayList.get(position);

        // Hiển thị thông tin hàng hóa lên giao diện
        holder.tvTenHH.setText(hangHoa.get_tenHH());

        holder.tvGiaBan.setText(String.valueOf(hangHoa.get_giaBan()));
        holder.tvSoLuong.setText(Integer.toString(hangHoa.get_soLuong()));

        // Gắn sự kiện onClick vào button Show Popup
        holder.btnShowPopupMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editHangHoaListener != null) {
                    showPopupMenu(view, hangHoa);
                }

            }
        });
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showInfoDialogHangHoa(hangHoa);
            }
        });

        return convertView;
    }

    static class ViewHolder {
        TextView tvTenHH, tvDMHH, tvGiaBan, tvGiaNhap,tvSoLuong, tvGhiChuHH;
        ImageButton btnShowPopupMenu;

    }
    // Phương thức để cập nhật lại danh sách hàng hóa trong adapter
    public void capNhatHangHoa(ArrayList<HangHoa> hangHoaList) {
        hangHoaArrayList.clear();
        hangHoaArrayList.addAll(hangHoaList);
    }
    public void capNhatListView() {
        notifyDataSetChanged();
    }

    //Show menu

    // Các biến và phương thức hiện tại

    private HangHoaActivity hangHoaActivity; // Thêm biến tham chiếu của HangHoaActivity

    public HangHoaAdapter(HangHoaActivity activity, int layoutId, ArrayList<HangHoa> hangHoaArrayList, ListView listView) {
        super(activity, layoutId, hangHoaArrayList);
        this.context = activity;
        this.hangHoaArrayList = hangHoaArrayList;
        this.layoutId = layoutId;
        this.listView = listView;
        database = new MyDatabase(activity);

        inflater = LayoutInflater.from(activity);
        this.hangHoaActivity = activity; // Gán giá trị cho biến tham chiếu của HangHoaActivity
    }
    private void showPopupMenu(View view, final HangHoa hangHoa) {
        PopupMenu popupMenu = new PopupMenu(context, view);
        MenuInflater inflater = popupMenu.getMenuInflater();
        inflater.inflate(R.menu.menu_khachhang_popup, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.menuItem_edit) {
                    // Xử lý sự kiện khi người dùng chọn menu Sửa
                    hangHoaActivity.showEditDialog(hangHoa);
                    return true;
                } else if (item.getItemId() == R.id.menuItem_remove) {
                    // Xử lý sự kiện khi người dùng chọn menu Xóa
                    hangHoaActivity.xoaHangHoa(hangHoa);
                    return true;
                }
                return false;
            }
        });
        popupMenu.show();
    }

    public interface OnEditHangHoaListener {
        void onEditHangHoa(HangHoa hangHoa);
    }
    private OnEditHangHoaListener editHangHoaListener;

    public void setOnEditHangHoaListener(OnEditHangHoaListener listener) {
        this.editHangHoaListener = listener;
    }
    // Phương thức hiển thị chi tiết hàng hóa
    private void showInfoDialogHangHoa(HangHoa hangHoa) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_dialog_info_hanghoa);
        Window window = dialog.getWindow();
        if (window == null) {
            return;
        }

        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = Gravity.CENTER;
        window.setAttributes(windowAttributes);
        // Tìm các TextView trong giao diện hộp thoại
        TextView tvTenHH = dialog.findViewById(R.id.tvTenHH);
        TextView tvDMHH = dialog.findViewById(R.id.tvDMHH);
        TextView tvGiaNhap = dialog.findViewById(R.id.tvGiaNhap);
        TextView tvGiaBan = dialog.findViewById(R.id.tvGiaBan);
        TextView tvSoLuong = dialog.findViewById(R.id.tvSoLuong);
        TextView tvGhiChuHH = dialog.findViewById(R.id.tvGhiChuHH);

        // Đặt thông tin của hàng hóa vào các TextView
        tvTenHH.setText(hangHoa.get_tenHH());
        tvDMHH.setText(hangHoa.get_danhMucHH());
        tvGiaNhap.setText(String.valueOf(hangHoa.get_giaNhap()));
        tvGiaBan.setText(String.valueOf(hangHoa.get_giaBan()));
        tvSoLuong.setText(Integer.toString(hangHoa.get_soLuong()));
        tvGhiChuHH.setText(hangHoa.get_ghiChuHH());

        // Thêm các thông tin hàng hóa khác vào hộp thoại nếu cần

        Button btnHuy = dialog.findViewById(R.id.btnHuy);
        btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss(); // Đóng hộp thoại khi nhấn nút đóng
            }
        });

        dialog.show();
    }

}
