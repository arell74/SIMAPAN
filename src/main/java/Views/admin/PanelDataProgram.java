/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package Views.admin;

import Model.Program;
import DataStore.DataStore;
import Util.TombolAksiEditor;
import Util.TombolAksiRenderer;
import java.awt.Color;
import java.awt.Font;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author arelssi
 */
public class PanelDataProgram extends javax.swing.JPanel {

    public PanelDataProgram() {
        initComponents();
        setPreferredSize(new java.awt.Dimension(980, 616));
        setBackground(new java.awt.Color(245, 245, 245));

        setupTable();

        // Sinkronkan cache DataStore dengan data terbaru dari MySQL
        DataStore.muatUlang();
        loadTableData("");
    }

    private void setupTable() {
        // Styling Header
        tabelProgram.getTableHeader().setFont(new Font("Inter", Font.BOLD, 12));
        tabelProgram.getTableHeader().setBackground(new Color(122, 0, 0));
        tabelProgram.getTableHeader().setForeground(Color.WHITE);
        tabelProgram.getTableHeader().setOpaque(false);
        tabelProgram.setRowHeight(36);
        tabelProgram.setSelectionBackground(new Color(255, 230, 230));

        // Pasang Renderer dan Editor ke Kolom Aksi (Indeks ke-6)
        tabelProgram.getColumnModel().getColumn(6).setCellRenderer(new TombolAksiRenderer());
        tabelProgram.getColumnModel().getColumn(6).setCellEditor(
            new TombolAksiEditor(new TombolAksiEditor.AksiListener() {

                @Override
                public void onDetail(int baris) {
                    String idProgram = tabelProgram.getValueAt(baris, 1).toString().trim();
                    java.awt.Window parentWindow = SwingUtilities.getWindowAncestor(PanelDataProgram.this);
                    if (parentWindow instanceof java.awt.Frame) {
                        FormDetailProgram formDetail = new FormDetailProgram(
                            (java.awt.Frame) parentWindow,
                            true,
                            idProgram,
                            PanelDataProgram.this
                        );
                        tampilkanFormDenganRefresh(formDetail);
                    }
                }

                @Override
                public void onEdit(int baris) {
                    String idTarget = tabelProgram.getValueAt(baris, 1).toString().trim();
                    java.awt.Window parentWindow = SwingUtilities.getWindowAncestor(PanelDataProgram.this);
                    if (parentWindow instanceof java.awt.Frame) {
                        FormEditProgram formEdit = new FormEditProgram(
                            (java.awt.Frame) parentWindow,
                            true,
                            idTarget,
                            PanelDataProgram.this
                        );
                        tampilkanFormDenganRefresh(formEdit);
                    }
                }

                @Override
                public void onHapus(int baris) {
                    hapusData(baris);
                }
            })
        );
    }

