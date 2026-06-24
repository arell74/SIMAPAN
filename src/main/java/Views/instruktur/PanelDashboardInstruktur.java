/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package Views.instruktur;

import DataStore.DataStore;
import Model.Instruktur;
import Model.Peserta;
import Model.Seleksi;
import Util.TabelUtil;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import javax.swing.Box;
import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

/**
 *
 * @author arelssi
 */
public class PanelDashboardInstruktur extends javax.swing.JPanel {

    /**
     * Creates new form PanelDashboardInstruktur
     */
    private String idInstrukturAktif;
    private Model.Instruktur instrukturAktif;
    
    public PanelDashboardInstruktur(String idInstruktur) {
        initComponents();
        
        this.idInstrukturAktif = idInstruktur;
        System.out.println("DEBUG: ID Instruktur yang masuk ke panel adalah -> " + idInstruktur);
        
        loadDataDashboard();
        setupTablePresensi();
        loadJadwalDinamis();
    }
    
    private void setupTablePresensi() {
         tabelPresensi.setRowHeight(35);
         JComboBox<String> cbKehadiran = new JComboBox<>(new String[]{"Hadir", "Izin", "Sakit", "Alpa"});
         tabelPresensi.getColumnModel().getColumn(4).setCellEditor(new DefaultCellEditor(cbKehadiran));

         loadTabelPresensi();

         TabelUtil.autoResizeKolom(tabelPresensi);

         JTableHeader header = tabelPresensi.getTableHeader();
         header.setBackground(new Color(122, 0, 0));
         header.setForeground(Color.WHITE);
         header.setFont(new Font("Inter", Font.BOLD, 13)); 
         header.setOpaque(true);
     }
    
    private void loadTabelPresensi() {
        DefaultTableModel model = (DefaultTableModel) tabelPresensi.getModel();
        model.setRowCount(0);
        
        if (instrukturAktif == null) return; 
        
        // 1. Ambil teks dari kolom pencarian, ubah ke huruf kecil semua agar pencarian tidak sensitif huruf besar/kecil
        String keyword = txtCari.getText().trim().toLowerCase();
        
        int no = 1;
        for (Model.Peserta p : DataStore.daftarPeserta) {
            
            // 2. Filter utama: pastikan ini adalah anak didik dari instruktur yang sedang login
            if (p.getInstrukturDamping() != null && p.getInstrukturDamping().getIdInstruktur().equals(instrukturAktif.getIdInstruktur())) { 
                
                // 3. Logika Pencarian
                boolean cocok = true;
                
                // Jika kolom pencarian tidak kosong, maka lakukan pengecekan
                if (!keyword.isEmpty()) {
                    String id = p.getIdPeserta().toLowerCase();
                    String nama = p.getNamaLengkap().toLowerCase();
                    
                    // Jika teks yang dicari TIDAK ADA di ID dan TIDAK ADA di Nama, lewati data ini
                    if (!id.contains(keyword) && !nama.contains(keyword)) {
                        cocok = false;
                    }
                }
                
                // 4. Masukkan ke tabel jika cocok
                if (cocok) {
                    model.addRow(new Object[]{
                        no++,
                        p.getIdPeserta(),
                        p.getNamaLengkap(),
                        p.getLevelBahasa(),
                        p.getKehadiranHariIni() 
                    });
                }
            }
        }
    }
    
