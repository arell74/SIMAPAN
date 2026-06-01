/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package Views.admin;

import DataStore.DataStore;
import Model.Seleksi;
import Util.TabelUtil;
import Util.TombolAksiEditor;
import Util.TombolAksiRenderer;
import java.awt.Color;
import java.awt.Font;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author arelssi
 */
public class PanelManajemenSeleksi extends javax.swing.JPanel {

    /**
     * Creates new form panelManajemenSeleksi
     */
    public PanelManajemenSeleksi() {
        initComponents();
        setPreferredSize(new java.awt.Dimension(980, 616));
        setBackground(new java.awt.Color(245, 245, 245));
        
        cbFilterStatus.setModel(new DefaultComboBoxModel<>(new String[]{"Semua", "Lulus", "Gugur"}));
        setupTable();
        
        loadStatistics();
        loadTableData("");
    }
    
    private void setupTable() {
        // Styling tabel standar...
        tabelSeleksi.setRowHeight(40);
        
        // Asumsi kolom "Aksi" berada di indeks ke-9 (Kolom ke-10)
        tabelSeleksi.getColumnModel().getColumn(9).setCellRenderer(new TombolAksiRenderer());
        tabelSeleksi.getColumnModel().getColumn(9).setCellEditor(new TombolAksiEditor(new TombolAksiEditor.AksiListener() {
            
            @Override
            public void onDetail(int baris) {
                // Logika melihat detail riwayat (opsional, mungkin memanggil FormDetailPeserta)
            }

            @Override
            public void onEdit(int baris) {
                String idPeserta = tabelSeleksi.getValueAt(baris, 2).toString(); 
                
                // 2. Cari objek Peserta aslinya dari DataStore
                Model.Peserta pesertaTerpilih = null;
                for (Model.Peserta p : DataStore.daftarPeserta) {
                    if (p.getIdPeserta().equals(idPeserta)) {
                        pesertaTerpilih = p;
                        break;
                    }
                }
                
                // 3. Panggil Form Input Seleksi
                if (pesertaTerpilih != null) {
                    
                    // Gunakan PanelManajemenSeleksi.this agar lemparan induknya tepat sasaran
                    FormInputSeleksi form = new FormInputSeleksi(pesertaTerpilih, PanelManajemenSeleksi.this);
                    form.setVisible(true); 
                    
                    // CATATAN PENTING:
                    // Tidak perlu menaruh loadTableData() di sini.
                    // Biarkan form pop-up terbuka. Saat user menekan tombol "Simpan" di pop-up,
                    // form tersebut yang akan memanggil fungsi refresh milik induknya.
                }
            }

            @Override
            public void onHapus(int baris) {
                // Kosongkan jika tidak ada tombol hapus
            }
        }));
    }

    // --- LOGIKA MENGHITUNG KARTU STATISTIK ---
    private void loadStatistics() {
        int totalSeleksi = DataStore.daftarSeleksi.size(); // Total semua riwayat seleksi yang pernah dilakukan
        int pesertaLulus = 0;
        int gugurPermanen = 0;

        // Kita hitung dari status akhir tiap peserta
        for (Model.Peserta p : DataStore.daftarPeserta) {
            // Null-Safe: Jika status p null, tidak akan error, langsung dianggap false
            if ("Lulus".equalsIgnoreCase(p.getStatusSeleksi())) {
                pesertaLulus++;
            } else if ("Gugur".equalsIgnoreCase(p.getStatusSeleksi()) || "Gugur Permanen".equalsIgnoreCase(p.getStatusSeleksi())) {
                gugurPermanen++;
            }
        }

        lblTotalSeleksi.setText(String.valueOf(totalSeleksi));
        lblPesertaLulus.setText(String.valueOf(pesertaLulus));
        lblGugurPermanen.setText(String.valueOf(gugurPermanen));
    }

