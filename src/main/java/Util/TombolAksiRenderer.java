package Util;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;

public class TombolAksiRenderer extends JPanel implements TableCellRenderer {
    private final JButton btnDetail = new JButton("Detail");
    private final JButton btnEdit  = new JButton("Edit");
    private final JButton btnHapus = new JButton("Hapus");

    public TombolAksiRenderer() {
        setLayout(new FlowLayout(FlowLayout.CENTER, 4, 4));
        setOpaque(true);

//        Tombol Detail
        btnDetail.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        btnDetail.setBackground(new Color(230, 240, 255));
        btnDetail.setForeground(new Color(0, 50, 150));
        btnDetail.setBorderPainted(false);
        btnDetail.setFocusPainted(false);
        btnDetail.setPreferredSize(new Dimension(60, 26));

//        Tombol Edit
        btnEdit.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        btnEdit.setBackground(new Color(240, 240, 240));
        btnEdit.setForeground(new Color(50, 50, 50));
        btnEdit.setBorderPainted(false);
        btnEdit.setFocusPainted(false);
        btnEdit.setPreferredSize(new Dimension(60, 26));

//         Tombol Hapus
        btnHapus.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        btnHapus.setBackground(new Color(255, 235, 235));
        btnHapus.setForeground(new Color(180, 0, 0));
        btnHapus.setBorderPainted(false);
        btnHapus.setFocusPainted(false);
        btnHapus.setPreferredSize(new Dimension(60, 26));

        add(btnDetail);
        add(btnEdit);
        add(btnHapus);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {
        setBackground(isSelected ? new Color(255, 230, 230) : Color.WHITE);
        return this;
    }
}