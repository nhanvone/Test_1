/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.DuAn1.DAO;

import com.DuAn1.Entity.LoaiSanPham;
import com.DuAn1.Entity.SanPham;
import com.DuAn1.JDBCHelper.JDBCHelper;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Hoàng Nhân
 */
public class LoaiSanPhamDAO extends QuanLyTraSuaDAO<LoaiSanPham, String> {

    String insert = "INSERT INTO LoaiSanPham(TenLoaiSanPham) VALUES(?)";
    String update = "UPDATE LoaiSanPham SET TenLoaiSanPham = ? WHERE MaLoaiSanPham = ?";
    String delete = "DELETE FROM LoaiSanPham WHERE MaLoaiSanPham = ?";
    String selectAll = "SELECT * FROM LoaiSanPham";
    String selectbyId = "SELECT *FROM LoaiSanPham WHERE MaLoaiSanPham = ?";
    String selectbyTen = "SELECT *FROM LoaiSanPham WHERE TenLoaiSanPham = ?";

    public void insert(LoaiSanPham entity) {
        JDBCHelper.update(insert, entity.getMaLoaiSP(), entity.getTenLoaiSP());
    }

    @Override
    public void update(LoaiSanPham entity) {
        JDBCHelper.update(update, entity.getTenLoaiSP(), entity.getMaLoaiSP());
    }

    @Override
    public void delete(String id) {
        JDBCHelper.update(delete, id);
    }

    @Override
    public LoaiSanPham selectbyId(String id) {
        List<LoaiSanPham> list = this.selectbySql(selectbyId, id);
        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }
    public LoaiSanPham selectbyTen(String Ten) {
        List<LoaiSanPham> list = this.selectbySql(selectbyTen, Ten);
        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

    @Override
    public List<LoaiSanPham> selectAll() {
        return this.selectbySql(selectAll);
    }

    @Override
    protected List<LoaiSanPham> selectbySql(String sql, Object... args) {
        List<LoaiSanPham> list = new ArrayList<LoaiSanPham>();
        try {
            ResultSet rs = JDBCHelper.query(sql, args);
            while (rs.next()) {
                LoaiSanPham lsp = new LoaiSanPham();
                lsp.setMaLoaiSP(rs.getInt("MaLoaiSanPham"));
                lsp.setTenLoaiSP(rs.getString("TenLoaiSanPham"));
                list.add(lsp);
            }

            rs.getStatement().getConnection().close();
            return list;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