    private void loadDataDashboard() {
        // Cari instruktur berdasarkan ID/Username
        for (Model.Pengguna pgn : DataStore.daftarPengguna) {
            if (pgn instanceof Model.Instruktur) {
                Model.Instruktur ins = (Model.Instruktur) pgn;
                boolean cocokIDInstruktur = ins.getIdInstruktur() != null && ins.getIdInstruktur().equals(idInstrukturAktif);
                boolean cocokUsername = ins.getUsername() != null && ins.getUsername().equals(idInstrukturAktif);
                
                if (cocokIDInstruktur || cocokUsername) { 
                    this.idInstrukturAktif = ins.getIdInstruktur();
                    this.instrukturAktif = ins; // Simpan ke variabel global
                    break;
                }
            }
        }

        if (instrukturAktif != null) {
            // 1. Update Teks Banner & Profil secara DINAMIS
            lblNamaInstruktur.setText(instrukturAktif.getNamaLengkap());
            lblSpesialisasi.setText("Instruktur aktif - " + instrukturAktif.getSpesialisasi()); 
            
            // Mengambil kelas dan level JLPT dinamis dari model Instruktur
            String kelas = instrukturAktif.getKelasDiampu() != null ? instrukturAktif.getKelasDiampu() : "Belum Ada Kelas";
            String level = instrukturAktif.getLevelJlpt() != null ? instrukturAktif.getLevelJlpt() : "-";
            lblInfoInstruktur.setText(instrukturAktif.getIdInstruktur() + " - KELAS " + kelas.toUpperCase() + " - LEVEL JLPT " + level);
            
            // 2. Hitung Statistik (Siswa, Rata-rata)
            int jumlahSiswa = 0;
            int akumulasiNilai = 0;
            int jumlahNilai = 0;
            
            for (Model.Peserta p : DataStore.daftarPeserta) {
                
                if (p.getInstrukturDamping() != null && p.getInstrukturDamping().getIdInstruktur().equals(instrukturAktif.getIdInstruktur())) { 
                    
                    jumlahSiswa++; // Hitung sebagai siswanya instruktur ini
                    
                    for (Model.Seleksi s : DataStore.daftarSeleksi) {
                        if (s.getPeserta().getIdPeserta().equals(p.getIdPeserta())) {
                            akumulasiNilai += s.getNilai();
                            jumlahNilai++;
                        }
                    }
                }
            }
            
            lblTotalSiswa.setText(String.valueOf(jumlahSiswa));
            int rataRata = (jumlahNilai > 0) ? (akumulasiNilai / jumlahNilai) : 0;
            lblRataNilai.setText(String.valueOf(rataRata));
            
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("EEEE, dd MMMM yyyy", new java.util.Locale("id", "ID"));
            lblTanggalPresensi.setText(sdf.format(new java.util.Date()));
        }
    }

