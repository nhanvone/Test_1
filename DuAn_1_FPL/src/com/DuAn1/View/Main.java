package com.DuAn1.View;

import com.DuAn1.DAO.LoaiSanPhamDAO;
import com.DuAn1.DAO.NhanVienDAO;
import com.DuAn1.DAO.SanPhamDAO;
import com.DuAn1.Entity.LoaiSanPham;
import com.DuAn1.Entity.NhanVien;
import com.DuAn1.Entity.SanPham;
import com.DuAn1.JDBCHelper.JDBCHelper;
import com.DuAn1.Utils.MsgBox;
import com.DuAn1.Utils.XImage;
import com.toedter.calendar.JDateChooser;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.RowFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;
import java.sql.Connection;
import java.util.Calendar;
import javax.swing.SwingUtilities;

/**
 *
 * @author Hoàng Nhân
 */
public class Main extends javax.swing.JFrame {

    int with = 260;
    int hight = 676;
    Color color = new Color(251, 224, 213, 255);
    private SanPhamDAO SPdao = new SanPhamDAO();
    private LoaiSanPhamDAO Ldao = new LoaiSanPhamDAO();
    private List<SanPham> list; // Thêm danh sách để lấy dữ liệu từ DAO
    private NhanVienDAO NVDAO = new NhanVienDAO();
    private List<NhanVien> listNV;
    private int row = 0;

    public Main() {
        initComponents();
        init();
        fillTable_FromSanPham();
        doDuLieu_FromSanPham();
        fillComboboxBanh_FromSanPham();
        doDuLieuNV_FromNhanVien();
        fillTableNV_FromNhanVien();
    }

    public void init() {
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setIconImage(XImage.getAppIcon());
//        this.openWelcome();
//      this.offLogin();
        this.openLogin();
    }

    void openWelcome() {
        new LoadingDialog(this, true).setVisible(true);
    }

    void openLogin() {
        new LoginDialog(this, true).setVisible(true);

    }

    private void ThongBaoLoi(String errorMessage) {
        LoiFrame loi = new LoiFrame();
        loi.setErrorMessage(errorMessage);
        loi.show();
    }

    private void ThongBaoThanhCong(String errorMessage) {
        ThanhCongFrame Tc = new ThanhCongFrame();
        Tc.setErrorMessage(errorMessage);
        Tc.show();
    }
//Khu vực của sản phẩm

    public void doDuLieu_FromSanPham() {
        list = SPdao.selectAll();
        fillTable_FromSanPham();
        fillComboboxBanh_FromSanPham();
        row = 0;
        capNhatTrangThai_FromSanPham();

    }

    private void fillTable_FromSanPham() {
        DefaultTableModel modelSP = (DefaultTableModel) tblSP.getModel();
        modelSP.setRowCount(0); // Xóa dữ liệu cũ trước khi thêm mới

        // Lấy danh sách sản phẩm từ SanPhamDAO
        List<SanPham> list = SPdao.selectAll(); // hoặc có thể là Ldao.selectAll() tùy theo nhu cầu

        try {
            if (list != null && !list.isEmpty()) { // Kiểm tra list có dữ liệu không
                for (SanPham sp : list) {
                    modelSP.addRow(new Object[]{
                        sp.getMaSP(), sp.getTenSP(), sp.getMaLoaiSP(), sp.getGia(), sp.getDonViTinh(), sp.getHinh()
                    });
                }
            } else {
                MsgBox.alert(this, "Dữ liệu sản phẩm trống!");
            }
        } catch (Exception e) {
            MsgBox.alert(this, "Lỗi truy vấn dữ liệu !");
            e.printStackTrace();
        }
    }

    //Đổ loại vào combobox
    private void fillComboboxBanh_FromSanPham() {
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>(); //Tạo model cho conmobox
        cboLoai.setModel(model);
        List<LoaiSanPham> list = Ldao.selectAll(); //Lấy dữ liệu từ bảng loại sản phẩm
        for (LoaiSanPham lsp : list) {
            //Đổ dữ liệu ra
            model.addElement(lsp.getTenLoaiSP());
        }
    }

