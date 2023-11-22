/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.DuAn1.DAO;

import java.util.List;

/**
 *
 * @author sonmi
 */
abstract public class QuanLyTraSuaDAO<EntytiType, KeyTepy> {
        // Khai báo một lớp trừu tượng có tên EduSysDAO với hai tham số kiểu dữ liệu generic.

    abstract public void insert(EntytiType entity);
    // Khai báo một phương thức trừu tượng 'insert', không có thân hàm, để chèn một đối tượng 'entity' vào cơ sở dữ liệu.

    abstract public void update(EntytiType entity);
    // Khai báo một phương thức trừu tượng 'update', không có thân hàm, để cập nhật thông tin của một đối tượng 'entity' trong cơ sở dữ liệu.

    abstract public void delete(KeyTepy id);
    // Khai báo một phương thức trừu tượng 'delete', không có thân hàm, để xóa một đối tượng dựa trên khóa chính 'id'.

    abstract public EntytiType selectbyId(KeyTepy id);
    // Khai báo một phương thức trừu tượng 'selectbyId', không có thân hàm, để lấy một đối tượng 'EntytiType' dựa trên khóa chính 'id'.

    abstract public List<EntytiType> selectAll();
    // Khai báo một phương thức trừu tượng 'selectAll', không có thân hàm, để lấy danh sách tất cả các đối tượng 'EntytiType' từ cơ sở dữ liệu.

    abstract protected List<EntytiType> selectbySql(String sql, Object... args);
    // Khai báo một phương thức trừu tượng 'selectbySql', không có thân hàm, để thực hiện truy vấn SQL chung và trả về danh sách các đối tượng 'EntytiType'. 
    // Các tham số 'sql' và 'args' được sử dụng để xây dựng và thực thi truy vấn SQL.
    
}
