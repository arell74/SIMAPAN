/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package Views.instruktur;

import DataStore.DataStore;
import Model.Instruktur;
import Model.Pengguna;
import Model.Peserta;
import Model.Seleksi;
import Util.TabelUtil;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author arelssi
 */
public class PanelDataPeserta extends javax.swing.JPanel {

    /**
     * Creates new form PanelDataSiswa
     */
    private String idInstrukturAktif;
    private Instruktur instrukturMilikKu;
    
    public PanelDataPeserta(String idInstruktur) {
        initComponents();
        initComponents();
        this.idInstrukturAktif = idInstruktur;
        
        setupTable();
        loadDataKelas();
        loadTableData("");
    }
    
    private void loadDataKelas() {
        // 1. Cari data instruktur yang login
        for (Pengguna pgn : DataStore.daftarPengguna) {
            if (pgn instanceof Instruktur) {
                Instruktur ins = (Instruktur) pgn;
                if ((ins.getIdInstruktur() != null && ins.getIdInstruktur().equals(idInstrukturAktif)) || 
                    (ins.getUsername() != null && ins.getUsername().equals(idInstrukturAktif))) {
                    instrukturMilikKu = ins;
                    this.idInstrukturAktif = ins.getIdInstruktur(); // Amankan ID aslinya
                    break;
                }
            }
        }

        if (instrukturMilikKu != null) {
            lblNamaKelas.setText("Kelas A - " + instrukturMilikKu.getSpesialisasi()); 
            lblDetailInstruktur.setText(instrukturMilikKu.getIdInstruktur() + " - " + instrukturMilikKu.getNamaLengkap() + " - LEVEL " + instrukturMilikKu.getLevelJlpt());
            
            int totalSiswa = 0;
            int totalLulus = 0;
            int akumulasiNilaiKelas = 0;
            int jumlahNilaiKelas = 0;

            // 2. Kalkulasi dari daftar peserta
            for (Peserta p : DataStore.daftarPeserta) {
                // Cek apakah instruktur peserta ini sama dengan instruktur yang login
                if (p.getInstrukturDamping() != null && p.getInstrukturDamping().getIdInstruktur().equals(idInstrukturAktif)) {
        
                totalSiswa++; // Kalau cocok, hitung dia sebagai siswa kelas ini

                if ("Lulus".equalsIgnoreCase(p.getStatusSeleksi())) {
                    totalLulus++; // Kalau cocok dan statusnya lulus, hitung juga
                }

                    // Ambil nilai dari riwayat seleksi untuk rata-rata
                    for (Seleksi s : DataStore.daftarSeleksi) {
                        if (s.getPeserta().getIdPeserta().equals(p.getIdPeserta())) {
                            akumulasiNilaiKelas += s.getNilai();
                            jumlahNilaiKelas++;
                        }
                    }
                }
            }

            // 3. Tampilkan ke kartu statistik
            lblTotalSiswa.setText(String.valueOf(totalSiswa));
            lblLulusSeleksi.setText(String.valueOf(totalLulus));
            
            int rataRata = (jumlahNilaiKelas > 0) ? (akumulasiNilaiKelas / jumlahNilaiKelas) : 0;
            lblRataNilai.setText(String.valueOf(rataRata));
        }
    }
    
    private void setupTable() {
        tabelPeserta.setRowHeight(40);
        TabelUtil.autoResizeKolom(tabelPeserta);
        javax.swing.table.JTableHeader header = tabelPeserta.getTableHeader();
        
        header.setBackground(new java.awt.Color(122, 0, 0));
        header.setForeground(java.awt.Color.WHITE);
        
        header.setFont(new java.awt.Font("Inter", java.awt.Font.BOLD, 13)); 
        header.setOpaque(true);
    }

