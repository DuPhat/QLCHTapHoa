    package com.example.qlcuahangtaphoa;

    public class DonHang {
        private int _maDH;
        private int _maKH;
        private int _maHH;
        private String _tendh;
        private String _thoiGian;
        private Double _phiGiao;
        private String _trangThai;
        private Double _tongTien;
        private String _ghichu;
        private String _pTThanhToan;

        public int get_maDH() {
            return _maDH;
        }

        public void set_maDH(int _maDH) {
            this._maDH = _maDH;
        }

        public int get_maKH() {
            return _maKH;
        }

        public void set_maKH(int _maKH) {
            this._maKH = _maKH;
        }

        public int get_maHH() {
            return _maHH;
        }

        public void set_maHH(int _maHH) {
            this._maHH = _maHH;
        }

        public String get_tendh() {
            return _tendh;
        }

        public void set_tendh(String _tendh) {
            this._tendh = _tendh;
        }

        public String get_thoiGian() {
            return _thoiGian;
        }

        public void set_thoiGian(String _thoiGian) {
            this._thoiGian = _thoiGian;
        }

        public Double get_phiGiao() {
            return _phiGiao;
        }

        public void set_phiGiao(Double _phiGiao) {
            this._phiGiao = _phiGiao;
        }

        public String get_trangThai() {
            return _trangThai;
        }

        public void set_trangThai(String _trangThai) {
            this._trangThai = _trangThai;
        }

        public Double get_tongTien() {
            return _tongTien;
        }

        public void set_tongTien(Double _tongTien) {
            this._tongTien = _tongTien;
        }

        public String get_ghichu() {
            return _ghichu;
        }

        public void set_ghichu(String _ghichu) {
            this._ghichu = _ghichu;
        }

        public String get_pTThanhToan() {
            return _pTThanhToan;
        }

        public void set_pTThanhToan(String _pTThanhToan) {
            this._pTThanhToan = _pTThanhToan;
        }

        public DonHang() {
        }

        public DonHang(int _maDH, int _maKH, int _maHH, String _tendh, String _thoiGian, Double _phiGiao, String _trangThai, Double _tongTien, String _ghichu, String _pTThanhToan) {
            this._maDH = _maDH;
            this._maKH = _maKH;
            this._maHH = _maHH;
            this._tendh = _tendh;
            this._thoiGian = _thoiGian;
            this._phiGiao = _phiGiao;
            this._trangThai = _trangThai;
            this._tongTien = _tongTien;
            this._ghichu = _ghichu;
            this._pTThanhToan = _pTThanhToan;
        }
    }
