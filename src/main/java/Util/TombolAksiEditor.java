package Util;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;

public class TombolAksiEditor extends AbstractCellEditor implements TableCellEditor {

    private final JPanel    panel    = new JPanel(new FlowLayout(FlowLayout.CENTER, 4, 4));
    private final JButton btnDetail = new JButton("Detail");
    private final JButton   btnEdit  = new JButton("Edit");
    private final JButton   btnHapus = new JButton("Hapus");
    private JTable          tabel;
    private int             baris;

    public interface AksiListener {
        void onDetail(int baris);
        void onEdit(int baris);
        void onHapus(int baris);
    }

    private AksiListener listener;

    public TombolAksiEditor(AksiListener listener) {
        this.listener = listener;
        
        btnDetail.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        btnDetail.setBackground(new Color(230, 240, 255));
        btnDetail.setForeground(new Color(0, 50, 150));
        btnDetail.setBorderPainted(false);
        btnDetail.setPreferredSize(new Dimension(55, 26));

        btnDetail.addActionListener(e -> {
            fireEditingStopped();
            listener.onDetail(baris);
        });

        btnEdit.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        btnEdit.setBackground(new Color(240, 240, 240));
        btnEdit.setForeground(new Color(50, 50, 50));
        btnEdit.setBorderPainted(false);
        btnEdit.setFocusPainted(false);
        btnEdit.setPreferredSize(new Dimension(46, 26));

        btnHapus.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        btnHapus.setBackground(new Color(255, 235, 235));
        btnHapus.setForeground(new Color(180, 0, 0));
        btnHapus.setBorderPainted(false);
        btnHapus.setFocusPainted(false);
        btnHapus.setPreferredSize(new Dimension(52, 26));

        btnEdit.addActionListener(e -> {
            fireEditingStopped();
            listener.onEdit(baris);
        });

        btnHapus.addActionListener(e -> {
            fireEditingStopped();
            listener.onHapus(baris);
        });

        panel.setOpaque(true);
        panel.add(btnDetail);
        panel.add(btnEdit);
        panel.add(btnHapus);
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value,
            boolean isSelected, int row, int column) {
        this.tabel = table;
        this.baris = row;
        panel.setBackground(new Color(255, 230, 230));
        return panel;
    }

    @Override
    public Object getCellEditorValue() { return ""; }
}