    private void setForm_SanPham(SanPham sp) {
        // Assuming sp.getMaLoaiSP() returns the LoaiSanPham ID as a string
        cboLoai.setSelectedItem(sp.getMaLoaiSP());
        txtTenSP.setText(sp.getTenSP());
        txtDonVi.setText(sp.getDonViTinh());
        txtGiaTien.setText(String.valueOf(sp.getGia()));
    }

//    private SanPham getForm_SanPham() {
//    private SanPham getForm() {
//        SanPham sp = new SanPham();
//
//        String LayGiatri = cboLoai.getSelectedItem().toString();
//        try {
//            int maLoaiSP = Integer.parseInt(LayGiatri);
//            sp.setMaLoaiSP(maLoaiSP);
//        } catch (NumberFormatException e) {
//            // Xử lý khi chuỗi không thể chuyển đổi thành số nguyên
//            e.printStackTrace(); // In thông báo lỗi (có thể thay thế bằng hành động phù hợp khác)
//        }
//
//        sp.setTenSP(txtTenSP.getText());
//        sp.setDonViTinh(txtDonVi.getText());
//        sp.setGia(Float.parseFloat(txtGiaTien.getText()));
//        return sp;
//    }
    private SanPham getForm() {
        SanPham sp = new SanPham();
        LoaiSanPham loai = new LoaiSanPham();
        String LayGiatri = cboLoai.getSelectedItem().toString();
        try {
//            // Kiểm tra và chuyển đổi chuỗi thành số nguyên
            if (!LayGiatri.isEmpty()) { // Kiểm tra chuỗi có chứa ký tự số hay không
                sp.setTenLoai(LayGiatri);
            } else {
                // Xử lý khi chuỗi không chứa số nguyên
                // Thông báo lỗi hoặc xử lý theo cách phù hợp
                // Ví dụ: MsgBox.alert(this, "Giá trị không hợp lệ!");
                // hoặc cập nhật giá trị mặc định hoặc không cập nhật gì cả
            }
//            sp.setMaLoaiSP(maLoaiSP);

            sp.setTenSP(txtTenSP.getText());
            sp.setDonViTinh(txtDonVi.getText());

            // Kiểm tra và chuyển đổi chuỗi thành số thực
            if (txtGiaTien.getText().matches("^\\d*\\.?\\d*$")) { // Kiểm tra chuỗi có dạng số thực hay không
                float gia = Float.parseFloat(txtGiaTien.getText());
                sp.setGia(gia);
            } else {
                // Xử lý khi chuỗi không chứa số thực
                // Thông báo lỗi hoặc xử lý theo cách phù hợp
                // Ví dụ: MsgBox.alert(this, "Giá tiền không hợp lệ!");
                // hoặc cập nhật giá trị mặc định hoặc không cập nhật gì cả
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
            System.out.println("Không thể chuyển đổi thành số nguyên: " + cboLoai.getSelectedItem());
            // Xử lý ngoại lệ khi chuyển đổi số
        }

        return sp;
    }

    private void capNhatTrangThai_FromSanPham() {
        boolean edit = this.row >= 0;
        boolean first = this.row == 0;
        boolean last = this.row == tblSP.getRowCount() - 1;
        // Trạng thái form
        cboLoai.setEditable(!edit);
        txtTenSP.setEditable(!edit);
        txtDonVi.setEditable(!edit);
        txtGiaTien.setEditable(!edit);
        btnThem.setEnabled(!edit);
        btnSua.setEnabled(!edit);
    }
//Làm mới tất cả

    private void LamMoi_FromSanPham() {
        cboLoai.setSelectedIndex(0);
        txtTenSP.setText("");
        txtDonVi.setText("");
        txtGiaTien.setText("0.0");
        this.row = -1;
        this.Img_FromSanPham();
        this.capNhatTrangThai_FromSanPham();

    }
//Kiểm tra khi thêm

    private boolean KiemTraSP_FromSanPham() {
        if (txtTenSP.getText().isEmpty() || txtGiaTien.getText().isEmpty()
                || txtDonVi.getText().isEmpty() || cboLoai.getSelectedIndex() == -1) {
            ThongBaoLoi("Không được bỏ trống dữ liệu");
            return false;
        }
        String giaTien = txtGiaTien.getText();
        try {
            Float.parseFloat(giaTien);
        } catch (Exception e) {
            ThongBaoLoi("Tiền phải là số :<");
            return false;
        }
        return true;
    }

    private void them_FromSanPham() {
        SanPham bm = getForm();
        try {
            SPdao.insert(bm);
            ThongBaoThanhCong("Thêm thành công");
            this.LamMoi_FromSanPham();
            doDuLieu_FromSanPham();
            this.fillTable_FromSanPham();
            return;
        } catch (Exception e) {
            e.printStackTrace();
            ThongBaoLoi("Thêm thất bại :<");

            return;
        }
    }

    private void capNhat_FromSanPham() {
        SanPham bm = getForm();
        try {
            SPdao.update(bm);
            this.fillTable_FromSanPham();
            ThongBaoThanhCong("Cập nhật thành công");
            doDuLieu_FromSanPham();
            return;
        } catch (Exception e) {
            ThongBaoLoi("Cập nhật thất bại :<");
        }
    }

    private void xoa_FromSanPham() {
        this.row = tblSP.getSelectedRow();
        int maBanh = (int) tblSP.getValueAt(row, 0);

//        int maBanh = (int) tblSP.getValueAt(row, 0);
        Object value = tblSP.getValueAt(row, 0);
        System.out.println("Class of the value: " + value.toString());

//        System.out.println(maBanh);
        if (MsgBox.confirm(this, "Bạn thực sự muốn xóa bánh này ?")) {
            try {
                SPdao.delete(maBanh);

                this.LamMoi_FromSanPham();
                ThongBaoThanhCong("Xoá thành công");
                this.fillTable_FromSanPham();
                doDuLieu_FromSanPham();
                return;
            } catch (Exception e) {
                e.printStackTrace();
                ThongBaoLoi("Xoá thất bại :<");
            }
        }
    }

    private void sua_FromSanPham() {
        int maBanh = (int) tblSP.getValueAt(row, 0);
        SanPham bm = SPdao.selectbyId(maBanh);
        this.setForm_SanPham(bm);
        this.capNhatTrangThai_FromSanPham();
    }

    private void Img_FromSanPham() {
        ImageIcon icon = XImage.read("default_img.png");
        lblHinh.setIcon(icon);
        lblHinh.setToolTipText("default_img.png");
    }

    private void chonAnh_FromSanPham() {
        JFileChooser fchooser = new JFileChooser();
        if (fchooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = fchooser.getSelectedFile();
            XImage.save(file);
            ImageIcon icon = XImage.read(file.getName());
            // Scale for img
            Image img = icon.getImage();
            Image imgScale = img.getScaledInstance(lblHinh.getWidth(), lblHinh.getHeight(), Image.SCALE_SMOOTH);
            ImageIcon scaleIcon = new ImageIcon(imgScale);
            lblHinh.setIcon(scaleIcon);
            lblHinh.setToolTipText(file.getName());
        }
    }

    //Khu vực Nhân Viên
    public void doDuLieuNV_FromNhanVien() {
        listNV = NVDAO.selectAll();
        fillTable_FromSanPham();
        row = 0;
        TrangThaiNV_FromNhanVien();

    }

    private void fillTableNV_FromNhanVien() {
        DefaultTableModel modelNV = (DefaultTableModel) tblNhanVien.getModel();
        modelNV.setRowCount(0); // Xóa dữ liệu cũ trước khi thêm mới

        // Lấy danh sách nhân viên từ nhanVienDAO
        List<NhanVien> list = NVDAO.selectAll(); // hoặc có thể là Ldao.selectAll() tùy theo nhu cầu

        try {
            if (list != null && !list.isEmpty()) { // Kiểm tra list có dữ liệu không
                for (NhanVien nv : list) {
                    modelNV.addRow(new Object[]{
                        nv.getMaNV(), nv.getTenNV(), nv.getNgaySinhDate(), nv.isGioiTinh(), nv.getSoDienThoai(),
                        nv.getNgayVaoLamDate(), nv.getChucVu(), nv.getTaiKhoan(), nv.getMatKhau(), nv.getHinh()
                    });
                }
            } else {
                ThongBaoLoi("Dữ liệu nhân viên trống :<");
            }
        } catch (Exception e) {
            MsgBox.alert(this, "Lỗi truy vấn dữ liệu !");
            e.printStackTrace();
        }
    }

    private void setFormNV(NhanVien nv) {
        txtHoTenNV.setText(nv.getTenNV());
        // Chuyển đổi giới tính
        if (nv.isGioiTinh()) {
            rdoNam.setSelected(true);
            rdoNu.setSelected(false);
        } else {
            rdoNam.setSelected(false);
            rdoNu.setSelected(true);
        }

        // Chuyển đổi ngày sinh
        if (nv.getNgaySinhDate() != null) {
            jdcNgaySinhNV.setDate(new java.util.Date(nv.getNgaySinhDate().getTime()));
        }

        txtSoDTNV.setText(nv.getSoDienThoai());

        // Chuyển đổi ngày vào làm
        if (nv.getNgayVaoLamDate() != null) {
            jdcNgayVLNhanVien.setDate(new java.util.Date(nv.getNgayVaoLamDate().getTime()));
        }

        cboChucVu.setSelectedItem(nv.getChucVu());
        txtTaiKhoanNV.setText(nv.getTaiKhoan());
        txtMKNV.setText(nv.getMatKhau());
        ImageIcon icon = XImage.read(nv.getHinh());
        Image img = icon.getImage();
        Image imgScale = img.getScaledInstance(lblHinh.getWidth(), lblHinh.getHeight(), Image.SCALE_SMOOTH);
        ImageIcon scaleicon = new ImageIcon(imgScale);
        lblHinh.setToolTipText(nv.getHinh());
        lblHinh.setIcon(scaleicon);
    }

    private NhanVien getFormNV() {
        NhanVien nv = new NhanVien();
        nv.setTenNV(txtHoTenNV.getText());

        // Đọc giới tính từ radio buttons
        nv.setGioiTinh(rdoNam.isSelected());

        // Lấy ngày sinh từ JCalendar
        java.util.Date ngaySinhDate = jdcNgaySinhNV.getDate();
        if (ngaySinhDate != null) {
            nv.setNgaySinhDate(new java.sql.Date(ngaySinhDate.getTime()));
        }

        nv.setSoDienThoai(txtSoDTNV.getText());

        // Lấy ngày vào làm từ JCalendar
        java.util.Date ngayVaoLamDate = jdcNgayVLNhanVien.getDate();
        if (ngayVaoLamDate != null) {
            nv.setNgayVaoLamDate(new java.sql.Date(ngayVaoLamDate.getTime()));
        }

        nv.setChucVu(cboChucVu.getSelectedItem().toString());
        nv.setTaiKhoan(txtTaiKhoanNV.getText());
        nv.setMatKhau(txtMKNV.getText());
        nv.setHinh(lblHinh.getToolTipText());

        return nv;
    }

    private void TrangThaiNV_FromNhanVien() {
        boolean edit = this.row >= 0;
        boolean first = this.row == 0;
        boolean last = this.row == tblNhanVien.getRowCount() - 1;

        // Trạng thái form
        cboChucVu.setEnabled(!edit);
        txtHoTenNV.setEditable(!edit);
        rdoNam.setEnabled(!edit);
        rdoNu.setEnabled(!edit);
        txtSoDTNV.setEditable(!edit);
        jdcNgaySinhNV.setEnabled(!edit);
        jdcNgayVLNhanVien.setEnabled(!edit);
        cboChucVu.setEditable(!edit);
        txtTaiKhoanNV.setEditable(!edit);
        txtMKNV.setEditable(!edit);
        btnThemNV.setEnabled(!edit);
        btnSuaNV.setEnabled(edit);
    }
//Làm mới tất cả

    private void LamMoiNV_FromNhanVien() {
        cboChucVu.setSelectedIndex(0);
        txtHoTenNV.setText("");
        txtSoDTNV.setText("");
        txtTaiKhoanNV.setText("");
        txtMKNV.setText("");

        // Đặt ngày vào làm về ngày hiện tại
        jdcNgayVLNhanVien.setDate(Calendar.getInstance().getTime());
        jdcNgayVLNhanVien.setSelectableDateRange(null, null);

        // Đặt ngày sinh về không có giá trị
        jdcNgaySinhNV.setDate(null);
        jdcNgaySinhNV.setSelectableDateRange(null, null);

        rdoNam.setEnabled(false);
        rdoNu.setEnabled(false);
        this.row = -1;
        this.TrangThaiNV_FromNhanVien();
    }
//Kiểm tra khi thêm

    private boolean KiemTraNV_FromNhanVien() {
        String tenNV = txtHoTenNV.getText();
        String soDT = txtSoDTNV.getText();
        String ngaySinh = jdcNgaySinhNV.getDateFormatString(); // Cần lấy giá trị ngày thực tế, không phải chuỗi định dạng
        String ngayVL = jdcNgayVLNhanVien.getDateFormatString(); // Cần lấy giá trị ngày thực tế, không phải chuỗi định dạng
        String chucVu = cboChucVu.getSelectedItem().toString(); // Lấy giá trị đã chọn từ combobox
        String taiKhoan = txtTaiKhoanNV.getText();
        String matKhau = txtMKNV.getText();

        if (tenNV.isEmpty() || soDT.isEmpty() || ngaySinh.isEmpty() || ngayVL.isEmpty() || taiKhoan.isEmpty() || matKhau.isEmpty()) {
            ThongBaoLoi("Vui lòng nhập đầy đủ thông tin :<");
            return false;
        }
        JDateChooser ngayVaoLamChooser = jdcNgayVLNhanVien;
        JDateChooser namSinhChooser = jdcNgaySinhNV;
        try {
            Integer.parseInt(soDT);
        } catch (NumberFormatException e) {
            ThongBaoLoi("Số điện thoại phải là số :<");
            return false;
        }

        return true;
    }

    private void ThemNV_FromNhanVien() {
        NhanVien nv = getFormNV();
        try {
            NVDAO.insert(nv);
            this.LamMoiNV_FromNhanVien();
            ThongBaoThanhCong("Thêm mới thành công!");
            doDuLieuNV_FromNhanVien();
            this.fillTableNV_FromNhanVien();
            return;
        } catch (Exception e) {
            e.printStackTrace();
            ThongBaoLoi("Thêm mới thất bại :<");
            return;
        }
    }

    private void SuaNV_FromNhanVien() {
        NhanVien nv = getFormNV();
        try {
            NVDAO.update(nv);
            this.fillTableNV_FromNhanVien();
            ThongBaoThanhCong("Cập nhật thành công!");
            doDuLieuNV_FromNhanVien();
            fillTableNV_FromNhanVien();
            return;
        } catch (Exception e) {
            ThongBaoLoi("Cập nhật thất bại :<");
        }
    }

    private void xoaNV_FromNhanVien() {
        this.row = tblNhanVien.getSelectedRow(); // Lấy chỉ số hàng đã chọn;
        if (MsgBox.confirm(this, "Bạn có muốn xoá hay không")) {
            try {
                int maNV = (int) tblNhanVien.getValueAt(row, 0); // Lấy dữ liệu từ hàng đã chọn
                System.out.println(maNV);
                NVDAO.delete(maNV); // Xóa dữ liệu từ cơ sở dữ liệu
                // Cập nhật giao diện người dùng và thông báo thành công
                this.LamMoiNV_FromNhanVien();
                this.ImgNV_FromNhanVien();
                ThongBaoThanhCong("Xoá Thành Công!!");
                doDuLieuNV_FromNhanVien();
                this.fillTableNV_FromNhanVien();
            } catch (Exception e) {
                e.printStackTrace();
                ThongBaoLoi("Xoá Thất bại :<");
            }
        }
    }

    private void suaNV_FromNhanVien() {
        int maNV = (int) tblNhanVien.getValueAt(row, 0);
        NhanVien nv = NVDAO.selectbyId(maNV);
        this.setFormNV(nv);
        this.TrangThaiNV_FromNhanVien();
    }

    private void ImgNV_FromNhanVien() {
        ImageIcon icon = XImage.read("default_img.png");
        lblHinhNV.setIcon(icon);
        lblHinhNV.setToolTipText("default_img.png");
    }

    private void chonAnhNV_FromNhanVien() {
        JFileChooser fchooser = new JFileChooser();
        if (fchooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = fchooser.getSelectedFile();
            XImage.save(file);
            ImageIcon icon = XImage.read(file.getName());
            // Scale for img
            Image img = icon.getImage();
            Image imgScale = img.getScaledInstance(lblHinhNV.getWidth(), lblHinhNV.getHeight(), Image.SCALE_SMOOTH);
            ImageIcon scaleIcon = new ImageIcon(imgScale);
            lblHinhNV.setIcon(scaleIcon);
            lblHinhNV.setToolTipText(file.getName());
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jpm = new javax.swing.JPopupMenu();
        Xoa = new javax.swing.JMenuItem();
        gioitinh = new javax.swing.ButtonGroup();
        jp_nhanVien = new javax.swing.JPopupMenu();
        jpI_XoaNhanVien = new javax.swing.JMenuItem();
        pnMain = new javax.swing.JPanel();
        pnMenu = new javax.swing.JPanel();
        lblBanHang = new javax.swing.JLabel();
        lblSanPham = new javax.swing.JLabel();
        lblNhanVien = new javax.swing.JLabel();
        lblDoanhThu = new javax.swing.JLabel();
        lblGioiTheu = new javax.swing.JLabel();
        lblTroGiup = new javax.swing.JLabel();
        lblDangXuat = new javax.swing.JLabel();
        lblHoaDon = new javax.swing.JLabel();
        lblTongQuan = new javax.swing.JLabel();
        pnHeader = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        lblTenUser = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        lblThu = new javax.swing.JLabel();
        pnMain_from = new javax.swing.JPanel();
        Tong = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        TongDThu = new javax.swing.JPanel();
        Tong_DT_Ngay = new javax.swing.JPanel();
        Tong_HD = new javax.swing.JPanel();
        jplBieuDo = new javax.swing.JPanel();
        BanHang = new javax.swing.JPanel();
        pnThucDon = new javax.swing.JPanel();
        pnHoaDon = new javax.swing.JPanel();
        pnChiTiet_ThuTien = new javax.swing.JPanel();
        txtKhachDua = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txtTraLai = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        rdoInBill = new javax.swing.JRadioButton();
        rdoKhongInBill = new javax.swing.JRadioButton();
        txtTong = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        pnThuTien_BanHang = new javax.swing.JPanel();
        btnXoa_Form_BanHang = new javax.swing.JButton();
        btnThuTien_BanHang = new javax.swing.JButton();
        SanPham = new javax.swing.JPanel();
        pnThemSanPham = new javax.swing.JPanel();
        pnHinhAnh = new javax.swing.JPanel();
        lblHinh = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        txtTenSP = new javax.swing.JTextField();
        txtGiaTien = new javax.swing.JTextField();
        txtDonVi = new javax.swing.JTextField();
        btnThem = new javax.swing.JButton();
        btnSua = new javax.swing.JButton();
        btnLamMoi = new javax.swing.JButton();
        txtTim = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        cboLoai = new javax.swing.JComboBox<>();
        pnDanhSach = new javax.swing.JPanel();
        jSop_SanPham = new javax.swing.JScrollPane();
        tblSP = new javax.swing.JTable();
        NhanVien = new javax.swing.JPanel();
        pnThemNhanVien = new javax.swing.JPanel();
        txtTimNV = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        txtHoTenNV = new javax.swing.JTextField();
        jdcNgaySinhNV = new com.toedter.calendar.JDateChooser();
        txtSoDTNV = new javax.swing.JTextField();
        jdcNgayVLNhanVien = new com.toedter.calendar.JDateChooser();
        txtTaiKhoanNV = new javax.swing.JTextField();
        rdoNam = new javax.swing.JRadioButton();
        rdoNu = new javax.swing.JRadioButton();
        cboChucVu = new javax.swing.JComboBox<>();
        txtMKNV = new javax.swing.JPasswordField();
        btnThemNV = new javax.swing.JButton();
        btnSuaNV = new javax.swing.JButton();
        btnLamMoiNV = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        lblHinhNV = new javax.swing.JLabel();
        pnDanhSachNhanVien = new javax.swing.JPanel();
        jSop_NhanVien = new javax.swing.JScrollPane();
        tblNhanVien = new javax.swing.JTable();
        HoaDon = new javax.swing.JPanel();
        DoanhThu = new javax.swing.JPanel();

        Xoa.setText("Xoá");
        Xoa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                XoaActionPerformed(evt);
            }
        });
        jpm.add(Xoa);

