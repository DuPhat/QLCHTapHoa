    package com.example.qlcuahangtaphoa;
    import android.app.DatePickerDialog;
    import android.database.Cursor;
    import android.database.sqlite.SQLiteDatabase;
    import android.os.Bundle;
    import android.util.Log;
    import android.view.View;
    import android.widget.Button;
    import android.widget.DatePicker;
    import android.widget.ImageButton;
    import android.widget.TextView;
    import androidx.appcompat.app.AppCompatActivity;
    import com.github.mikephil.charting.charts.BarChart;
    import com.github.mikephil.charting.data.BarData;
    import com.github.mikephil.charting.data.BarDataSet;
    import com.github.mikephil.charting.data.BarEntry;
    import java.util.ArrayList;
    import android.graphics.Color;
    import android.os.Bundle;
    import android.widget.Toast;

    import java.util.Calendar;
    public class ThongKeDonHangActivity extends AppCompatActivity {

        private TextView tvDateBD, tvDateKT,  tvTongThuNhap;
        private ImageButton btnCalendarBD, btnCalendarKT, btnBack;
        private BarChart barChart;
        private Button btnXacNhanTK;
        public  DBHelper helper;
        private MyDatabase myDatabaseInstance;
        private Calendar calendarBD, calendarKT;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_thong_ke_don_hang);

            helper = new DBHelper(this);
            myDatabaseInstance = MyDatabase.getInstance(this);

            btnBack = findViewById(R.id.btnBack);
            updateAppbar();
            // Ánh xạ các thành phần giao diện
            tvDateBD = findViewById(R.id.tvDateBD);
            tvDateKT = findViewById(R.id.tvDateKT);
            tvTongThuNhap = findViewById(R.id.tvTongThuNhap);
            btnCalendarBD = findViewById(R.id.btnCalendarBD);
            btnCalendarKT = findViewById(R.id.btnCalendarKT);
            barChart = findViewById(R.id.bar_chart);

            btnXacNhanTK = findViewById(R.id.btnXacNhanTK);

            btnXacNhanTK.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updateChartAndIncome(); // Gọi phương thức updateChartAndIncome() khi người dùng nhấn nút "Xác nhận"
                    capNhatThongKe(); // Gọi hàm cập nhật thống kê
                }
            });

            btnCalendarBD.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDatePickerDialog(calendarBD, tvDateBD);
                }
            });

            btnCalendarKT.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDatePickerDialog(calendarKT, tvDateKT);
                }
            });

            btnBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });

            // Khởi tạo calendar cho ngày bắt đầu và ngày kết thúc
            calendarBD = Calendar.getInstance();
            calendarKT = Calendar.getInstance();

            // Hiển thị ngày mặc định trên TextView
            updateDateTextViews();
        }
        public void updateAppbar(){
            btnBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });
        }
        // Hiển thị DatePickerDialog để người dùng chọn ngày
        private void showDatePickerDialog(final Calendar calendar, final TextView textView) {
            DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    calendar.set(Calendar.YEAR, year);
                    calendar.set(Calendar.MONTH, monthOfYear);
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    updateDateTextViews();
                }
            };

            new DatePickerDialog(
                    ThongKeDonHangActivity.this,
                    dateSetListener,
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
            ).show();
        }

        // Cập nhật TextView hiển thị ngày
        private void updateDateTextViews() {
            String startDate = formatDate(calendarBD);
            String endDate = formatDate(calendarKT);
            tvDateBD.setText(startDate);
            tvDateKT.setText(endDate);

        }

        // Format ngày thành chuỗi "dd-MM-yyyy"
        private String formatDate(Calendar calendar) {
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            int month = calendar.get(Calendar.MONTH) + 1;
            int year = calendar.get(Calendar.YEAR);
            return String.format("%02d-%02d-%d", day, month, year);
        }

        // Phương thức cập nhật thống kê trong MyDatabase
        private void capNhatThongKe() {
            // Lấy ngày bắt đầu và ngày kết thúc từ TextView
            String ngayBatDau = tvDateBD.getText().toString();
            String ngayKetThuc = tvDateKT.getText().toString();

            // Tính tổng thu nhập
            double tongThuNhap = calculateTotalIncome(ngayBatDau, ngayKetThuc);
            int soLuongDonHang = getSoLuongDonHang(ngayBatDau, ngayKetThuc);

            // Tạo đối tượng ThongKe để chứa thông tin cần cập nhật
            ThongKe thongKe = new ThongKe();
            thongKe.setNgayBatDau(ngayBatDau);
            thongKe.setNgayKetThuc(ngayKetThuc);
            thongKe.setTongDoanhThu(tongThuNhap);
            thongKe.setSoLuongDonHang(soLuongDonHang);

            // Cập nhật thống kê trong MyDatabase
            boolean isSuccess = myDatabaseInstance.capNhatThongKe(thongKe);

            if (isSuccess) {
                Toast.makeText(this, "Cập nhật thống kê thành công", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Cập nhật thống kê thất bại", Toast.LENGTH_SHORT).show();
            }
        }

        // Phương thức lấy số lượng đơn hàng từ cơ sở dữ liệu
        private int getSoLuongDonHang(String startDate, String endDate) {
            SQLiteDatabase database = helper.getReadableDatabase();
            int soLuongDonHang = 0;

            Cursor cursor = database.rawQuery("SELECT COUNT(*) AS SoLuongDonHang FROM " + DBHelper.tenBangDonHang +
                    " WHERE " + DBHelper.thoiGian + " BETWEEN ? AND ?", new String[]{startDate, endDate});

            if (cursor.moveToFirst()) {
                soLuongDonHang = cursor.getInt(cursor.getColumnIndexOrThrow("SoLuongDonHang"));
            }

            cursor.close();
            return soLuongDonHang;
        }


        // Cập nhật biểu đồ và tổng thu nhập
        private void updateChartAndIncome() {
            // Lấy ngày bắt đầu và ngày kết thúc từ TextView
            String ngayBatDau = tvDateBD.getText().toString();
            String ngayKetThuc = tvDateKT.getText().toString();

            // Tính tổng thu nhập
            double tongThuNhap = calculateTotalIncome(ngayBatDau, ngayKetThuc);

            // Cập nhật TextView hiển thị tổng thu nhập
            tvTongThuNhap.setText(String.valueOf(tongThuNhap));

            // Cập nhật biểu đồ
            updateChart(ngayBatDau, ngayKetThuc);
        }
        // Tính tổng thu nhập từ cơ sở dữ liệu
        private double calculateTotalIncome(String startDate, String endDate) {
            Log.d("HelperDebug", "Helper object: " + helper);
            SQLiteDatabase database = helper.getReadableDatabase();
            double tongThuNhap = 0;

            Cursor cursor = database.rawQuery("SELECT SUM(" + DBHelper.tongTien + ") AS TongThuNhap FROM " + DBHelper.tenBangDonHang +
                    " WHERE " + DBHelper.thoiGian + " BETWEEN ? AND ?", new String[]{startDate, endDate});

            if (cursor.moveToFirst()) {
                tongThuNhap = cursor.getDouble(cursor.getColumnIndexOrThrow("TongThuNhap"));
            }

            cursor.close();
            return tongThuNhap;
        }


        // Cập nhật biểu đồ với dữ liệu mới từ cơ sở dữ liệu
        private void updateChart(String startDate, String endDate) {
            // Mở cơ sở dữ liệu để đọc dữ liệu
            Log.d("HelperDebug", "Helper object: " + helper);
            SQLiteDatabase database = helper.getReadableDatabase();

            // Truy vấn dữ liệu từ cơ sở dữ liệu
            Cursor cursor = database.rawQuery("SELECT * FROM " + DBHelper.tenBangThongKe + " WHERE " + DBHelper.ngayBatDau + " >= ? AND " + DBHelper.ngayKetThuc + " <= ?", new String[]{startDate, endDate});

            ArrayList<BarEntry> entriesTongDoanhThu = new ArrayList<>();
            ArrayList<BarEntry> entriesSoLuongDonHang = new ArrayList<>();
            double tongDoanhThu = 0;
            int soLuongDonHang = 0;
            int i = 0;
            while (cursor.moveToNext()) {
                tongDoanhThu = cursor.getDouble(cursor.getColumnIndexOrThrow(DBHelper.tongDoanhThu));
                soLuongDonHang = cursor.getInt(cursor.getColumnIndexOrThrow(DBHelper.soLuongDonHang));

                entriesTongDoanhThu.add(new BarEntry(i, (float) tongDoanhThu));
                entriesSoLuongDonHang.add(new BarEntry(i, (float) soLuongDonHang));
                i++;
            }
            cursor.close();

            BarDataSet barDataSetTongDoanhThu = new BarDataSet(entriesTongDoanhThu, "Tổng Doanh Thu");
            barDataSetTongDoanhThu.setColor(Color.rgb(0, 155, 0));

            BarDataSet barDataSetSoLuongDonHang = new BarDataSet(entriesSoLuongDonHang, "Số Lượng Đơn Hàng");
            barDataSetSoLuongDonHang.setColor(Color.rgb(155, 0, 0));

            BarData barData = new BarData(barDataSetTongDoanhThu, barDataSetSoLuongDonHang);
            barChart.setData(barData);

            // Đảm bảo gọi groupBars sau khi đã thiết lập dữ liệu cho biểu đồ
            float groupSpace = 0.06f;
            float barSpace = 0.02f;
            float barWidth = 0.45f;
            barData.setBarWidth(barWidth);
            barChart.groupBars(0f, groupSpace, barSpace);

            barChart.getDescription().setEnabled(false);
            barChart.setFitBars(true);
            barChart.invalidate(); // Refresh chart
        }
    }