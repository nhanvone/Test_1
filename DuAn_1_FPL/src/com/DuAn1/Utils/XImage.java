package com.DuAn1.Utils;

import javax.swing.*;
import java.awt.Image;
import java.io.File;
import java.net.URL;
import java.nio.file.*;

public class XImage {

    public static Image getAppIcon() {
        URL url = XImage.class.getResource("/Imges/icon.png");
        return new ImageIcon(url).getImage();
    }

    public static void save(File src) {
        File dst = new File("Images", src.getName());

        if (!dst.getParentFile().exists()) {
            dst.getParentFile().mkdirs();
        }

        try {
            Path from = Paths.get(src.getAbsolutePath());
            Path to = Paths.get(dst.getAbsolutePath());
            Files.copy(from, to, StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public static ImageIcon read(String fileName) {
        File path = new File("Images", fileName);
        System.out.println("Đường dẫn của tệp '" + fileName + "': " + path.getAbsolutePath());
        return new ImageIcon(path.getAbsolutePath());
    }


}