        jpI_XoaNhanVien.setText("Xoá Nhân Viên");
        jpI_XoaNhanVien.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jpI_XoaNhanVienActionPerformed(evt);
            }
        });
        jp_nhanVien.add(jpI_XoaNhanVien);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        pnMain.setBackground(new java.awt.Color(221, 190, 169));
        pnMain.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        pnMenu.setFont(new java.awt.Font("Verdana", 0, 16)); // NOI18N

        lblBanHang.setFont(new java.awt.Font("Verdana", 0, 15)); // NOI18N
        lblBanHang.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblBanHang.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imges/shop.png"))); // NOI18N
        lblBanHang.setText("Bán Hàng");
        lblBanHang.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblBanHangMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                lblBanHangMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                lblBanHangMouseExited(evt);
            }
        });

        lblSanPham.setFont(new java.awt.Font("Verdana", 0, 15)); // NOI18N
        lblSanPham.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblSanPham.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imges/product.png"))); // NOI18N
        lblSanPham.setText("Sản Phẩm");
        lblSanPham.setPreferredSize(new java.awt.Dimension(71, 20));
        lblSanPham.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblSanPhamMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                lblSanPhamMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                lblSanPhamMouseExited(evt);
            }
        });

        lblNhanVien.setFont(new java.awt.Font("Verdana", 0, 15)); // NOI18N
        lblNhanVien.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblNhanVien.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imges/staff.png"))); // NOI18N
        lblNhanVien.setText("Nhân Viên");
        lblNhanVien.setPreferredSize(new java.awt.Dimension(71, 20));
        lblNhanVien.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblNhanVienMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                lblNhanVienMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                lblNhanVienMouseExited(evt);
            }
        });

        lblDoanhThu.setFont(new java.awt.Font("Verdana", 0, 15)); // NOI18N
        lblDoanhThu.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblDoanhThu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imges/bill.png"))); // NOI18N
        lblDoanhThu.setText("Doanh Thu");
        lblDoanhThu.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblDoanhThuMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                lblDoanhThuMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                lblDoanhThuMouseExited(evt);
            }
        });

        lblGioiTheu.setFont(new java.awt.Font("Verdana", 0, 15)); // NOI18N
        lblGioiTheu.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblGioiTheu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imges/info.png"))); // NOI18N
        lblGioiTheu.setText("Giới Thiệu");
        lblGioiTheu.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                lblGioiTheuMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                lblGioiTheuMouseExited(evt);
            }
        });

        lblTroGiup.setFont(new java.awt.Font("Verdana", 0, 15)); // NOI18N
        lblTroGiup.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTroGiup.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imges/help.png"))); // NOI18N
        lblTroGiup.setText("Trợ giúp");
        lblTroGiup.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                lblTroGiupMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                lblTroGiupMouseExited(evt);
            }
        });

        lblDangXuat.setFont(new java.awt.Font("Verdana", 0, 15)); // NOI18N
        lblDangXuat.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblDangXuat.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imges/logout.png"))); // NOI18N
        lblDangXuat.setText("Đăng xuẩt");
        lblDangXuat.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                lblDangXuatMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                lblDangXuatMouseExited(evt);
            }
        });

        lblHoaDon.setFont(new java.awt.Font("Verdana", 0, 15)); // NOI18N
        lblHoaDon.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblHoaDon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imges/bill_.png"))); // NOI18N
        lblHoaDon.setText("Hoá Đơn");
        lblHoaDon.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblHoaDonMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                lblHoaDonMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                lblHoaDonMouseExited(evt);
            }
        });

        lblTongQuan.setFont(new java.awt.Font("Verdana", 0, 15)); // NOI18N
        lblTongQuan.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTongQuan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imges/view.png"))); // NOI18N
        lblTongQuan.setText("Tổng Quan");
        lblTongQuan.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblTongQuanMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                lblTongQuanMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                lblTongQuanMouseExited(evt);
            }
        });

        javax.swing.GroupLayout pnMenuLayout = new javax.swing.GroupLayout(pnMenu);
        pnMenu.setLayout(pnMenuLayout);
        pnMenuLayout.setHorizontalGroup(
            pnMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lblBanHang, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(pnMenuLayout.createSequentialGroup()
                .addGroup(pnMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblGioiTheu, javax.swing.GroupLayout.PREFERRED_SIZE, 260, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblTroGiup, javax.swing.GroupLayout.PREFERRED_SIZE, 260, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblDangXuat, javax.swing.GroupLayout.PREFERRED_SIZE, 260, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblSanPham, javax.swing.GroupLayout.PREFERRED_SIZE, 260, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblNhanVien, javax.swing.GroupLayout.PREFERRED_SIZE, 260, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblDoanhThu, javax.swing.GroupLayout.PREFERRED_SIZE, 260, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblHoaDon, javax.swing.GroupLayout.PREFERRED_SIZE, 260, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblTongQuan, javax.swing.GroupLayout.PREFERRED_SIZE, 260, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        pnMenuLayout.setVerticalGroup(
            pnMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnMenuLayout.createSequentialGroup()
                .addGap(56, 56, 56)
                .addComponent(lblTongQuan, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(lblBanHang, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addComponent(lblSanPham, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(lblNhanVien, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(lblHoaDon, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(lblDoanhThu, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 66, Short.MAX_VALUE)
                .addComponent(lblGioiTheu, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(lblTroGiup, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(lblDangXuat, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pnMain.add(pnMenu, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 42, 0, 676));

        pnHeader.setBackground(new java.awt.Color(255, 102, 102));
        pnHeader.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setText("Hello,");
        pnHeader.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(1140, 10, -1, -1));

        lblTenUser.setText("Admin");
        pnHeader.add(lblTenUser, new org.netbeans.lib.awtextra.AbsoluteConstraints(1179, 10, -1, -1));

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imges/User.png"))); // NOI18N
        pnHeader.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(1227, 8, -1, -1));

        lblThu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imges/So.png"))); // NOI18N
        lblThu.setText("  ");
        lblThu.setToolTipText("");
        lblThu.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblThuMouseClicked(evt);
            }
        });
        pnHeader.add(lblThu, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 8, -1, -1));

        pnMain.add(pnHeader, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1280, 42));

        pnMain_from.setBackground(new java.awt.Color(204, 204, 0));
        pnMain_from.setLayout(new java.awt.CardLayout());

        Tong.setBackground(new java.awt.Color(255, 204, 204));
        Tong.setPreferredSize(new java.awt.Dimension(1280, 680));

        TongDThu.setBackground(new java.awt.Color(204, 255, 51));

        javax.swing.GroupLayout TongDThuLayout = new javax.swing.GroupLayout(TongDThu);
        TongDThu.setLayout(TongDThuLayout);
        TongDThuLayout.setHorizontalGroup(
            TongDThuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 342, Short.MAX_VALUE)
        );
        TongDThuLayout.setVerticalGroup(
            TongDThuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 164, Short.MAX_VALUE)
        );

        Tong_DT_Ngay.setBackground(new java.awt.Color(204, 255, 51));

        javax.swing.GroupLayout Tong_DT_NgayLayout = new javax.swing.GroupLayout(Tong_DT_Ngay);
        Tong_DT_Ngay.setLayout(Tong_DT_NgayLayout);
        Tong_DT_NgayLayout.setHorizontalGroup(
            Tong_DT_NgayLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 342, Short.MAX_VALUE)
        );
        Tong_DT_NgayLayout.setVerticalGroup(
            Tong_DT_NgayLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 164, Short.MAX_VALUE)
        );

        Tong_HD.setBackground(new java.awt.Color(204, 255, 51));

        javax.swing.GroupLayout Tong_HDLayout = new javax.swing.GroupLayout(Tong_HD);
        Tong_HD.setLayout(Tong_HDLayout);
        Tong_HDLayout.setHorizontalGroup(
            Tong_HDLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 342, Short.MAX_VALUE)
        );
        Tong_HDLayout.setVerticalGroup(
            Tong_HDLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 164, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addComponent(Tong_DT_Ngay, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 95, Short.MAX_VALUE)
                .addComponent(Tong_HD, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(102, 102, 102)
                .addComponent(TongDThu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(Tong_DT_Ngay, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Tong_HD, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(TongDThu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(30, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jplBieuDoLayout = new javax.swing.GroupLayout(jplBieuDo);
        jplBieuDo.setLayout(jplBieuDoLayout);
        jplBieuDoLayout.setHorizontalGroup(
            jplBieuDoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jplBieuDoLayout.setVerticalGroup(
            jplBieuDoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 424, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout TongLayout = new javax.swing.GroupLayout(Tong);
        Tong.setLayout(TongLayout);
        TongLayout.setHorizontalGroup(
            TongLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jplBieuDo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        TongLayout.setVerticalGroup(
            TongLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(TongLayout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jplBieuDo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pnMain_from.add(Tong, "card7");

        BanHang.setBackground(new java.awt.Color(255, 0, 51));

        pnThucDon.setBackground(new java.awt.Color(204, 255, 204));

        javax.swing.GroupLayout pnThucDonLayout = new javax.swing.GroupLayout(pnThucDon);
        pnThucDon.setLayout(pnThucDonLayout);
        pnThucDonLayout.setHorizontalGroup(
            pnThucDonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 781, Short.MAX_VALUE)
        );
        pnThucDonLayout.setVerticalGroup(
            pnThucDonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        pnChiTiet_ThuTien.setBackground(new java.awt.Color(255, 102, 51));
        pnChiTiet_ThuTien.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        pnChiTiet_ThuTien.add(txtKhachDua, new org.netbeans.lib.awtextra.AbsoluteConstraints(265, 75, 207, 39));

        jLabel3.setFont(new java.awt.Font("Verdana", 0, 16)); // NOI18N
        jLabel3.setText("Khánh đưa");
        pnChiTiet_ThuTien.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(159, 82, -1, -1));
        pnChiTiet_ThuTien.add(txtTraLai, new org.netbeans.lib.awtextra.AbsoluteConstraints(265, 144, 207, 35));

        jLabel14.setFont(new java.awt.Font("Verdana", 0, 16)); // NOI18N
        jLabel14.setText("Trả lại");
        pnChiTiet_ThuTien.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(195, 149, -1, -1));

        buttonGroup1.add(rdoInBill);
        rdoInBill.setText("In Hoá đơn");
        pnChiTiet_ThuTien.add(rdoInBill, new org.netbeans.lib.awtextra.AbsoluteConstraints(123, 210, -1, -1));

        buttonGroup1.add(rdoKhongInBill);
        rdoKhongInBill.setText("Không in hoá đơn");
        pnChiTiet_ThuTien.add(rdoKhongInBill, new org.netbeans.lib.awtextra.AbsoluteConstraints(265, 210, -1, -1));
        pnChiTiet_ThuTien.add(txtTong, new org.netbeans.lib.awtextra.AbsoluteConstraints(265, 21, 207, 36));

        jLabel15.setFont(new java.awt.Font("Verdana", 0, 16)); // NOI18N
        jLabel15.setText("Tổng Tiền");
        pnChiTiet_ThuTien.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(166, 27, -1, -1));

        pnThuTien_BanHang.setBackground(new java.awt.Color(255, 204, 204));

        btnXoa_Form_BanHang.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imges/delle.png"))); // NOI18N
        btnXoa_Form_BanHang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXoa_Form_BanHangActionPerformed(evt);
            }
        });

        btnThuTien_BanHang.setFont(new java.awt.Font("Verdana", 0, 16)); // NOI18N
        btnThuTien_BanHang.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imges/pay.png"))); // NOI18N
        btnThuTien_BanHang.setText("Thu Tiền");
        btnThuTien_BanHang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThuTien_BanHangActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnThuTien_BanHangLayout = new javax.swing.GroupLayout(pnThuTien_BanHang);
        pnThuTien_BanHang.setLayout(pnThuTien_BanHangLayout);
        pnThuTien_BanHangLayout.setHorizontalGroup(
            pnThuTien_BanHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnThuTien_BanHangLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(btnXoa_Form_BanHang)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnThuTien_BanHang, javax.swing.GroupLayout.PREFERRED_SIZE, 389, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnThuTien_BanHangLayout.setVerticalGroup(
            pnThuTien_BanHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnThuTien_BanHangLayout.createSequentialGroup()
                .addContainerGap(22, Short.MAX_VALUE)
                .addGroup(pnThuTien_BanHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnXoa_Form_BanHang, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnThuTien_BanHang, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        javax.swing.GroupLayout pnHoaDonLayout = new javax.swing.GroupLayout(pnHoaDon);
        pnHoaDon.setLayout(pnHoaDonLayout);
        pnHoaDonLayout.setHorizontalGroup(
            pnHoaDonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnChiTiet_ThuTien, javax.swing.GroupLayout.DEFAULT_SIZE, 499, Short.MAX_VALUE)
            .addComponent(pnThuTien_BanHang, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        pnHoaDonLayout.setVerticalGroup(
            pnHoaDonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnHoaDonLayout.createSequentialGroup()
                .addGap(0, 603, Short.MAX_VALUE)
                .addComponent(pnChiTiet_ThuTien, javax.swing.GroupLayout.PREFERRED_SIZE, 0, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(pnThuTien_BanHang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout BanHangLayout = new javax.swing.GroupLayout(BanHang);
        BanHang.setLayout(BanHangLayout);
        BanHangLayout.setHorizontalGroup(
            BanHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(BanHangLayout.createSequentialGroup()
                .addComponent(pnThucDon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(pnHoaDon, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        BanHangLayout.setVerticalGroup(
            BanHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnThucDon, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(pnHoaDon, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pnMain_from.add(BanHang, "card2");

        SanPham.setBackground(new java.awt.Color(255, 255, 255));

        pnThemSanPham.setBackground(new java.awt.Color(255, 255, 255));
        pnThemSanPham.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        pnHinhAnh.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        lblHinh.setText(" ");

        javax.swing.GroupLayout pnHinhAnhLayout = new javax.swing.GroupLayout(pnHinhAnh);
        pnHinhAnh.setLayout(pnHinhAnhLayout);
        pnHinhAnhLayout.setHorizontalGroup(
            pnHinhAnhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnHinhAnhLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(lblHinh, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        pnHinhAnhLayout.setVerticalGroup(
            pnHinhAnhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnHinhAnhLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(lblHinh, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pnThemSanPham.add(pnHinhAnh, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 160, -1, -1));

        jLabel5.setFont(new java.awt.Font("Verdana", 0, 14)); // NOI18N
        jLabel5.setText("Tên Sản Phẩm:");
        pnThemSanPham.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 130, -1, -1));

        jLabel6.setFont(new java.awt.Font("Verdana", 0, 14)); // NOI18N
        jLabel6.setText("Loại sản phẩm: ");
        pnThemSanPham.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 220, -1, -1));

        jLabel8.setFont(new java.awt.Font("Verdana", 0, 14)); // NOI18N
        jLabel8.setText("Giá tiền:");
        jLabel8.setToolTipText("");
        pnThemSanPham.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 300, -1, -1));

        jLabel9.setFont(new java.awt.Font("Verdana", 0, 14)); // NOI18N
        jLabel9.setText("Đơn vị tính:");
        jLabel9.setToolTipText("");
        pnThemSanPham.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 400, -1, -1));
        pnThemSanPham.add(txtTenSP, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 120, 340, 40));
        pnThemSanPham.add(txtGiaTien, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 290, 340, 40));

        txtDonVi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtDonViActionPerformed(evt);
            }
        });
        pnThemSanPham.add(txtDonVi, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 390, 340, 40));

        btnThem.setText("Thêm");
        btnThem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThemActionPerformed(evt);
            }
        });
        pnThemSanPham.add(btnThem, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 530, 100, 40));

        btnSua.setText("Sửa");
        btnSua.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSuaActionPerformed(evt);
            }
        });
        pnThemSanPham.add(btnSua, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 530, 100, 40));

        btnLamMoi.setText("Làm mới");
        btnLamMoi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLamMoiActionPerformed(evt);
            }
        });
        pnThemSanPham.add(btnLamMoi, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 530, 100, 40));

        txtTim.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtTimKeyReleased(evt);
            }
        });
        pnThemSanPham.add(txtTim, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 30, 443, 40));

        jLabel7.setText("Tìm kiếm");
        pnThemSanPham.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 40, -1, -1));

        cboLoai.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "--Chọn loại sản phẩm--" }));
        pnThemSanPham.add(cboLoai, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 210, 340, 40));

        tblSP.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "#", "Tên sản phẩm", "Loại sản phẩm", "Giớ tiền", "Đơn vị tính"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblSP.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblSPMouseClicked(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                tblSPMouseReleased(evt);
            }
        });
        jSop_SanPham.setViewportView(tblSP);

        javax.swing.GroupLayout pnDanhSachLayout = new javax.swing.GroupLayout(pnDanhSach);
        pnDanhSach.setLayout(pnDanhSachLayout);
        pnDanhSachLayout.setHorizontalGroup(
            pnDanhSachLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnDanhSachLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jSop_SanPham, javax.swing.GroupLayout.PREFERRED_SIZE, 497, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );
        pnDanhSachLayout.setVerticalGroup(
            pnDanhSachLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnDanhSachLayout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jSop_SanPham)
                .addGap(0, 0, 0))
        );

        javax.swing.GroupLayout SanPhamLayout = new javax.swing.GroupLayout(SanPham);
        SanPham.setLayout(SanPhamLayout);
        SanPhamLayout.setHorizontalGroup(
            SanPhamLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(SanPhamLayout.createSequentialGroup()
                .addComponent(pnThemSanPham, javax.swing.GroupLayout.PREFERRED_SIZE, 763, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnDanhSach, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        SanPhamLayout.setVerticalGroup(
            SanPhamLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnThemSanPham, javax.swing.GroupLayout.DEFAULT_SIZE, 680, Short.MAX_VALUE)
            .addComponent(pnDanhSach, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pnMain_from.add(SanPham, "card3");

        NhanVien.setBackground(new java.awt.Color(51, 255, 204));

        pnThemNhanVien.setBackground(new java.awt.Color(255, 204, 204));
        pnThemNhanVien.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        txtTimNV.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtTimNVKeyReleased(evt);
            }
        });
        pnThemNhanVien.add(txtTimNV, new org.netbeans.lib.awtextra.AbsoluteConstraints(88, 13, 518, 30));

        jLabel4.setText("Tìm kiếm:");
        pnThemNhanVien.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(12, 20, -1, -1));

        jLabel10.setText("Họ và tên:");
        pnThemNhanVien.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(35, 95, -1, -1));

        jLabel11.setText("Ngày sinh:");
        pnThemNhanVien.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(34, 158, -1, -1));

        jLabel12.setText("Giới tính:");
        pnThemNhanVien.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 280, -1, -1));

        jLabel13.setText("Số điện thoại:");
        pnThemNhanVien.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(14, 213, -1, -1));

        jLabel16.setText("Ngày vào làm:");
        pnThemNhanVien.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(12, 279, -1, -1));

        jLabel17.setText("Chức vụ:");
        pnThemNhanVien.add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 340, -1, -1));

        jLabel18.setText("Tài khoản:");
        pnThemNhanVien.add(jLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(33, 341, -1, -1));

        jLabel19.setText("Mật khẩu");
        pnThemNhanVien.add(jLabel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(42, 407, -1, -1));
        pnThemNhanVien.add(txtHoTenNV, new org.netbeans.lib.awtextra.AbsoluteConstraints(112, 89, 200, -1));
        pnThemNhanVien.add(jdcNgaySinhNV, new org.netbeans.lib.awtextra.AbsoluteConstraints(112, 152, 200, -1));
        pnThemNhanVien.add(txtSoDTNV, new org.netbeans.lib.awtextra.AbsoluteConstraints(112, 210, 200, -1));
        pnThemNhanVien.add(jdcNgayVLNhanVien, new org.netbeans.lib.awtextra.AbsoluteConstraints(112, 276, 200, -1));
        pnThemNhanVien.add(txtTaiKhoanNV, new org.netbeans.lib.awtextra.AbsoluteConstraints(112, 338, 200, -1));

        gioitinh.add(rdoNam);
        rdoNam.setText("Nam");
        pnThemNhanVien.add(rdoNam, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 280, -1, -1));

        gioitinh.add(rdoNu);
        rdoNu.setText("Nữ");
        pnThemNhanVien.add(rdoNu, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 280, -1, -1));

        cboChucVu.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "--Chọn chức vụ--", "Quản lý", "Nhân viên bán hàng", "Quản lý kho" }));
        pnThemNhanVien.add(cboChucVu, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 340, 207, -1));
        pnThemNhanVien.add(txtMKNV, new org.netbeans.lib.awtextra.AbsoluteConstraints(112, 404, 200, -1));

        btnThemNV.setText("Thêm");
        btnThemNV.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThemNVActionPerformed(evt);
            }
        });
        pnThemNhanVien.add(btnThemNV, new org.netbeans.lib.awtextra.AbsoluteConstraints(42, 565, 100, 35));

        btnSuaNV.setText("Sửa");
        btnSuaNV.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSuaNVActionPerformed(evt);
            }
        });
        pnThemNhanVien.add(btnSuaNV, new org.netbeans.lib.awtextra.AbsoluteConstraints(187, 565, 100, 35));

        btnLamMoiNV.setText("Làm mới");
        btnLamMoiNV.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLamMoiNVActionPerformed(evt);
            }
        });
        pnThemNhanVien.add(btnLamMoiNV, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 565, 100, 35));

        lblHinhNV.setText(" ");
        lblHinhNV.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblHinhNVMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lblHinhNV, javax.swing.GroupLayout.DEFAULT_SIZE, 160, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lblHinhNV, javax.swing.GroupLayout.DEFAULT_SIZE, 180, Short.MAX_VALUE)
        );

        pnThemNhanVien.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 70, 160, 180));

        tblNhanVien.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "#", "Họ tên", "Ngày sinh", "Giới tính", "Số điện thoại", "Ngày vào làm", "Chức vụ", "Tài khoản", "mật khẩu"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblNhanVien.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblNhanVienMouseClicked(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                tblNhanVienMouseReleased(evt);
            }
        });
        jSop_NhanVien.setViewportView(tblNhanVien);

        javax.swing.GroupLayout pnDanhSachNhanVienLayout = new javax.swing.GroupLayout(pnDanhSachNhanVien);
        pnDanhSachNhanVien.setLayout(pnDanhSachNhanVienLayout);
        pnDanhSachNhanVienLayout.setHorizontalGroup(
            pnDanhSachNhanVienLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnDanhSachNhanVienLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jSop_NhanVien, javax.swing.GroupLayout.DEFAULT_SIZE, 650, Short.MAX_VALUE)
                .addContainerGap())
        );
        pnDanhSachNhanVienLayout.setVerticalGroup(
            pnDanhSachNhanVienLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnDanhSachNhanVienLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jSop_NhanVien)
                .addContainerGap())
        );

        javax.swing.GroupLayout NhanVienLayout = new javax.swing.GroupLayout(NhanVien);
        NhanVien.setLayout(NhanVienLayout);
        NhanVienLayout.setHorizontalGroup(
            NhanVienLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(NhanVienLayout.createSequentialGroup()
                .addComponent(pnThemNhanVien, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(pnDanhSachNhanVien, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        NhanVienLayout.setVerticalGroup(
            NhanVienLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnThemNhanVien, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(pnDanhSachNhanVien, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pnMain_from.add(NhanVien, "card4");

        HoaDon.setBackground(new java.awt.Color(153, 102, 255));

        javax.swing.GroupLayout HoaDonLayout = new javax.swing.GroupLayout(HoaDon);
        HoaDon.setLayout(HoaDonLayout);
        HoaDonLayout.setHorizontalGroup(
            HoaDonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1280, Short.MAX_VALUE)
        );
        HoaDonLayout.setVerticalGroup(
            HoaDonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 680, Short.MAX_VALUE)
        );

        pnMain_from.add(HoaDon, "card5");

        DoanhThu.setBackground(new java.awt.Color(255, 51, 255));

        javax.swing.GroupLayout DoanhThuLayout = new javax.swing.GroupLayout(DoanhThu);
        DoanhThu.setLayout(DoanhThuLayout);
        DoanhThuLayout.setHorizontalGroup(
            DoanhThuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1280, Short.MAX_VALUE)
        );
        DoanhThuLayout.setVerticalGroup(
            DoanhThuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 680, Short.MAX_VALUE)
        );

        pnMain_from.add(DoanhThu, "card6");

        pnMain.add(pnMain_from, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 40, 1280, 680));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnMain, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnMain, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void lblThuMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblThuMouseClicked
        pnMenu.setSize(with, hight);
        pnMain_from.setSize(1020, 676);
        pnMain_from.setLocation(260, 41);
    }//GEN-LAST:event_lblThuMouseClicked

    private void lblBanHangMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblBanHangMouseEntered
        lblBanHang.setOpaque(true);
        lblBanHang.setBackground(color);
    }//GEN-LAST:event_lblBanHangMouseEntered

    private void lblBanHangMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblBanHangMouseExited
        lblBanHang.setOpaque(true);
        lblBanHang.setBackground(this.getBackground());
    }//GEN-LAST:event_lblBanHangMouseExited

    private void lblSanPhamMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblSanPhamMouseEntered
        lblSanPham.setOpaque(true);
        lblSanPham.setBackground(color);
    }//GEN-LAST:event_lblSanPhamMouseEntered

    private void lblSanPhamMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblSanPhamMouseExited
        lblSanPham.setOpaque(true);
        lblSanPham.setBackground(this.getBackground());
    }//GEN-LAST:event_lblSanPhamMouseExited

    private void lblNhanVienMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblNhanVienMouseEntered
        lblNhanVien.setOpaque(true);
        lblNhanVien.setBackground(color);
    }//GEN-LAST:event_lblNhanVienMouseEntered

    private void lblNhanVienMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblNhanVienMouseExited
        lblNhanVien.setOpaque(true);
        lblNhanVien.setBackground(this.getBackground());
    }//GEN-LAST:event_lblNhanVienMouseExited

    private void lblDoanhThuMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblDoanhThuMouseEntered
        lblDoanhThu.setOpaque(true);
        lblDoanhThu.setBackground(color);
    }//GEN-LAST:event_lblDoanhThuMouseEntered

    private void lblDoanhThuMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblDoanhThuMouseExited
        lblDoanhThu.setOpaque(true);
        lblDoanhThu.setBackground(this.getBackground());
    }//GEN-LAST:event_lblDoanhThuMouseExited

    private void lblGioiTheuMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblGioiTheuMouseEntered
        lblGioiTheu.setOpaque(true);
        lblGioiTheu.setBackground(color);
    }//GEN-LAST:event_lblGioiTheuMouseEntered

    private void lblGioiTheuMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblGioiTheuMouseExited
        lblGioiTheu.setOpaque(true);
        lblGioiTheu.setBackground(this.getBackground());
    }//GEN-LAST:event_lblGioiTheuMouseExited

    private void lblTroGiupMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblTroGiupMouseEntered
        lblTroGiup.setOpaque(true);
        lblTroGiup.setBackground(color);
    }//GEN-LAST:event_lblTroGiupMouseEntered

    private void lblTroGiupMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblTroGiupMouseExited
        lblTroGiup.setOpaque(true);
        lblTroGiup.setBackground(this.getBackground());
    }//GEN-LAST:event_lblTroGiupMouseExited

    private void lblDangXuatMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblDangXuatMouseEntered
        lblDangXuat.setOpaque(true);
        lblDangXuat.setBackground(color);
    }//GEN-LAST:event_lblDangXuatMouseEntered

    private void lblDangXuatMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblDangXuatMouseExited
        lblDangXuat.setOpaque(true);
        lblDangXuat.setBackground(this.getBackground());
    }//GEN-LAST:event_lblDangXuatMouseExited

    private void lblHoaDonMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblHoaDonMouseEntered
        lblHoaDon.setOpaque(true);
        lblHoaDon.setBackground(color);
    }//GEN-LAST:event_lblHoaDonMouseEntered

    private void lblHoaDonMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblHoaDonMouseExited
        lblHoaDon.setOpaque(true);
        lblHoaDon.setBackground(this.getBackground());
    }//GEN-LAST:event_lblHoaDonMouseExited

    private void lblBanHangMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblBanHangMouseClicked
        Tong.setVisible(false);
        BanHang.setVisible(true);
        SanPham.setVisible(false);
        NhanVien.setVisible(false);
        HoaDon.setVisible(false);
        DoanhThu.setVisible(false);
        pnMenu.setSize(0, 0);
        pnMain_from.setSize(1280, 676);
        pnMain_from.setLocation(0, 40);
    }//GEN-LAST:event_lblBanHangMouseClicked

    private void lblNhanVienMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblNhanVienMouseClicked
        Tong.setVisible(false);
        BanHang.setVisible(false);
        SanPham.setVisible(false);
        NhanVien.setVisible(true);
        HoaDon.setVisible(false);
        DoanhThu.setVisible(false);
        pnMenu.setSize(0, 0);
        pnMain_from.setSize(1280, 676);
        pnMain_from.setLocation(0, 40);
    }//GEN-LAST:event_lblNhanVienMouseClicked

    private void lblSanPhamMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblSanPhamMouseClicked
        Tong.setVisible(false);
        BanHang.setVisible(false);
        SanPham.setVisible(true);
        NhanVien.setVisible(false);
        HoaDon.setVisible(false);
        DoanhThu.setVisible(false);
        pnMenu.setSize(0, 0);
        pnMain_from.setSize(1280, 676);
        pnMain_from.setLocation(0, 40);

    }//GEN-LAST:event_lblSanPhamMouseClicked

    private void lblHoaDonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblHoaDonMouseClicked
        Tong.setVisible(false);
        BanHang.setVisible(false);
        SanPham.setVisible(false);
        NhanVien.setVisible(false);
        HoaDon.setVisible(true);
        DoanhThu.setVisible(false);
        pnMenu.setSize(0, 0);
        pnMain_from.setSize(1280, 676);
        pnMain_from.setLocation(0, 40);
    }//GEN-LAST:event_lblHoaDonMouseClicked

    private void lblDoanhThuMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblDoanhThuMouseClicked
        Tong.setVisible(false);
        BanHang.setVisible(false);
        SanPham.setVisible(false);
        NhanVien.setVisible(false);
        HoaDon.setVisible(false);
        DoanhThu.setVisible(true);
        pnMenu.setSize(0, 0);
        pnMain_from.setSize(1280, 676);
        pnMain_from.setLocation(0, 40);
    }//GEN-LAST:event_lblDoanhThuMouseClicked

    private void lblTongQuanMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblTongQuanMouseClicked
        Tong.setVisible(true);
        BanHang.setVisible(false);
        SanPham.setVisible(false);
        NhanVien.setVisible(false);
        HoaDon.setVisible(false);
        DoanhThu.setVisible(false);
        pnMenu.setSize(0, 0);
        pnMain_from.setSize(1280, 676);
        pnMain_from.setLocation(0, 40);
    }//GEN-LAST:event_lblTongQuanMouseClicked

    private void lblTongQuanMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblTongQuanMouseEntered
        lblTongQuan.setOpaque(true);
        lblTongQuan.setBackground(color);
    }//GEN-LAST:event_lblTongQuanMouseEntered

    private void lblTongQuanMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblTongQuanMouseExited
        lblTongQuan.setOpaque(true);
        lblTongQuan.setBackground(this.getBackground());
    }//GEN-LAST:event_lblTongQuanMouseExited

    private void btnXoa_Form_BanHangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXoa_Form_BanHangActionPerformed

    }//GEN-LAST:event_btnXoa_Form_BanHangActionPerformed
    int diemClik = 0;
    private void btnThuTien_BanHangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThuTien_BanHangActionPerformed
        int check = diemClik += 1;
        if (check == 1) {
            pnChiTiet_ThuTien.setSize(499, 260);
            pnChiTiet_ThuTien.setLocation(0, 350);
        } else if (check == 2) {
            ThongBaoThanhCong("Thanh Toán Thành Công");
            JOptionPane.showMessageDialog(this, "ThanhToan Thanh COng");
            pnChiTiet_ThuTien.setSize(499, 0);
        }

    }//GEN-LAST:event_btnThuTien_BanHangActionPerformed

    private void txtTimKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTimKeyReleased
        // Lấy model từ bảng 'tblDataMan'
        DefaultTableModel obj = (DefaultTableModel) tblSP.getModel();