    // --- LOGIKA TABEL DENGAN MULTI-FILTER ---
    private void loadTableData(String keyword) {
        DefaultTableModel model = (DefaultTableModel) tabelSeleksi.getModel();
        model.setRowCount(0);
        
        String filter = cbFilterStatus.getSelectedItem() != null ? cbFilterStatus.getSelectedItem().toString() : "Semua";
        int no = 1;

        // 1. Loop semua daftar Peserta
        for (Model.Peserta p : DataStore.daftarPeserta) {
            
            // Filter Pencarian Teks
            boolean matchText = keyword.isEmpty() || 
                                p.getNamaLengkap().toLowerCase().contains(keyword.toLowerCase()) || 
                                p.getIdPeserta().toLowerCase().contains(keyword.toLowerCase());
            
            if (!matchText) continue;

            // 2. Kalkulasi Riwayat Seleksi (Algoritma Pencarian)
            int jumlahPercobaan = 0;
            Model.Seleksi seleksiTerakhir = null;
            
            for (Model.Seleksi s : DataStore.daftarSeleksi) {
                if (s.getPeserta().getIdPeserta().equals(p.getIdPeserta())) {
                    jumlahPercobaan++;
                    seleksiTerakhir = s; // Variabel ini akan terus tertimpa sampai menemukan data yang paling akhir (terbaru)
                }
            }

            // 3. Tentukan Nilai Kolom Berdasarkan Hasil Kalkulasi
            String idSel = (seleksiTerakhir != null) ? seleksiTerakhir.getIdSeleksi() : "—";
            String jenisSel = (seleksiTerakhir != null) ? seleksiTerakhir.getJenisSeleksi() : "—";
            String tglSel = (seleksiTerakhir != null) ? seleksiTerakhir.getTanggalSeleksi() : "—";
            String nilaiStr = (seleksiTerakhir != null) ? String.valueOf(seleksiTerakhir.getNilai()) : "—";
            
            // Tentukan status, jika belum pernah seleksi sama sekali = "Menunggu"
            String status = (seleksiTerakhir != null) ? seleksiTerakhir.getStatusHasil() : "Menunggu";
            
            // Format string percobaan seperti di desain: "1 / 3"
            String teksPercobaan = jumlahPercobaan + " / 3";

            // 4. Terapkan Filter Status Dropdown
            boolean matchStatus = filter.equals("Semua") || status.equalsIgnoreCase(filter);
            
            if (matchStatus) {
                model.addRow(new Object[]{
                    no++,
                    idSel,
                    p.getIdPeserta(),
                    p.getNamaLengkap(),
                    jenisSel,
                    tglSel,
                    nilaiStr,
                    teksPercobaan,
                    status,
                    ""
                });
            }
        }
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
        cbFilterStatus.setSelectedIndex(0); // Kembali ke "Semua"
        loadTableData("");
    }