    private void loadJadwalDinamis() {
        pnlWadahJadwal.removeAll();
        int jumlahJadwal = 0;

        for (Model.Jadwal j : DataStore.daftarJadwal) {
            if (j.getIdInstruktur().equals(idInstrukturAktif)) {
                
                jumlahJadwal++; 
                // Asumsi CardJadwal adalah custom component yang sudah kamu buat
                CardJadwal kartu = new CardJadwal();
                kartu.setJadwalData(
                    j.getJamMulai(), 
                    j.getHari(), 
                    j.getNamaMateri(), 
                    "Kelas " + j.getNamaKelas() + " - " + j.getDurasi() + " Menit", 
                    j.getStatusJadwal()
                );
                
                kartu.setAlignmentX(Component.LEFT_ALIGNMENT);

                pnlWadahJadwal.add(kartu);
                pnlWadahJadwal.add(Box.createRigidArea(new Dimension(0, 10)));
            }
        }
        
        lblTotalJadwal.setText(String.valueOf(jumlahJadwal)); 

        pnlWadahJadwal.add(Box.createVerticalGlue());
        pnlWadahJadwal.revalidate();
        pnlWadahJadwal.repaint();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabelPresensi = new javax.swing.JTable();
        txtCari = new javax.swing.JTextField();
        btnCari = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        lblTanggalPresensi = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        pnlJadwal = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        pnlWadahJadwal = new javax.swing.JPanel();
        panelKartu1 = new javax.swing.JPanel();
        lblSpesialisasi = new javax.swing.JLabel();
        lblNamaInstruktur = new javax.swing.JLabel();
        lblInfoInstruktur = new javax.swing.JLabel();
        jPanel16 = new javax.swing.JPanel();
        lblTotalSiswa = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jPanel17 = new javax.swing.JPanel();
        lblTotalJadwal = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        jPanel18 = new javax.swing.JPanel();
        lblRataNilai = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();

        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jScrollPane1.setBackground(new java.awt.Color(255, 255, 255));

        tabelPresensi.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "No", "ID", "Nama", "Level", "Kehadiran"
            }
        ));
        jScrollPane1.setViewportView(tabelPresensi);
        if (tabelPresensi.getColumnModel().getColumnCount() > 0) {
            tabelPresensi.getColumnModel().getColumn(0).setMaxWidth(40);
            tabelPresensi.getColumnModel().getColumn(1).setMaxWidth(70);
            tabelPresensi.getColumnModel().getColumn(2).setMaxWidth(190);
            tabelPresensi.getColumnModel().getColumn(3).setMaxWidth(50);
        }

        txtCari.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtCariKeyReleased(evt);
            }
        });

        btnCari.setBackground(new java.awt.Color(102, 102, 102));
        btnCari.setForeground(new java.awt.Color(255, 255, 255));
        btnCari.setText("Cari");
        btnCari.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCariActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(txtCari, javax.swing.GroupLayout.PREFERRED_SIZE, 277, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnCari, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(24, 24, 24))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtCari, javax.swing.GroupLayout.DEFAULT_SIZE, 34, Short.MAX_VALUE)
                    .addComponent(btnCari, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 472, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 200, 430, 330));

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(220, 220, 220)));

        jLabel1.setFont(new java.awt.Font("Inter", 1, 15)); // NOI18N
        jLabel1.setText("Presensi Hari ini");

        lblTanggalPresensi.setFont(new java.awt.Font("Inter", 0, 10)); // NOI18N
        lblTanggalPresensi.setText("Senin, 01 Juni 2026");

        jPanel3.setBackground(new java.awt.Color(255, 210, 210));
        jPanel3.setForeground(new java.awt.Color(255, 210, 210));

        jLabel3.setForeground(new java.awt.Color(100, 0, 0));
        jLabel3.setText("Minna no nihonggo Bab 1");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addGap(0, 6, Short.MAX_VALUE)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblTanggalPresensi)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 57, Short.MAX_VALUE)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(19, 19, 19))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblTanggalPresensi)))
                .addContainerGap(24, Short.MAX_VALUE))
        );

        add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 140, 430, 70));

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(220, 220, 220)));

        jLabel4.setFont(new java.awt.Font("Inter", 1, 15)); // NOI18N
        jLabel4.setText("Jadwal Mengajar");

        jLabel5.setFont(new java.awt.Font("Inter", 0, 10)); // NOI18N
        jLabel5.setText("Minggu ini -- Kelas A");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5)
                    .addComponent(jLabel4))
                .addContainerGap(283, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel5)
                .addContainerGap(24, Short.MAX_VALUE))
        );

        add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 140, 430, 70));

        pnlJadwal.setBackground(new java.awt.Color(255, 255, 255));
        pnlJadwal.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(220, 220, 220)));

        pnlWadahJadwal.setBackground(new java.awt.Color(240, 240, 240));
        pnlWadahJadwal.setLayout(new javax.swing.BoxLayout(pnlWadahJadwal, javax.swing.BoxLayout.Y_AXIS));
        jScrollPane2.setViewportView(pnlWadahJadwal);

        javax.swing.GroupLayout pnlJadwalLayout = new javax.swing.GroupLayout(pnlJadwal);
        pnlJadwal.setLayout(pnlJadwalLayout);
        pnlJadwalLayout.setHorizontalGroup(
            pnlJadwalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 428, Short.MAX_VALUE)
        );
        pnlJadwalLayout.setVerticalGroup(
            pnlJadwalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlJadwalLayout.createSequentialGroup()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 460, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        add(pnlJadwal, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 210, 430, 360));

        panelKartu1.setBackground(new java.awt.Color(122, 0, 0));
        panelKartu1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(220, 220, 220)));
        panelKartu1.setPreferredSize(new java.awt.Dimension(210, 100));
        panelKartu1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblSpesialisasi.setFont(new java.awt.Font("Inter", 0, 12)); // NOI18N
        lblSpesialisasi.setForeground(new java.awt.Color(130, 130, 130));
        lblSpesialisasi.setText("Instruktur aktif - Bahasa Jepang Dasar");
        lblSpesialisasi.setPreferredSize(new java.awt.Dimension(140, 16));
        panelKartu1.add(lblSpesialisasi, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 250, -1));

        lblNamaInstruktur.setFont(new java.awt.Font("Inter", 1, 28)); // NOI18N
        lblNamaInstruktur.setForeground(new java.awt.Color(255, 255, 255));
        lblNamaInstruktur.setText("Dian Priatna Kusuma S.P.d");
        lblNamaInstruktur.setPreferredSize(new java.awt.Dimension(100, 36));
        panelKartu1.add(lblNamaInstruktur, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 30, 380, -1));

        lblInfoInstruktur.setFont(new java.awt.Font("Inter", 0, 10)); // NOI18N
        lblInfoInstruktur.setForeground(new java.awt.Color(240, 170, 170));
        lblInfoInstruktur.setText("INS001 - KELAS A - LEVEL JLPT N1");
        lblInfoInstruktur.setPreferredSize(new java.awt.Dimension(140, 16));
        panelKartu1.add(lblInfoInstruktur, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 70, 210, -1));

        jPanel16.setBackground(new java.awt.Color(240, 210, 210));
        jPanel16.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblTotalSiswa.setFont(new java.awt.Font("Inter", 1, 24)); // NOI18N
        lblTotalSiswa.setForeground(new java.awt.Color(122, 0, 0));
        lblTotalSiswa.setText("6");
        jPanel16.add(lblTotalSiswa, new org.netbeans.lib.awtextra.AbsoluteConstraints(36, 4, 54, -1));

        jLabel24.setFont(new java.awt.Font("Inter", 0, 12)); // NOI18N
        jLabel24.setText("Siswa ");
        jPanel16.add(jLabel24, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 40, -1, -1));

        panelKartu1.add(jPanel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 10, 90, 70));

        jPanel17.setBackground(new java.awt.Color(240, 210, 210));
        jPanel17.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblTotalJadwal.setFont(new java.awt.Font("Inter", 1, 24)); // NOI18N
        lblTotalJadwal.setForeground(new java.awt.Color(122, 0, 0));
        lblTotalJadwal.setText("6");
        jPanel17.add(lblTotalJadwal, new org.netbeans.lib.awtextra.AbsoluteConstraints(36, 4, 54, -1));

        jLabel26.setFont(new java.awt.Font("Inter", 0, 12)); // NOI18N
        jLabel26.setText("Jadwal");
        jPanel17.add(jLabel26, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 40, -1, -1));

        panelKartu1.add(jPanel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 10, -1, 70));

        jPanel18.setBackground(new java.awt.Color(240, 210, 210));
        jPanel18.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblRataNilai.setFont(new java.awt.Font("Inter", 1, 24)); // NOI18N
        lblRataNilai.setForeground(new java.awt.Color(122, 0, 0));
        lblRataNilai.setText("70");
        jPanel18.add(lblRataNilai, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 0, 54, -1));

        jLabel28.setFont(new java.awt.Font("Inter", 0, 12)); // NOI18N
        jLabel28.setText("Rata-rata");
        jPanel18.add(jLabel28, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 40, -1, -1));

        panelKartu1.add(jPanel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(810, 10, 90, 70));

        add(panelKartu1, new org.netbeans.lib.awtextra.AbsoluteConstraints(24, 24, 920, -1));

        jButton1.setBackground(new java.awt.Color(122, 0, 0));
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setText("Simpan");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 550, 110, -1));
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        if (tabelPresensi.isEditing()) {
            tabelPresensi.getCellEditor().stopCellEditing();
        }

        DefaultTableModel model = (DefaultTableModel) tabelPresensi.getModel();
        int jumlahBaris = model.getRowCount();
        int tersimpan = 0;

        // Looping seluruh baris di tabel
        for (int i = 0; i < jumlahBaris; i++) {
            // Kolom 1 adalah ID Peserta, Kolom 4 adalah Status Kehadiran
            String idPesertaTabel = model.getValueAt(i, 1).toString();
            String statusKehadiran = model.getValueAt(i, 4).toString();

            // Cari peserta di DataStore dan update kehadirannya
            for (Model.Peserta p : DataStore.daftarPeserta) {
                if (p.getIdPeserta().equals(idPesertaTabel)) {
                    p.setKehadiranHariIni(statusKehadiran);
                    tersimpan++;
                    break;
                }
            }
        }

        // Tampilkan pesan sukses
        javax.swing.JOptionPane.showMessageDialog(this, 
            "Berhasil menyimpan presensi untuk " + tersimpan + " siswa hari ini!", 
            "Simpan Sukses", 
            javax.swing.JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void btnCariActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCariActionPerformed
        loadTabelPresensi();
    }//GEN-LAST:event_btnCariActionPerformed

    private void txtCariKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCariKeyReleased
        loadTabelPresensi();
    }//GEN-LAST:event_txtCariKeyReleased


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCari;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lblInfoInstruktur;
    private javax.swing.JLabel lblNamaInstruktur;
    private javax.swing.JLabel lblRataNilai;
    private javax.swing.JLabel lblSpesialisasi;
    private javax.swing.JLabel lblTanggalPresensi;
    private javax.swing.JLabel lblTotalJadwal;
    private javax.swing.JLabel lblTotalSiswa;
    private javax.swing.JPanel panelKartu1;
    private javax.swing.JPanel pnlJadwal;
    private javax.swing.JPanel pnlWadahJadwal;
    private javax.swing.JTable tabelPresensi;
    private javax.swing.JTextField txtCari;
    // End of variables declaration//GEN-END:variables
}
