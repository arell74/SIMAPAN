/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package Views;

import DataStore.DataStore;
import Model.Seleksi;
import Util.TabelUtil;
import Util.TombolAksiEditor;
import Util.TombolAksiRenderer;
import java.awt.Color;
import java.awt.Font;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author arelssi
 */
public class panelManajemenSeleksi extends javax.swing.JPanel {

    /**
     * Creates new form panelManajemenSeleksi
     */
    public panelManajemenSeleksi() {
        initComponents();
        setPreferredSize(new java.awt.Dimension(980, 616));
        setBackground(new java.awt.Color(245, 245, 245));
        
        setupTable();
        loadStatistics();
        loadTableData("");
    }
    private void setupTable() {
        tabelSeleksi.getTableHeader().setFont(new Font("Inter", Font.BOLD, 12));
        tabelSeleksi.getTableHeader().setBackground(new Color(122, 0, 0));
        tabelSeleksi.getTableHeader().setForeground(Color.WHITE);
        tabelSeleksi.setRowHeight(36);

        // PASANG RENDERER DAN EDITOR KE KOLOM AKSI (Indeks ke-5)
        tabelSeleksi.getColumnModel().getColumn(5).setCellRenderer(new TombolAksiRenderer());
        tabelSeleksi.getColumnModel().getColumn(5).setCellEditor(
            new TombolAksiEditor(new TombolAksiEditor.AksiListener() {
                
                @Override
                public void onDetail(int baris) {
                    String id = tabelSeleksi.getValueAt(baris, 1).toString();
                    JOptionPane.showMessageDialog(panelManajemenSeleksi.this, "Detail Seleksi: " + id);
                }

                @Override
                public void onEdit(int baris) {
                    String id = tabelSeleksi.getValueAt(baris, 1).toString();
                    JOptionPane.showMessageDialog(panelManajemenSeleksi.this, "Edit Hasil Seleksi: " + id);
                }

                @Override
                public void onHapus(int baris) {
                    hapusData(baris);
                }
            })
        );
    }

    // --- LOGIKA MENGHITUNG KARTU STATISTIK ---
    private void loadStatistics() {
        int total = DataStore.daftarSeleksi.size();
        int lulus = 0;
        int gugur = 0;

        for (Seleksi sel : DataStore.daftarSeleksi) {
            if (sel.getStatusHasil().equalsIgnoreCase("Lulus")) {
                lulus++;
            } else if (sel.getStatusHasil().equalsIgnoreCase("Gugur Permanen")) {
                gugur++;
            }
        }

        lblTotalSeleksi.setText(String.valueOf(total));
        lblLulus.setText(String.valueOf(lulus));
        lblGugur.setText(String.valueOf(gugur));
    }

    // --- LOGIKA TABEL DENGAN MULTI-FILTER ---
    private void loadTableData(String keyword) {
        DefaultTableModel model = (DefaultTableModel) tabelSeleksi.getModel();
        model.setRowCount(0); 

        // Ambil filter status dari ComboBox (Misal isinya: "Semua", "Lulus", "Gugur Permanen")
        String filterStatus = cbFilter.getSelectedItem().toString();

        int no = 1;
        for (Seleksi sel : DataStore.daftarSeleksi) {
            
            // 1. Pencarian Teks (Cek ID Seleksi, ID Peserta, atau Nama Peserta)
            // Perhatikan cara kita memanggil nama peserta dari objek di dalam objek: sel.getPeserta().getNamaLengkap()
            boolean textMatch = keyword.isEmpty() || 
                                sel.getIdSeleksi().toLowerCase().contains(keyword.toLowerCase()) || 
                                sel.getPeserta().getIdPeserta().toLowerCase().contains(keyword.toLowerCase()) ||
                                sel.getPeserta().getNamaLengkap().toLowerCase().contains(keyword.toLowerCase());

            // 2. Filter ComboBox Status
            boolean statusMatch = filterStatus.equals("Semua") || 
                                  sel.getStatusHasil().equalsIgnoreCase(filterStatus);

            // Jika cocok teks DAN cocok filter combobox, tampilkan!
            if (textMatch && statusMatch) {
                model.addRow(new Object[]{
                    no++,
                    sel.getIdSeleksi(),
                    sel.getPeserta().getIdPeserta(),
                    sel.getPeserta().getNamaLengkap(),
                    sel.getJenisSeleksi(),
                    "" // Kolom Aksi
                });
            }
        }
        
        try { 
            TabelUtil.autoResizeKolom(tabelSeleksi); 
        } catch (Exception e) {}
    }

