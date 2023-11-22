/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.DuAn1.DAO;

import com.DuAn1.JDBCHelper.JDBCHelper;
import com.DuAn1.Entity.NhanVien;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author sonmi
 */
public class NhanVienDAO extends QuanLyTraSuaDAO<NhanVien, Integer> {

    // Các phương thức và truy vấn SQL đã sửa đổi để phù hợp với mã sản phẩm là kiểu int
    String insert = "INSERT INTO NhanVien(TenNhanVien, NgaySinh, GioiTinh, SoDienThoai, NgayVaoLam, ChucVu, TenTaiKhoan, MatKhau, Hinh) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
    String update = "UPDATE NhanVien SET TenNhanVien = ?, NgaySinh = ?, GioiTinh = ?, SoDienThoai = ?, NgayVaoLam = ?, ChucVu = ?, TenTaiKhoan = ?, MatKhau = ?, Hinh = ? WHERE MaNhanVien = ?";
    String delete = "DELETE FROM NhanVien WHERE MaNhanVien = ?";
    String selectAll = "SELECT * FROM NhanVien";
    String selectbyId = "SELECT * FROM NhanVien WHERE MaNhanVien = ?";
    String selectbyTK = "SELECT * FROM NhanVien WHERE TenTaiKhoan = ?";
    @Override
    public void insert(NhanVien entity) {
        JDBCHelper.update(insert, entity.getTenNV(), entity.getNgaySinhDate(), entity.isGioiTinh(),
                entity.getSoDienThoai(), entity.getNgayVaoLamDate(), entity.getChucVu(), entity.getTaiKhoan(),
                entity.getMatKhau(), entity.getHinh());
    }

    @Override
    public void update(NhanVien entity) {
        JDBCHelper.update(update, entity.getTenNV(), entity.getNgaySinhDate(), entity.isGioiTinh(),
                entity.getSoDienThoai(), entity.getNgayVaoLamDate(), entity.getChucVu(), entity.getTaiKhoan(),
                entity.getMatKhau(), entity.getHinh(), entity.getMaNV());
    }

    @Override
    public void delete(Integer id) {
        JDBCHelper.update(delete, id);
    }

    @Override
    public NhanVien selectbyId(Integer id) {
        List<NhanVien> list = this.selectbySql(selectbyId, id);
        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }
    public NhanVien selectbyTK(String taiKhoan) {
        List<NhanVien> list = this.selectbySql(selectbyTK, taiKhoan);
        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }
    @Override
    public List<NhanVien> selectAll() {
        return this.selectbySql(selectAll);
    }

    @Override
    protected List<NhanVien> selectbySql(String sql, Object... args) {
        List<NhanVien> list = new ArrayList<NhanVien>();
        try {
            ResultSet rs = JDBCHelper.query(sql, args);
            while (rs.next()) {
                NhanVien nv = new NhanVien();
                nv.setMaNV(rs.getInt("MaNhanVien"));
                nv.setTenNV(rs.getString("TenNhanVien"));
                nv.setNgaySinhDate(rs.getDate("NgaySinh"));
                nv.setGioiTinh(rs.getBoolean("GioiTinh"));
                nv.setSoDienThoai(rs.getString("SoDienThoai"));
                nv.setNgayVaoLamDate(rs.getDate("NgayVaoLam"));
                nv.setChucVu(rs.getString("ChucVu"));
                nv.setTaiKhoan(rs.getString("TenTaiKhoan"));
                nv.setMatKhau(rs.getString("MatKhau"));
                nv.setHinh(rs.getString("Hinh"));
                nv.setRoles(rs.getBoolean("Roles"));
                list.add(nv);
            }
            rs.getStatement().getConnection().close();
            return list;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