// Tạo một đối tượng TableRowSorter để sắp xếp các hàng trong bảng
        TableRowSorter<DefaultTableModel> obj1 = new TableRowSorter<>(obj);

// Đặt TableRowSorter làm trình sắp xếp hàng cho bảng 'tblDataMan'
        tblSP.setRowSorter(obj1);

// Thiết lập bộ lọc hàng cho TableRowSorter dựa trên nội dung của trường nhập liệu 'txtTim'
// Bộ lọc này sẽ chỉ hiển thị các hàng mà giá trị của chúng khớp với biểu thức chính quy trong 'txtTim'
        String searchText = txtTim.getText().toLowerCase(); // Chuyển đổi sang chữ thường
        obj1.setRowFilter(RowFilter.regexFilter("(?i)" + searchText)); // Sử dụng "(?i)" để bỏ qua việc phân biệt chữ hoa chữ thường

    }//GEN-LAST:event_txtTimKeyReleased

    private void tblSPMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblSPMouseReleased
        showPopup(evt);
    }//GEN-LAST:event_tblSPMouseReleased

    private void XoaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_XoaActionPerformed
        xoa_FromSanPham();
    }//GEN-LAST:event_XoaActionPerformed

    private void btnThemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThemActionPerformed
        if (KiemTraSP_FromSanPham()) {
            them_FromSanPham();
        }
    }//GEN-LAST:event_btnThemActionPerformed

    private void btnSuaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSuaActionPerformed
        if (KiemTraSP_FromSanPham() == false) {
            return;
        } else {
            capNhat_FromSanPham();
        }
    }//GEN-LAST:event_btnSuaActionPerformed

    private void btnLamMoiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLamMoiActionPerformed
        LamMoi_FromSanPham();
    }//GEN-LAST:event_btnLamMoiActionPerformed

    private void btnThemNVActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThemNVActionPerformed
        if (KiemTraNV_FromNhanVien()) {
            ThemNV_FromNhanVien();
        }
    }//GEN-LAST:event_btnThemNVActionPerformed

    private void btnSuaNVActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSuaNVActionPerformed
        if (KiemTraNV_FromNhanVien() == false) {
            return;
        } else {
            SuaNV_FromNhanVien();
        }
    }//GEN-LAST:event_btnSuaNVActionPerformed

    private void btnLamMoiNVActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLamMoiNVActionPerformed
        LamMoiNV_FromNhanVien();
    }//GEN-LAST:event_btnLamMoiNVActionPerformed

    private void tblNhanVienMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblNhanVienMouseClicked
        if (evt.getClickCount() == 2) {
            this.row = tblNhanVien.getSelectedRow();
            this.suaNV_FromNhanVien();
        }
    }//GEN-LAST:event_tblNhanVienMouseClicked

    private void tblNhanVienMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblNhanVienMouseReleased
        if (evt.isPopupTrigger()) {
            JTable source = (JTable) evt.getSource();
            int row = source.rowAtPoint(evt.getPoint());
            int column = source.columnAtPoint(evt.getPoint());

            if (!source.isRowSelected(row)) {
                source.changeSelection(row, column, false, false);
            }

            // Lấy dữ liệu từ hàng và cột tương ứng
            Object data = source.getValueAt(row, 0);

            // Chuyển đổi tọa độ của sự kiện chuột sang tọa độ của JFrame
            Point point = SwingUtilities.convertPoint(source, evt.getPoint(), this);

            // Hiển thị Popup Menu tại vị trí mới
            jp_nhanVien.show(this, point.x, point.y);

            // In dữ liệu ra màn hình hoặc thực hiện các thao tác khác với dữ liệu
            System.out.println("Data at row " + row + ", column " + column + ": " + data);
        }
    }//GEN-LAST:event_tblNhanVienMouseReleased

    private void txtTimNVKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTimNVKeyReleased
        // Lấy model từ bảng 'tblDataMan'
        DefaultTableModel obj = (DefaultTableModel) tblNhanVien.getModel();

        // Tạo một đối tượng TableRowSorter để sắp xếp các hàng trong bảng
        TableRowSorter<DefaultTableModel> obj1 = new TableRowSorter<>(obj);

        // Đặt TableRowSorter làm trình sắp xếp hàng cho bảng 'tblDataMan'
        tblNhanVien.setRowSorter(obj1);

        // Thiết lập bộ lọc hàng cho TableRowSorter dựa trên nội dung của trường nhập liệu 'txtTim'
        // Bộ lọc này sẽ chỉ hiển thị các hàng mà giá trị của chúng khớp với biểu thức chính quy trong 'txtTim'
        String searchText = txtTim.getText().toLowerCase(); // Chuyển đổi sang chữ thường
        obj1.setRowFilter(RowFilter.regexFilter("(?i)" + searchText));
        // Sử dụng "(?i)" để bỏ qua việc phân biệt chữ hoa chữ thường
    }//GEN-LAST:event_txtTimNVKeyReleased

    private void lblHinhNVMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblHinhNVMouseClicked
        chonAnhNV_FromNhanVien();
    }//GEN-LAST:event_lblHinhNVMouseClicked

    private void tblSPMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblSPMouseClicked
        if (evt.getClickCount() == 2) {
            this.row = tblSP.getSelectedRow();
            this.sua_FromSanPham();
        }
    }//GEN-LAST:event_tblSPMouseClicked

    private void jpI_XoaNhanVienActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jpI_XoaNhanVienActionPerformed
        this.xoaNV_FromNhanVien();
    }//GEN-LAST:event_jpI_XoaNhanVienActionPerformed

    private void txtDonViActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDonViActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDonViActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Main().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel BanHang;
    private javax.swing.JPanel DoanhThu;
    private javax.swing.JPanel HoaDon;
    private javax.swing.JPanel NhanVien;
    private javax.swing.JPanel SanPham;
    private javax.swing.JPanel Tong;
    private javax.swing.JPanel TongDThu;
    private javax.swing.JPanel Tong_DT_Ngay;
    private javax.swing.JPanel Tong_HD;
    private javax.swing.JMenuItem Xoa;
    private javax.swing.JButton btnLamMoi;
    private javax.swing.JButton btnLamMoiNV;
    private javax.swing.JButton btnSua;
    private javax.swing.JButton btnSuaNV;
    private javax.swing.JButton btnThem;
    private javax.swing.JButton btnThemNV;
    private javax.swing.JButton btnThuTien_BanHang;
    private javax.swing.JButton btnXoa_Form_BanHang;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JComboBox<String> cboChucVu;
    private javax.swing.JComboBox<String> cboLoai;
    private javax.swing.ButtonGroup gioitinh;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jSop_NhanVien;
    private javax.swing.JScrollPane jSop_SanPham;
    private com.toedter.calendar.JDateChooser jdcNgaySinhNV;
    private com.toedter.calendar.JDateChooser jdcNgayVLNhanVien;
    private javax.swing.JMenuItem jpI_XoaNhanVien;
    private javax.swing.JPopupMenu jp_nhanVien;
    private javax.swing.JPanel jplBieuDo;
    private javax.swing.JPopupMenu jpm;
    private javax.swing.JLabel lblBanHang;
    private javax.swing.JLabel lblDangXuat;
    private javax.swing.JLabel lblDoanhThu;
    private javax.swing.JLabel lblGioiTheu;
    private javax.swing.JLabel lblHinh;
    private javax.swing.JLabel lblHinhNV;
    private javax.swing.JLabel lblHoaDon;
    private javax.swing.JLabel lblNhanVien;
    private javax.swing.JLabel lblSanPham;
    private javax.swing.JLabel lblTenUser;
    private javax.swing.JLabel lblThu;
    private javax.swing.JLabel lblTongQuan;
    private javax.swing.JLabel lblTroGiup;
    private javax.swing.JPanel pnChiTiet_ThuTien;
    private javax.swing.JPanel pnDanhSach;
    private javax.swing.JPanel pnDanhSachNhanVien;
    private javax.swing.JPanel pnHeader;
    private javax.swing.JPanel pnHinhAnh;
    private javax.swing.JPanel pnHoaDon;
    private javax.swing.JPanel pnMain;
    private javax.swing.JPanel pnMain_from;
    private javax.swing.JPanel pnMenu;
    private javax.swing.JPanel pnThemNhanVien;
    private javax.swing.JPanel pnThemSanPham;
    private javax.swing.JPanel pnThuTien_BanHang;
    private javax.swing.JPanel pnThucDon;
    private javax.swing.JRadioButton rdoInBill;
    private javax.swing.JRadioButton rdoKhongInBill;
    private javax.swing.JRadioButton rdoNam;
    private javax.swing.JRadioButton rdoNu;
    private javax.swing.JTable tblNhanVien;
    private javax.swing.JTable tblSP;
    private javax.swing.JTextField txtDonVi;
    private javax.swing.JTextField txtGiaTien;
    private javax.swing.JTextField txtHoTenNV;
    private javax.swing.JTextField txtKhachDua;
    private javax.swing.JPasswordField txtMKNV;
    private javax.swing.JTextField txtSoDTNV;
    private javax.swing.JTextField txtTaiKhoanNV;
    private javax.swing.JTextField txtTenSP;
    private javax.swing.JTextField txtTim;
    private javax.swing.JTextField txtTimNV;
    private javax.swing.JTextField txtTong;
    private javax.swing.JTextField txtTraLai;
    // End of variables declaration//GEN-END:variables
 private void showPopup(MouseEvent e) {
        if (e.isPopupTrigger()) {
            JTable source = (JTable) e.getSource();
            int row = source.rowAtPoint(e.getPoint());
            int column = source.columnAtPoint(e.getPoint());

            if (!source.isRowSelected(row)) {
                source.changeSelection(row, column, false, false);
            }

            jpm.show(e.getComponent(), e.getX(), e.getY());
        }
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Xoá")) {

        }
    }

    public JPopupMenu popupMenu() {
        return jpm;
    }
}