    private void cbFilterActionPerformed(java.awt.event.ActionEvent evt) {
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
        lblGugurPermanen = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        pnlToolbar = new javax.swing.JPanel();
        txtCari = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        cbFilterStatus = new javax.swing.JComboBox<>();
        jButton3 = new javax.swing.JButton();
        kartuTotal = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        lblTotalSeleksi = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        kartuTotal1 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        lblPesertaLulus = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        panelTabel = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabelSeleksi = new javax.swing.JTable();
        lblTotalData = new javax.swing.JLabel();

        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        kartuTotal2.setBackground(new java.awt.Color(255, 255, 255));
        kartuTotal2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(220, 220, 220)));
        kartuTotal2.setPreferredSize(new java.awt.Dimension(296, 56));
        kartuTotal2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel5.setForeground(new java.awt.Color(130, 130, 130));
        jLabel5.setText("Gugur pemanen");
        kartuTotal2.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 20, -1, -1));

        lblGugurPermanen.setFont(new java.awt.Font("Inter", 1, 36)); // NOI18N
        lblGugurPermanen.setForeground(new java.awt.Color(200, 0, 0));
        lblGugurPermanen.setText("0");
        kartuTotal2.add(lblGugurPermanen, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 40, -1, -1));

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

        cbFilterStatus.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Semua" }));
        pnlToolbar.add(cbFilterStatus, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 20, 170, 34));

        jButton3.setBackground(new java.awt.Color(122, 0, 0));
        jButton3.setFont(new java.awt.Font("Inter", 0, 12)); // NOI18N
        jButton3.setForeground(new java.awt.Color(255, 255, 255));
        jButton3.setText("Input Seleksi");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        pnlToolbar.add(jButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(800, 20, -1, 34));

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

        lblPesertaLulus.setFont(new java.awt.Font("Inter", 1, 36)); // NOI18N
        lblPesertaLulus.setForeground(new java.awt.Color(0, 140, 60));
        lblPesertaLulus.setText("0");
        kartuTotal1.add(lblPesertaLulus, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 40, -1, -1));

        jPanel2.setBackground(new java.awt.Color(255, 235, 235));
        jPanel2.setPreferredSize(new java.awt.Dimension(60, 60));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel10.setIcon(new javax.swing.ImageIcon("/home/arelssi/Downloads/check.png")); // NOI18N
        jLabel10.setText("jLabel8");
        jLabel10.setPreferredSize(new java.awt.Dimension(32, 32));
        jPanel2.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 15, -1, -1));

        kartuTotal1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 20, -1, -1));

        add(kartuTotal1, new org.netbeans.lib.awtextra.AbsoluteConstraints(345, 30, -1, 100));

        panelTabel.setBackground(new java.awt.Color(255, 255, 255));
        panelTabel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(220, 220, 220)));
        panelTabel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tabelSeleksi.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "No", "ID Seleksi", "ID Peserta", "Nama Peserta", "Jenis Seleksi", "Tgl Seleksi", "Nilai", "Percobaan", "Status", "Aksi"
            }
        ));
        jScrollPane1.setViewportView(tabelSeleksi);

        panelTabel.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(1, 37, 928, 332));

        lblTotalData.setFont(new java.awt.Font("Inter", 0, 12)); // NOI18N
        lblTotalData.setForeground(new java.awt.Color(120, 120, 120));
        lblTotalData.setText("Menampilkan 0 data seleksi");
        panelTabel.add(lblTotalData, new org.netbeans.lib.awtextra.AbsoluteConstraints(22, 10, -1, -1));

        add(panelTabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 225, 930, 370));
    }// </editor-fold>//GEN-END:initComponents

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // 1. Cek apakah ada baris tabel yang dipilih
        int barisTerpilih = tabelSeleksi.getSelectedRow();
        
        if (barisTerpilih == -1) {
            javax.swing.JOptionPane.showMessageDialog(this, 
                "Pilih data peserta di tabel terlebih dahulu untuk menambah seleksi!", 
                "Peringatan", javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // 2. Ambil ID Peserta dari tabel (Asumsi ID Peserta ada di kolom indeks ke-2)
        String idPeserta = tabelSeleksi.getValueAt(barisTerpilih, 2).toString();
        
        // 3. Cari objek Peserta aslinya dari DataStore
        Model.Peserta pesertaTerpilih = null;
        for (Model.Peserta p : DataStore.daftarPeserta) {
            if (p.getIdPeserta().equals(idPeserta)) {
                pesertaTerpilih = p;
                break;
            }
        }
        
        // 4. Buka Form Input dan "lempar" data peserta serta halaman ini (this) ke dalamnya
        if (pesertaTerpilih != null) {
            FormInputSeleksi form = new FormInputSeleksi(pesertaTerpilih, this);
            form.setVisible(true);
        }
    }//GEN-LAST:event_jButton3ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> cbFilterStatus;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPanel kartuTotal;
    private javax.swing.JPanel kartuTotal1;
    private javax.swing.JPanel kartuTotal2;
    private javax.swing.JLabel lblGugurPermanen;
    private javax.swing.JLabel lblPesertaLulus;
    private javax.swing.JLabel lblTotalData;
    private javax.swing.JLabel lblTotalSeleksi;
    private javax.swing.JPanel panelTabel;
    private javax.swing.JPanel pnlToolbar;
    private javax.swing.JTable tabelSeleksi;
    private javax.swing.JTextField txtCari;
    // End of variables declaration//GEN-END:variables
}