    // Refresh cache MySQL & tabel otomatis saat form Tambah/Edit ditutup
    private void tampilkanFormDenganRefresh(java.awt.Window form) {
        form.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent e) {
                DataStore.muatUlang();
                loadTableData("");
            }
        });
        form.setVisible(true);
    }

    // Menampilkan ringkasan detail program + jumlah peserta terdaftar dari cache MySQL
    private void tampilkanDetailProgram(String idProgram) {
        Program prg = DataStore.cariProgram(idProgram);
        if (prg == null) {
            JOptionPane.showMessageDialog(this, "Data program tidak ditemukan di database!",
                    "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int jumlahPeserta = 0;
        for (Model.Peserta p : DataStore.daftarPeserta) {
            if (idProgram.equals(p.getProgram())) {
                jumlahPeserta++;
            }
        }

        String biayaFormatted = "Rp " + String.format("%,.0f", prg.getBiaya()).replace(',', '.');
        String info = "ID Program      : " + prg.getIdProgram() + "\n"
                    + "Nama Program  : " + prg.getNamaProgram() + "\n"
                    + "Bidang Usaha   : " + prg.getBidangUsaha() + "\n"
                    + "Biaya Program  : " + biayaFormatted + "\n"
                    + "Kuota Peserta   : " + prg.getKuota() + " orang\n"
                    + "Terdaftar Saat Ini : " + jumlahPeserta + " peserta";

        JOptionPane.showMessageDialog(this, info, "Detail Program - " + prg.getIdProgram(),
                JOptionPane.INFORMATION_MESSAGE);
    }

    // Hapus data program dari MySQL via ProgramDAO
    private void hapusData(int baris) {
        String idProgram = tabelProgram.getValueAt(baris, 1).toString();
        String nama = tabelProgram.getValueAt(baris, 2).toString();

        int confirm = JOptionPane.showConfirmDialog(this,
            "Apakah Anda yakin ingin menghapus program " + nama + "?",
            "Konfirmasi Hapus", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            if (Database.ProgramDAO.hapus(idProgram)) {
                DataStore.muatUlang();
                JOptionPane.showMessageDialog(this, "Data Program Berhasil Dihapus!");
                loadTableData("");
            }
            // Jika gagal (misal masih dipakai oleh peserta), popup error sudah ditampilkan oleh ProgramDAO
        }
    }

    public void loadTableData(String keyword) {
        DefaultTableModel model = (DefaultTableModel) tabelProgram.getModel();
        model.setRowCount(0);

        String kw = (keyword == null) ? "" : keyword.trim().toLowerCase();
        int no = 1;

        for (Program prg : DataStore.daftarProgram) {
            boolean match = kw.isEmpty() ||
                            prg.getNamaProgram().toLowerCase().contains(kw) ||
                            prg.getIdProgram().toLowerCase().contains(kw) ||
                            (prg.getBidangUsaha() != null && prg.getBidangUsaha().toLowerCase().contains(kw));

            if (match) {
                model.addRow(new Object[]{
                    no++,
                    prg.getIdProgram(),
                    prg.getNamaProgram(),
                    prg.getBidangUsaha(),
                    "Rp " + String.format("%,.0f", prg.getBiaya()).replace(',', '.'),
                    prg.getKuota(),
                    "" // Kolom Aksi
                });
            }
        }

        lblTotalData.setText("Menampilkan " + model.getRowCount() + " data program");

        try {
            Util.TabelUtil.autoResizeKolom(tabelProgram);
        } catch (Exception e) {
            System.out.println("TabelUtil belum tersedia: " + e.getMessage());
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelToolbar = new javax.swing.JPanel();
        jTextField1 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        btnTambah = new javax.swing.JButton();
        jSeparator2 = new javax.swing.JSeparator();
        jSeparator3 = new javax.swing.JSeparator();
        jLabel3 = new javax.swing.JLabel();
        panelTabel = new javax.swing.JPanel();
        scrollTabel = new javax.swing.JScrollPane();
        tabelProgram = new javax.swing.JTable();
        lblTotalData = new javax.swing.JLabel();

        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        panelToolbar.setBackground(new java.awt.Color(255, 255, 255));
        panelToolbar.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jTextField1.setPreferredSize(new java.awt.Dimension(280, 30));
        panelToolbar.add(jTextField1, new org.netbeans.lib.awtextra.AbsoluteConstraints(56, 14, 270, -1));

        jButton1.setBackground(new java.awt.Color(80, 80, 80));
        jButton1.setFont(new java.awt.Font("Inter", 0, 12)); // NOI18N
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setText("Cari");
        jButton1.setPreferredSize(new java.awt.Dimension(70, 30));
        panelToolbar.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(334, 14, -1, -1));

        jButton2.setBackground(new java.awt.Color(240, 240, 240));
        jButton2.setFont(new java.awt.Font("Inter", 0, 12)); // NOI18N
        jButton2.setForeground(new java.awt.Color(80, 80, 80));
        jButton2.setText("Reset");
        jButton2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(210, 210, 210)));
        jButton2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton2.setFocusPainted(false);
        jButton2.setPreferredSize(new java.awt.Dimension(70, 30));
        panelToolbar.add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(412, 14, -1, -1));

        btnTambah.setBackground(new java.awt.Color(122, 0, 0));
        btnTambah.setFont(new java.awt.Font("Inter", 1, 12)); // NOI18N
        btnTambah.setForeground(new java.awt.Color(255, 255, 255));
        btnTambah.setText("Tambah Program");
        btnTambah.setPreferredSize(new java.awt.Dimension(148, 30));
        btnTambah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTambahActionPerformed(evt);
            }
        });
        panelToolbar.add(btnTambah, new org.netbeans.lib.awtextra.AbsoluteConstraints(810, 14, -1, -1));

        jSeparator2.setPreferredSize(new java.awt.Dimension(980, 2));
        panelToolbar.add(jSeparator2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 54, -1, -1));

        jSeparator3.setPreferredSize(new java.awt.Dimension(980, 2));
        panelToolbar.add(jSeparator3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        jLabel3.setIcon(new javax.swing.ImageIcon("/home/arelssi/Downloads/search.png")); // NOI18N
        jLabel3.setText("C");
        jLabel3.setPreferredSize(new java.awt.Dimension(24, 28));
        panelToolbar.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 14, -1, -1));

        add(panelToolbar, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        panelTabel.setBackground(new java.awt.Color(255, 255, 255));
        panelTabel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(220, 220, 220)));
        panelTabel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        scrollTabel.setPreferredSize(new java.awt.Dimension(948, 460));

        tabelProgram.setFont(new java.awt.Font("Inter", 0, 12)); // NOI18N
        tabelProgram.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "No.", "ID Program", "Nama Program", "Bidang Usaha", "Biaya", "Kuota", "Aksi"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Object.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        tabelProgram.setRowHeight(36);
        tabelProgram.setSelectionBackground(new java.awt.Color(255, 230, 230));
        tabelProgram.setSelectionForeground(new java.awt.Color(122, 0, 0));
        tabelProgram.setShowHorizontalLines(true);
        scrollTabel.setViewportView(tabelProgram);

        panelTabel.add(scrollTabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        add(panelTabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 90, -1, 450));

        lblTotalData.setFont(new java.awt.Font("Inter", 0, 11)); // NOI18N
        lblTotalData.setForeground(new java.awt.Color(120, 120, 120));
        lblTotalData.setText("Menampilkan 0 data peserta");
        lblTotalData.setPreferredSize(new java.awt.Dimension(300, 20));
        add(lblTotalData, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 60, -1, -1));
    }// </editor-fold>//GEN-END:initComponents

    private void btnTambahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTambahActionPerformed
        FormTambahProgram form = new FormTambahProgram(this);
        form.setVisible(true);
    }//GEN-LAST:event_btnTambahActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnTambah;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JLabel lblTotalData;
    private javax.swing.JPanel panelTabel;
    private javax.swing.JPanel panelToolbar;
    private javax.swing.JScrollPane scrollTabel;
    private javax.swing.JTable tabelProgram;
    // End of variables declaration//GEN-END:variables
}
