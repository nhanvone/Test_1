/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.DuAn1.View;

import com.DuAn1.DAO.LoaiSanPhamDAO;
import com.DuAn1.DAO.NhanVienDAO;
import com.DuAn1.DAO.SanPhamDAO;
import com.DuAn1.Entity.LoaiSanPham;
import com.DuAn1.Entity.NhanVien;
import com.DuAn1.Entity.SanPham;
import java.util.List;

/**
 *
 * @author Hoàng Nhân
 */
public class test {

    public static void main(String[] args) {
//        NhanVienDAO dao = new NhanVienDAO();
//        List<NhanVien> ls = dao.selectAll();
//        for (NhanVien nv : ls) {
//            System.out.println("Mã nhân viên: " + nv.getMaNV());
//            System.out.println("Tên nhân viên: " + nv.getTenNV());
//            System.out.println("Ngày sinh: " + nv.getNgaySinhDate());
//            System.out.println("Giới tính: " + nv.getGoiTinh());
//            System.out.println("Số điện thoại: " + nv.getSoDienThoai());
//            System.out.println("Ngày vào làm: " + nv.getNgayVaoLamDate());
//            System.out.println("Chức vụ: " + nv.getChucVu());
//            System.out.println("Tài khoản: " + nv.getTaiKhoan());
//            System.out.println("Mật khẩu: " + nv.getMatKhau());
//            System.out.println("Hình: " + nv.getHinh());
//            System.out.println("Roles: " + nv.isRoles());
//            System.out.println("-------------------------------");
//
//        }
        String name = "Trà Olong";
        SanPhamDAO dao = new SanPhamDAO();
        List<SanPham> ls = dao.selectbyName(name);

// Print or process the list of SanPham objects
        System.out.println(ls);
//        LoaiSanPhamDAO dao = new LoaiSanPhamDAO();
//        List<LoaiSanPham> ls = dao.selectAll();
//        for (LoaiSanPham sp : ls) {
//            System.out.println(sp.getTenLoaiSP());
//        }

    }
}
