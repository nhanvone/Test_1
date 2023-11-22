/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.DuAn1.Entity;


public class SanPham {
    private int maSP;
    private String tenSP;
    private int maLoaiSP;
    private float gia;
    private String donViTinh;
    private String hinh;
    private String TenLoai;

    public SanPham() {
    }

    public SanPham(int maSP, String tenSP, int maLoaiSP, float gia, String donViTinh, String hinh) {
        this.maSP = maSP;
        this.tenSP = tenSP;
        this.maLoaiSP = maLoaiSP;
        this.gia = gia;
        this.donViTinh = donViTinh;
        this.hinh = hinh;
    }

    public int getMaSP() {
        return maSP;
    }

    public String getTenSP() {
        return tenSP;
    }

    public int getMaLoaiSP() {
        return maLoaiSP;
    }

    public String getTenLoai() {
        return TenLoai;
    }

    public void setTenLoai(String TenLoai) {
        this.TenLoai = TenLoai;
    }
    

    public float getGia() {
        return gia;
    }

    public String getDonViTinh() {
        return donViTinh;
    }

    public String getHinh() {
        return hinh;
    }

    public void setMaSP(int maSP) {
        this.maSP = maSP;
    }

    public void setTenSP(String tenSP) {
        this.tenSP = tenSP;
    }

    public void setMaLoaiSP(int maLoaiSP) {
        this.maLoaiSP = maLoaiSP;
    }

    public void setGia(float gia) {
        this.gia = gia;
    }

    public void setDonViTinh(String donViTinh) {
        this.donViTinh = donViTinh;
    }

    public void setHinh(String hinh) {
        this.hinh = hinh;
    }
    
    
}
