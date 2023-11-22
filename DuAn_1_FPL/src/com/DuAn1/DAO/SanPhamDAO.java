/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.DuAn1.DAO;

import com.DuAn1.JDBCHelper.JDBCHelper;
import com.DuAn1.Entity.SanPham;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author sonmi
 */
public class SanPhamDAO extends QuanLyTraSuaDAO<SanPham, Integer> {

//    String insert = "INSERT INTO SanPham(TenSanPham, MaLoaiSanPham, Gia, DonViTinh, Hinh) VALUES (?, ?, ?, ?, ?)";
    String insert = "INSERT INTO SanPham(TenSanPham, MaLoaiSanPham, Gia, DonViTinh, Hinh) VALUES (?, (SELECT MaLoaiSanPham FROM LoaiSanPham WHERE TenLoaiSanPham = ?), ?, ?, ?)";
    String selectbyName = "SELECT * FROM SanPham WHERE TenSanPham = ?";
    String update = "UPDATE SanPham SET TenSanPham = ?, MaLoaiSanPham = ?, Gia = ?, DonViTinh = ?, Hinh = ? WHERE MaSanPham = ?";
    String delete = "DELETE FROM SanPham WHERE MaSanPham = ?";
    String selectAll = "SELECT * FROM SanPham";
    String selectbyId = "SELECT * FROM SanPham WHERE MaSanPham = ?";

    @Override
    public void insert(SanPham entity) {
        JDBCHelper.update(insert, entity.getTenSP(), entity.getTenLoai(), entity.getGia(),
                entity.getDonViTinh(), entity.getHinh());
    }

    @Override
    public void update(SanPham entity) {
        JDBCHelper.update(update, entity.getTenSP(), entity.getMaLoaiSP(), entity.getGia(),
                entity.getDonViTinh(), entity.getHinh(), entity.getMaSP());
    }

    @Override
    public void delete(Integer id) {
        JDBCHelper.update(delete, id);
    }

    @Override
    public SanPham selectbyId(Integer id) {
        List<SanPham> list = this.selectbySql(selectbyId, id);
        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

    // Update your DAO method to return a List<SanPham>
    public List<SanPham> selectbyName(String name) {
        return this.selectbySql(selectbyName, name);
    }

    @Override
    public List<SanPham> selectAll() {
        return this.selectbySql(selectAll);
    }

    @Override
    protected List<SanPham> selectbySql(String sql, Object... args) {
        List<SanPham> list = new ArrayList<>();
        try {
            ResultSet rs = JDBCHelper.query(sql, args);
            while (rs.next()) {
                SanPham sp = new SanPham();
                sp.setMaSP(rs.getInt("MaSanPham"));
                sp.setTenSP(rs.getString("TenSanPham"));
                sp.setMaLoaiSP(rs.getInt("MaLoaiSanPham"));
                sp.setGia(rs.getFloat("Gia"));
                sp.setDonViTinh(rs.getString("DonViTinh"));
                sp.setHinh(rs.getString("Hinh"));
                list.add(sp);
            }

            rs.getStatement().getConnection().close();
            return list;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
