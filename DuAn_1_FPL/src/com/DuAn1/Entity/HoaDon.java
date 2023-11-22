/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.DuAn1.Entity;

import java.util.Date;

/**
 *
 * @author sonmi
 */
public class HoaDon {
    private int maHD;
    private int maHDCT;
    private int soLuong;
    private Date ngayGoiMon;
    private String thoiGianGoiMon;
    private int soHoaDon;

    public HoaDon(int maHD, int maHDCT, int soLuong, Date ngayGoiMon, String thoiGianGoiMon, int soHoaDon) {
        this.maHD = maHD;
        this.maHDCT = maHDCT;
        this.soLuong = soLuong;
        this.ngayGoiMon = ngayGoiMon;
        this.thoiGianGoiMon = thoiGianGoiMon;
        this.soHoaDon = soHoaDon;
    }

    public HoaDon() {
    }

    public int getMaHD() {
        return maHD;
    }

    public int getMaHDCT() {
        return maHDCT;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public Date getNgayGoiMon() {
        return ngayGoiMon;
    }

    public String getThoiGianGoiMon() {
        return thoiGianGoiMon;
    }

    public int getSoHoaDon() {
        return soHoaDon;
    }

    public void setMaHD(int maHD) {
        this.maHD = maHD;
    }

    public void setMaHDCT(int maHDCT) {
        this.maHDCT = maHDCT;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    public void setNgayGoiMon(Date ngayGoiMon) {
        this.ngayGoiMon = ngayGoiMon;
    }

    public void setThoiGianGoiMon(String thoiGianGoiMon) {
        this.thoiGianGoiMon = thoiGianGoiMon;
    }

    public void setSoHoaDon(int soHoaDon) {
        this.soHoaDon = soHoaDon;
    }
    
    
}