    // --- LOGIKA HAPUS DATA ---
    private void hapusData(int baris) {
        String idSeleksi = tabelSeleksi.getValueAt(baris, 1).toString();
        
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Hapus data seleksi " + idSeleksi + "?", 
            "Konfirmasi", JOptionPane.YES_NO_OPTION);
            
        if (confirm == JOptionPane.YES_OPTION) {
            DataStore.daftarSeleksi.removeIf(sel -> sel.getIdSeleksi().equals(idSeleksi));
            JOptionPane.showMessageDialog(this, "Berhasil dihapus!");
            
            // Refresh tabel dan kartu statistik
            loadTableData(txtCari.getText());
            loadStatistics();
        }
    }

    // EVENT UNTUK TOMBOL (Bisa kamu copas ke dalam actionPerformed masing-masing tombol)
    private void btnCariActionPerformed(java.awt.event.ActionEvent evt) {
        loadTableData(txtCari.getText());
    }

    private void btnResetActionPerformed(java.awt.event.ActionEvent evt) {
        txtCari.setText("");
        cbFilter.setSelectedIndex(0); // Kembali ke "Semua"
        loadTableData("");
    }

    private void cbFilterActionPerformed(java.awt.event.ActionEvent evt) {
        // Otomatis filter tabel saat combobox diubah
        loadTableData(txtCari.getText());
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        kartuTotal2 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        lblGugur = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        pnlToolbar = new javax.swing.JPanel();
        txtCari = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        cbFilter = new javax.swing.JComboBox<>();
        jButton3 = new javax.swing.JButton();
        kartuTotal = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        lblTotalSeleksi = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        kartuTotal1 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        lblLulus = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabelSeleksi = new javax.swing.JTable();
        jLabel7 = new javax.swing.JLabel();

        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        kartuTotal2.setBackground(new java.awt.Color(255, 255, 255));
        kartuTotal2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(220, 220, 220)));
        kartuTotal2.setPreferredSize(new java.awt.Dimension(296, 56));
        kartuTotal2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel5.setForeground(new java.awt.Color(130, 130, 130));
        jLabel5.setText("Gugur pemanen");
        kartuTotal2.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 20, -1, -1));

        lblGugur.setFont(new java.awt.Font("Inter", 1, 36)); // NOI18N
        lblGugur.setForeground(new java.awt.Color(200, 0, 0));
        lblGugur.setText("0");
        kartuTotal2.add(lblGugur, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 40, -1, -1));

        jPanel3.setBackground(new java.awt.Color(255, 235, 235));
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel8.setIcon(new javax.swing.ImageIcon("/home/arelssi/Downloads/failed.png")); // NOI18N
        jLabel8.setText("jLabel8");
        jLabel8.setPreferredSize(new java.awt.Dimension(32, 32));
        jPanel3.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 15, -1, -1));

        kartuTotal2.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(32, 20, 60, 60));

        add(kartuTotal2, new org.netbeans.lib.awtextra.AbsoluteConstraints(660, 30, -1, 100));

        pnlToolbar.setBackground(new java.awt.Color(255, 255, 255));
        pnlToolbar.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(220, 220, 220)));
        pnlToolbar.setPreferredSize(new java.awt.Dimension(980, 56));
        pnlToolbar.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        pnlToolbar.add(txtCari, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 270, 34));

        jButton1.setBackground(new java.awt.Color(80, 80, 80));
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setText("Cari");
        pnlToolbar.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 20, -1, 34));

        jButton2.setText("Reset");
        pnlToolbar.add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 20, 90, 34));

        cbFilter.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Semua" }));
        pnlToolbar.add(cbFilter, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 20, 170, 34));

        jButton3.setBackground(new java.awt.Color(122, 0, 0));
        jButton3.setFont(new java.awt.Font("Inter", 0, 12)); // NOI18N
        jButton3.setForeground(new java.awt.Color(255, 255, 255));
        jButton3.setText("Input Seleksi");
        pnlToolbar.add(jButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(780, 20, -1, 34));

        add(pnlToolbar, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 156, 930, 70));

        kartuTotal.setBackground(new java.awt.Color(255, 255, 255));
        kartuTotal.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(220, 220, 220)));
        kartuTotal.setPreferredSize(new java.awt.Dimension(296, 56));
        kartuTotal.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setForeground(new java.awt.Color(130, 130, 130));
        jLabel1.setText("Total Seleksi");
        kartuTotal.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 20, -1, -1));

        lblTotalSeleksi.setFont(new java.awt.Font("Inter", 1, 36)); // NOI18N
        lblTotalSeleksi.setForeground(new java.awt.Color(122, 0, 0));
        lblTotalSeleksi.setText("0");
        kartuTotal.add(lblTotalSeleksi, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 40, -1, -1));

        jPanel1.setBackground(new java.awt.Color(255, 235, 235));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel9.setIcon(new javax.swing.ImageIcon("/home/arelssi/Downloads/exam.png")); // NOI18N
        jLabel9.setText("jLabel8");
        jLabel9.setPreferredSize(new java.awt.Dimension(32, 32));
        jPanel1.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 15, -1, -1));

        kartuTotal.add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 20, 60, 60));

        add(kartuTotal, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 30, -1, 100));

        kartuTotal1.setBackground(new java.awt.Color(255, 255, 255));
        kartuTotal1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(220, 220, 220)));
        kartuTotal1.setPreferredSize(new java.awt.Dimension(296, 56));
        kartuTotal1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel3.setForeground(new java.awt.Color(130, 130, 130));
        jLabel3.setText("Peserta Lulus");
        kartuTotal1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 20, -1, -1));

        lblLulus.setFont(new java.awt.Font("Inter", 1, 36)); // NOI18N
        lblLulus.setForeground(new java.awt.Color(0, 140, 60));
        lblLulus.setText("0");
        kartuTotal1.add(lblLulus, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 40, -1, -1));

        jPanel2.setBackground(new java.awt.Color(255, 235, 235));
        jPanel2.setPreferredSize(new java.awt.Dimension(60, 60));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel10.setIcon(new javax.swing.ImageIcon("/home/arelssi/Downloads/check.png")); // NOI18N
        jLabel10.setText("jLabel8");
        jLabel10.setPreferredSize(new java.awt.Dimension(32, 32));
        jPanel2.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 15, -1, -1));

        kartuTotal1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 20, -1, -1));

        add(kartuTotal1, new org.netbeans.lib.awtextra.AbsoluteConstraints(345, 30, -1, 100));

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(220, 220, 220)));
        jPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tabelSeleksi.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "No", "ID Seleksi", "ID Peserta", "Nama Peserta", "Jenis Seleksi", "Aksi"
            }
        ));
        jScrollPane1.setViewportView(tabelSeleksi);

        jPanel4.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(1, 37, 928, 332));

        jLabel7.setFont(new java.awt.Font("Inter", 0, 12)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(120, 120, 120));
        jLabel7.setText("Menampilkan 0 data seleksi");
        jPanel4.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(22, 10, -1, -1));

        add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 225, 930, 370));
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> cbFilter;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPanel kartuTotal;
    private javax.swing.JPanel kartuTotal1;
    private javax.swing.JPanel kartuTotal2;
    private javax.swing.JLabel lblGugur;
    private javax.swing.JLabel lblLulus;
    private javax.swing.JLabel lblTotalSeleksi;
    private javax.swing.JPanel pnlToolbar;
    private javax.swing.JTable tabelSeleksi;
    private javax.swing.JTextField txtCari;
    // End of variables declaration//GEN-END:variables
}
