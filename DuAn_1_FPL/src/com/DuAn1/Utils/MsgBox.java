
package com.DuAn1.Utils;

import java.awt.Component;
import javax.swing.JOptionPane;

public class MsgBox {
   // Phương thức alert dùng để hiển thị hộp thoại thông báo đơn giản với một tin nhắn
public static void alert(Component parent, String message) {
    JOptionPane.showMessageDialog(parent, message, "Hệ thống quản lý đào tạo", JOptionPane.INFORMATION_MESSAGE);
}

// Phương thức confirm dùng để hiển thị hộp thoại xác nhận với một câu hỏi và hai nút Yes và No
// Nó trả về true nếu người dùng chọn Yes và false nếu chọn No
public static boolean confirm(Component parent, String message) {
    int result = JOptionPane.showConfirmDialog(parent, message, "Hệ thống quản lý đào tạo", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
    return result == JOptionPane.YES_OPTION;
}

// Phương thức prompt dùng để hiển thị hộp thoại nhắc nhở để người dùng nhập một chuỗi dữ liệu
// Nó trả về chuỗi người dùng nhập hoặc null nếu người dùng hủy bỏ hoặc đóng hộp thoại
public static String prompt(Component parent, String message) {
    return JOptionPane.showInputDialog(parent, message, "Hệ thống quản lý đào tạo", JOptionPane.INFORMATION_MESSAGE);
}
}