    private void loadTableData(String keyword) {
        DefaultTableModel model = (DefaultTableModel) tabelPeserta.getModel();
        model.setRowCount(0);
        int no = 1;

        for (Peserta p : DataStore.daftarPeserta) {
            
            // FILTER 1: Pastikan siswa ini diajar oleh instruktur yang sedang login
            if (p.getInstrukturDamping() != null && p.getInstrukturDamping().getIdInstruktur().equals(idInstrukturAktif)) {
                
                // FILTER 2: Fitur Pencarian (Berdasarkan ID atau Nama)
                boolean matchText = keyword.isEmpty() || 
                                    p.getNamaLengkap().toLowerCase().contains(keyword.toLowerCase()) || 
                                    p.getIdPeserta().toLowerCase().contains(keyword.toLowerCase());
                
                if (!matchText) continue; // Lewati jika tidak cocok dengan pencarian

                // Kalkulasi Rata-rata Individu Siswa
                int akumulasiIndividu = 0;
                int jumlahUjian = 0;
                for (Seleksi s : DataStore.daftarSeleksi) {
                    if (s.getPeserta().getIdPeserta().equals(p.getIdPeserta())) {
                        akumulasiIndividu += s.getNilai();
                        jumlahUjian++;
                    }
                }
                String rataNilaiIndividu = (jumlahUjian > 0) ? String.valueOf(akumulasiIndividu / jumlahUjian) : "-";

                // Dummy Kehadiran (Bisa kamu ganti nanti jika sudah ada modul presensi)
                String persentaseHadir = "100%"; 

                // Masukkan ke tabel
                model.addRow(new Object[]{
                    no++,
                    p.getIdPeserta(),
                    p.getNamaLengkap(),
                    p.getLevelBahasa(),
                    rataNilaiIndividu,
                    persentaseHadir,
                    p.getStatusSeleksi(),
                    p.getStatusPembayaran(), // "Lunas" / "Belum Lunas" sesuai constructor DataStore
                    "" // Kolom Aksi (Bisa disisipkan tombol 'Detail' nantinya)
                });
            }
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
        txtCari = new javax.swing.JTextField();
        btnCari = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jSeparator2 = new javax.swing.JSeparator();
        jSeparator3 = new javax.swing.JSeparator();
        jLabel3 = new javax.swing.JLabel();
        panelTabel = new javax.swing.JPanel();
        scrollTabel = new javax.swing.JScrollPane();
        tabelPeserta = new javax.swing.JTable();
        lblTotalData = new javax.swing.JLabel();
        panelKartu1 = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        lblNamaKelas = new javax.swing.JLabel();
        lblDetailInstruktur = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        lblTotalSiswa = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        lblLulusSeleksi = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        lblRataNilai = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();

        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        panelToolbar.setBackground(new java.awt.Color(255, 255, 255));
        panelToolbar.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        txtCari.setPreferredSize(new java.awt.Dimension(280, 30));
        txtCari.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCariActionPerformed(evt);
            }
        });
        panelToolbar.add(txtCari, new org.netbeans.lib.awtextra.AbsoluteConstraints(56, 14, 270, -1));

        btnCari.setBackground(new java.awt.Color(80, 80, 80));
        btnCari.setFont(new java.awt.Font("Inter", 0, 12)); // NOI18N
        btnCari.setForeground(new java.awt.Color(255, 255, 255));
        btnCari.setText("Cari");
        btnCari.setPreferredSize(new java.awt.Dimension(70, 30));
        btnCari.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCariActionPerformed(evt);
            }
        });
        panelToolbar.add(btnCari, new org.netbeans.lib.awtextra.AbsoluteConstraints(334, 14, -1, -1));

        jButton2.setBackground(new java.awt.Color(240, 240, 240));
        jButton2.setFont(new java.awt.Font("Inter", 0, 12)); // NOI18N
        jButton2.setForeground(new java.awt.Color(80, 80, 80));
        jButton2.setText("Reset");
        jButton2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(210, 210, 210)));
        jButton2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton2.setFocusPainted(false);
        jButton2.setPreferredSize(new java.awt.Dimension(70, 30));
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        panelToolbar.add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(412, 14, -1, -1));

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

        tabelPeserta.setFont(new java.awt.Font("Inter", 0, 12)); // NOI18N
        tabelPeserta.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "No.", "ID Peserta", "Nama Lengkap", "Level", "Rata Nilai", "Kehadiran", "Seleksi", "Bayar", "Aksi"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        tabelPeserta.setRowHeight(36);
        tabelPeserta.setSelectionBackground(new java.awt.Color(255, 230, 230));
        tabelPeserta.setSelectionForeground(new java.awt.Color(122, 0, 0));
        tabelPeserta.setShowHorizontalLines(true);
        scrollTabel.setViewportView(tabelPeserta);
        if (tabelPeserta.getColumnModel().getColumnCount() > 0) {
            tabelPeserta.getColumnModel().getColumn(0).setMaxWidth(30);
        }

        panelTabel.add(scrollTabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        add(panelTabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 225, -1, 300));

        lblTotalData.setFont(new java.awt.Font("Inter", 0, 11)); // NOI18N
        lblTotalData.setForeground(new java.awt.Color(120, 120, 120));
        lblTotalData.setText("Hanya dapat melihat, tidak dapat mengubah data");
        lblTotalData.setPreferredSize(new java.awt.Dimension(300, 20));
        add(lblTotalData, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 190, -1, -1));

        panelKartu1.setBackground(new java.awt.Color(122, 0, 0));
        panelKartu1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(220, 220, 220)));
        panelKartu1.setPreferredSize(new java.awt.Dimension(210, 100));
        panelKartu1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel14.setFont(new java.awt.Font("Inter", 0, 12)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(130, 130, 130));
        jLabel14.setText("Kelas yang diampu");
        jLabel14.setPreferredSize(new java.awt.Dimension(140, 16));
        panelKartu1.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 250, -1));

        lblNamaKelas.setFont(new java.awt.Font("Inter", 1, 24)); // NOI18N
        lblNamaKelas.setForeground(new java.awt.Color(255, 255, 255));
        lblNamaKelas.setText("Kelas A - Bahasa Jepang Dasar");
        lblNamaKelas.setPreferredSize(new java.awt.Dimension(100, 36));
        panelKartu1.add(lblNamaKelas, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 30, 460, -1));

        lblDetailInstruktur.setFont(new java.awt.Font("Inter", 0, 10)); // NOI18N
        lblDetailInstruktur.setForeground(new java.awt.Color(240, 170, 170));
        lblDetailInstruktur.setText("INS001 - Dian Pratama S.P.d - LEVEL JLPT N1");
        lblDetailInstruktur.setPreferredSize(new java.awt.Dimension(140, 16));
        panelKartu1.add(lblDetailInstruktur, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 70, 260, -1));

        jPanel2.setBackground(new java.awt.Color(240, 210, 210));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblTotalSiswa.setFont(new java.awt.Font("Inter", 1, 24)); // NOI18N
        lblTotalSiswa.setForeground(new java.awt.Color(122, 0, 0));
        lblTotalSiswa.setText("6");
        jPanel2.add(lblTotalSiswa, new org.netbeans.lib.awtextra.AbsoluteConstraints(36, 4, 54, -1));

        jLabel12.setFont(new java.awt.Font("Inter", 0, 12)); // NOI18N
        jLabel12.setText("Siswa ");
        jPanel2.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 40, -1, -1));

        panelKartu1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 15, 90, 70));

        jPanel3.setBackground(new java.awt.Color(240, 210, 210));
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblLulusSeleksi.setFont(new java.awt.Font("Inter", 1, 24)); // NOI18N
        lblLulusSeleksi.setForeground(new java.awt.Color(122, 0, 0));
        lblLulusSeleksi.setText("4");
        jPanel3.add(lblLulusSeleksi, new org.netbeans.lib.awtextra.AbsoluteConstraints(36, 4, 54, -1));

        jLabel13.setFont(new java.awt.Font("Inter", 0, 10)); // NOI18N
        jLabel13.setText("Lulus Seleksi");
        jPanel3.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(16, 40, -1, -1));

        panelKartu1.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 15, -1, 70));

        jPanel4.setBackground(new java.awt.Color(240, 210, 210));
        jPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblRataNilai.setFont(new java.awt.Font("Inter", 1, 24)); // NOI18N
        lblRataNilai.setForeground(new java.awt.Color(122, 0, 0));
        lblRataNilai.setText("70");
        jPanel4.add(lblRataNilai, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 7, 54, -1));

        jLabel11.setFont(new java.awt.Font("Inter", 0, 12)); // NOI18N
        jLabel11.setText("Rata Nilai");
        jPanel4.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 40, -1, -1));

        panelKartu1.add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(750, 15, 90, 70));

        add(panelKartu1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 70, 950, -1));
    }// </editor-fold>//GEN-END:initComponents

    private void btnCariActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCariActionPerformed
        String kataKunci = txtCari.getText().trim();
        loadTableData(kataKunci);
    }//GEN-LAST:event_btnCariActionPerformed

    private void txtCariActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCariActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCariActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        txtCari.setText("");
        loadTableData("");
    }//GEN-LAST:event_jButton2ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCari;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JLabel lblDetailInstruktur;
    private javax.swing.JLabel lblLulusSeleksi;
    private javax.swing.JLabel lblNamaKelas;
    private javax.swing.JLabel lblRataNilai;
    private javax.swing.JLabel lblTotalData;
    private javax.swing.JLabel lblTotalSiswa;
    private javax.swing.JPanel panelKartu1;
    private javax.swing.JPanel panelTabel;
    private javax.swing.JPanel panelToolbar;
    private javax.swing.JScrollPane scrollTabel;
    private javax.swing.JTable tabelPeserta;
    private javax.swing.JTextField txtCari;
    // End of variables declaration//GEN-END:variables
}
