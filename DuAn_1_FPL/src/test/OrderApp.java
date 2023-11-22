
package test;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class OrderApp extends JFrame {
    private List<Product> productList;
    private JPanel productPanel;

    public OrderApp() {
        productList = new ArrayList<>();

        // Khởi tạo giao diện
        setTitle("Order App");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Panel chứa danh sách sản phẩm
        productPanel = new JPanel();
        productPanel.setLayout(new BoxLayout(productPanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(productPanel);
        add(scrollPane, BorderLayout.CENTER);

        // Button thêm sản phẩm
        JButton addButton = new JButton("Thêm sản phẩm");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addProduct();
            }
        });
        add(addButton, BorderLayout.SOUTH);

        // Hiển thị giao diện
        pack();
        setVisible(true);
    }

    private void addProduct() {
        // Tạo sản phẩm mới
        Product newProduct = new Product("Sản phẩm mới");

        // Thêm sản phẩm vào danh sách
        productList.add(newProduct);

        // Tạo nút JButton chứa thông tin sản phẩm
        JButton productButton = new JButton(newProduct.getName());
        productButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Xử lý sự kiện khi nhấp vào nút sản phẩm
                System.out.println("Đã chọn sản phẩm: " + newProduct.getName());
            }
        });

        // Thêm nút sản phẩm vào panel
        productPanel.add(productButton);

        // Cập nhật giao diện
        revalidate();
        repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new OrderApp();
            }
        });
    }
}

class Product {
    private String name;

    public Product(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}