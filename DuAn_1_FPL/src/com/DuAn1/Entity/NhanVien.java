/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.DuAn1.Entity;

import java.sql.Date;

/**
 *
 * @author sonmi
 */
public class NhanVien {
    private int maNV;
    private String tenNV;
    private Date ngaySinhDate;
    private boolean gioiTinh;
    private String soDienThoai;
    private Date ngayVaoLamDate;
    private String chucVu;
    private String taiKhoan;
    private String matKhau;
    private String hinh;
    private Boolean Roles;

    public NhanVien() {
    }

    public NhanVien(int maNV, String tenNV, Date ngaySinhDate, boolean gioiTinh, String soDienThoai, Date ngayVaoLamDate, String chucVu, String taiKhoan, String matKhau, String hinh, Boolean Roles) {
        this.maNV = maNV;
        this.tenNV = tenNV;
        this.ngaySinhDate = ngaySinhDate;
        this.gioiTinh = gioiTinh;
        this.soDienThoai = soDienThoai;
        this.ngayVaoLamDate = ngayVaoLamDate;
        this.chucVu = chucVu;
        this.taiKhoan = taiKhoan;
        this.matKhau = matKhau;
        this.hinh = hinh;
        this.Roles = Roles;
    }

    public int getMaNV() {
        return maNV;
    }

    public void setMaNV(int maNV) {
        this.maNV = maNV;
    }

    public String getTenNV() {
        return tenNV;
    }

    public void setTenNV(String tenNV) {
        this.tenNV = tenNV;
    }

    public Date getNgaySinhDate() {
        return ngaySinhDate;
    }

    public void setNgaySinhDate(Date ngaySinhDate) {
        this.ngaySinhDate = ngaySinhDate;
    }

    public boolean isGioiTinh() {
        return gioiTinh;
    }

    public void setGioiTinh(boolean gioiTinh) {
        this.gioiTinh = gioiTinh;
    }

    public String getSoDienThoai() {
        return soDienThoai;
    }

    public void setSoDienThoai(String soDienThoai) {
        this.soDienThoai = soDienThoai;
    }

    public Date getNgayVaoLamDate() {
        return ngayVaoLamDate;
    }

    public void setNgayVaoLamDate(Date ngayVaoLamDate) {
        this.ngayVaoLamDate = ngayVaoLamDate;
    }

    public String getChucVu() {
        return chucVu;
    }

    public void setChucVu(String chucVu) {
        this.chucVu = chucVu;
    }

    public String getTaiKhoan() {
        return taiKhoan;
    }

    public void setTaiKhoan(String taiKhoan) {
        this.taiKhoan = taiKhoan;
    }

    public String getMatKhau() {
        return matKhau;
    }

    public void setMatKhau(String matKhau) {
        this.matKhau = matKhau;
    }

    public String getHinh() {
        return hinh;
    }

    public void setHinh(String hinh) {
        this.hinh = hinh;
    }

    public Boolean getRoles() {
        return Roles;
    }
    
    
    public void setRoles(Boolean Roles) {
        this.Roles = Roles;
    }

    
    
}
