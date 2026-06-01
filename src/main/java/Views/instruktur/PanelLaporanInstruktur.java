/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package Views.instruktur;

import DataStore.DataStore;
import Model.Dokumen;
import Model.Instruktur;
import Model.Pengguna;
import Model.Peserta;
import Model.Program;
import Model.Seleksi;
import Util.TabelUtil;
import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.Phrase;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.FileOutputStream;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author arelssi
 */
public class PanelLaporanInstruktur extends javax.swing.JPanel {

    /**
     * Creates new form PanelLaporan
     */
    private String idInstrukturAktif;
    public PanelLaporanInstruktur(String idInstruktur) {
        initComponents();
        setPreferredSize(new java.awt.Dimension(980, 616));
        this.idInstrukturAktif = idInstruktur;
        
        setupTable();
        loadDataLaporan();
    }
    
    private void setupTable() {
        tabelLaporan.setRowHeight(40); 
        
        TabelUtil.autoResizeKolom(tabelLaporan);
        javax.swing.table.JTableHeader header = tabelLaporan.getTableHeader();
        
        header.setBackground(new java.awt.Color(122, 0, 0));
        header.setForeground(java.awt.Color.WHITE);
        
        header.setFont(new java.awt.Font("Inter", java.awt.Font.BOLD, 13)); 
        header.setOpaque(true);
    }
    
    private void setupFilter() {
        // Pasang pendengar ke Dropdown Periode
        cbPeriode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loadDataLaporan(); // Muat ulang tabel setiap kali dropdown berubah
            }
        });

        // Pasang pendengar ke Dropdown Materi
        cbMateri.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loadDataLaporan(); // Muat ulang tabel setiap kali dropdown berubah
            }
        });
    }
    
    private void generateLaporan() {
        
    }
    
    private void loadDataLaporan() {
        DefaultTableModel model = (DefaultTableModel) tabelLaporan.getModel();
        model.setRowCount(0);

        // 1. Amankan ID Asli Instruktur
        String idAsli = idInstrukturAktif;
        for (Pengguna pgn : DataStore.daftarPengguna) {
            if (pgn instanceof Instruktur) {
                Instruktur ins = (Instruktur) pgn;
                if ((ins.getIdInstruktur() != null && ins.getIdInstruktur().equals(idInstrukturAktif)) || 
                    (ins.getUsername() != null && ins.getUsername().equals(idInstrukturAktif))) {
                    idAsli = ins.getIdInstruktur();
                    break;
                }
            }
        }

        // Variabel untuk menghitung Kartu Statistik di atas
        int totalSiswa = 0;
        int akumulasiRataKelas = 0;
        int siswaPerluPerhatian = 0;
        
        // Asumsi hitungan kehadiran dummy (misal 14x pertemuan)
        int totalPertemuanHadir = 0; 
        int totalPertemuanWajib = 0;

        int no = 1;

        // 2. Looping Data Peserta
        for (Peserta p : DataStore.daftarPeserta) {
            
            if (p.getInstrukturDamping() != null && p.getInstrukturDamping().getIdInstruktur().equals(idAsli)) {
                totalSiswa++;

                // === DATA SIMULASI NILAI ===
                // Kita ambil nilai dasarnya dari nilai Seleksi agar sedikit realistis
                int baseScore = 75; // Nilai default jika belum ada nilai seleksi
                for (Seleksi s : DataStore.daftarSeleksi) {
                    if (s.getPeserta().getIdPeserta().equals(p.getIdPeserta())) {
                        baseScore = s.getNilai();
                        break;
                    }
                }

                // Buat nilai per-bab sedikit bervariasi dari baseScore
                int nilaiBab4 = baseScore;
                int nilaiPercakapan = Math.min(100, baseScore + 5); // Maksimal 100
                int nilaiBab5 = Math.max(0, baseScore - 5);         // Minimal 0
                
                int rataRataIndividu = (nilaiBab4 + nilaiPercakapan + nilaiBab5) / 3;

                // === DATA SIMULASI KEHADIRAN ===
                int hadir = 12; // Dummy: Hadir 12 kali
                int izin = 0;
                int alpha = 0;
                
                // Variasi dummy: Jika nilainya kecil, anggap dia pernah alpha
                if (rataRataIndividu < 70) {
                    hadir = 9; alpha = 3;
                }

                // Kalkulasi Kartu Statistik
                akumulasiRataKelas += rataRataIndividu;
                totalPertemuanHadir += hadir;
                totalPertemuanWajib += (hadir + izin + alpha);

                if (rataRataIndividu < 70) {
                    siswaPerluPerhatian++; // Hitung yang nilainya di bawah standar
                }

                String status = (rataRataIndividu >= 70) ? "Aman" : "Beresiko";

                // 3. Masukkan ke Tabel (Urutan harus sama dengan kolom di NetBeans)
                model.addRow(new Object[]{
                    no++,
                    p.getIdPeserta(),
                    p.getNamaLengkap(),
                    nilaiBab4,
                    nilaiPercakapan,
                    nilaiBab5,
                    rataRataIndividu,
                    hadir,
                    izin,
                    alpha,
                    status
                });
            }
        }

        if (totalSiswa > 0) {
            lblTotalPeserta.setText(String.valueOf(totalSiswa));
            
            int rataKelas = akumulasiRataKelas / totalSiswa;
            lblPerluPerhatian.setText(String.valueOf(rataKelas));
            
            lblPerluPerhatian.setText(String.valueOf(siswaPerluPerhatian));
            
            int persenHadir = (totalPertemuanHadir * 100) / totalPertemuanWajib;
            lblKehadiran.setText(persenHadir + " %");
        } else {
            lblTotalPeserta.setText("0");
            lblPerluPerhatian.setText("0");
            lblPerluPerhatian.setText("0");
            lblKehadiran.setText("0 %");
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

        pnlToolbar = new javax.swing.JPanel();
        btnReset = new javax.swing.JButton();
        cbMateri = new javax.swing.JComboBox<>();
        btnEkspor = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        cbPeriode = new javax.swing.JComboBox<>();
        panelTabel = new javax.swing.JPanel();
        lblTotalData = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabelLaporan = new javax.swing.JTable();
        kartuTotal3 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        lblPerluPerhatian = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        kartuTotal4 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        lblTotalPeserta = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        kartuTotal5 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        lblKehadiran = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        kartuTotal6 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        lblRataNilai = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();

        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        pnlToolbar.setBackground(new java.awt.Color(255, 255, 255));
        pnlToolbar.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(220, 220, 220)));
        pnlToolbar.setPreferredSize(new java.awt.Dimension(980, 56));
        pnlToolbar.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnReset.setText("Reset");
        btnReset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnResetActionPerformed(evt);
            }
        });
        pnlToolbar.add(btnReset, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 20, 90, 34));

        cbMateri.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Semua" }));
        cbMateri.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbMateriActionPerformed(evt);
            }
        });
        pnlToolbar.add(cbMateri, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 20, 130, 34));

        btnEkspor.setBackground(new java.awt.Color(0, 122, 0));
        btnEkspor.setFont(new java.awt.Font("Inter", 0, 12)); // NOI18N
        btnEkspor.setForeground(new java.awt.Color(255, 255, 255));
        btnEkspor.setText("Ekspor Laporan");
        btnEkspor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEksporActionPerformed(evt);
            }
        });
        pnlToolbar.add(btnEkspor, new org.netbeans.lib.awtextra.AbsoluteConstraints(820, 20, -1, 34));

        jLabel2.setFont(new java.awt.Font("Inter", 0, 13)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(30, 30, 30));
        jLabel2.setText("Materi:");
        pnlToolbar.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 30, -1, -1));

        jLabel4.setFont(new java.awt.Font("Inter", 0, 13)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(30, 30, 30));
        jLabel4.setText("Periode: ");
        pnlToolbar.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(33, 30, -1, -1));

        cbPeriode.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Semua" }));
        cbPeriode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbPeriodeActionPerformed(evt);
            }
        });
        pnlToolbar.add(cbPeriode, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 20, 140, 34));

        add(pnlToolbar, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 980, 70));

        panelTabel.setBackground(new java.awt.Color(255, 255, 255));
        panelTabel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(220, 220, 220)));
        panelTabel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblTotalData.setFont(new java.awt.Font("Inter", 0, 12)); // NOI18N
        lblTotalData.setForeground(new java.awt.Color(120, 120, 120));
        lblTotalData.setText("Rekap Keberangkatan Peserta");
        panelTabel.add(lblTotalData, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, -1, -1));

        jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        jScrollPane1.setToolTipText("");
        jScrollPane1.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        tabelLaporan.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "No", "ID", "Nama Peserta", "Bab 4", "Percakapan", "Bab 5", "Rata-rata", "Hadir", "Izin", "Alpha", "Status"
            }
        ));
        jScrollPane1.setViewportView(tabelLaporan);
        if (tabelLaporan.getColumnModel().getColumnCount() > 0) {
            tabelLaporan.getColumnModel().getColumn(0).setMaxWidth(50);
            tabelLaporan.getColumnModel().getColumn(1).setPreferredWidth(50);
            tabelLaporan.getColumnModel().getColumn(2).setPreferredWidth(180);
            tabelLaporan.getColumnModel().getColumn(3).setPreferredWidth(150);
            tabelLaporan.getColumnModel().getColumn(3).setMaxWidth(60);
            tabelLaporan.getColumnModel().getColumn(4).setMaxWidth(90);
            tabelLaporan.getColumnModel().getColumn(5).setMaxWidth(50);
            tabelLaporan.getColumnModel().getColumn(6).setMaxWidth(90);
        }

        panelTabel.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 30, 910, 340));

        add(panelTabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 220, 910, 370));

        kartuTotal3.setBackground(new java.awt.Color(255, 255, 255));
        kartuTotal3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(220, 220, 220)));
        kartuTotal3.setPreferredSize(new java.awt.Dimension(296, 56));
        kartuTotal3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel7.setFont(new java.awt.Font("Inter", 0, 12)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(130, 130, 130));
        jLabel7.setText("Nilai dibawah 70");
        kartuTotal3.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 90, -1, -1));

        lblPerluPerhatian.setFont(new java.awt.Font("Inter", 1, 36)); // NOI18N
        lblPerluPerhatian.setForeground(new java.awt.Color(255, 165, 0));
        lblPerluPerhatian.setText("0");
        kartuTotal3.add(lblPerluPerhatian, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, -1, -1));

        jPanel4.setBackground(new java.awt.Color(255, 235, 235));
        jPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setIcon(new javax.swing.ImageIcon("/home/arelssi/Downloads/alert.png")); // NOI18N
        jLabel1.setPreferredSize(new java.awt.Dimension(60, 40));
        jPanel4.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(5, 0, 40, -1));

        kartuTotal3.add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 10, 40, 40));

        jLabel9.setFont(new java.awt.Font("Inter", 0, 12)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(130, 130, 130));
        jLabel9.setText("Perlu Perhatian");
        kartuTotal3.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, -1, -1));

        add(kartuTotal3, new org.netbeans.lib.awtextra.AbsoluteConstraints(720, 80, 217, 120));

        kartuTotal4.setBackground(new java.awt.Color(255, 255, 255));
        kartuTotal4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(220, 220, 220)));
        kartuTotal4.setPreferredSize(new java.awt.Dimension(300, 56));
        kartuTotal4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel8.setFont(new java.awt.Font("Inter", 0, 12)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(130, 130, 130));
        jLabel8.setText("Di kelas A");
        kartuTotal4.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 90, -1, -1));

        lblTotalPeserta.setFont(new java.awt.Font("Inter", 1, 36)); // NOI18N
        lblTotalPeserta.setForeground(new java.awt.Color(122, 0, 0));
        lblTotalPeserta.setText("0");
        kartuTotal4.add(lblTotalPeserta, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, -1, -1));

        jPanel5.setBackground(new java.awt.Color(255, 235, 235));
        jPanel5.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel3.setIcon(new javax.swing.ImageIcon("/home/arelssi/Downloads/user.png")); // NOI18N
        jLabel3.setText("i");
        jLabel3.setPreferredSize(new java.awt.Dimension(60, 40));
        jPanel5.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(7, 0, 30, 40));

        kartuTotal4.add(jPanel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 10, 40, 40));

        jLabel10.setFont(new java.awt.Font("Inter", 0, 12)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(130, 130, 130));
        jLabel10.setText("Total Peserta");
        kartuTotal4.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, -1, -1));

        add(kartuTotal4, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 80, 217, 120));

        kartuTotal5.setBackground(new java.awt.Color(255, 255, 255));
        kartuTotal5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(220, 220, 220)));
        kartuTotal5.setPreferredSize(new java.awt.Dimension(300, 56));
        kartuTotal5.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel11.setFont(new java.awt.Font("Inter", 0, 12)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(130, 130, 130));
        jLabel11.setText("Rata-rata kehadiran");
        kartuTotal5.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 90, -1, -1));

        lblKehadiran.setFont(new java.awt.Font("Inter", 1, 36)); // NOI18N
        lblKehadiran.setForeground(new java.awt.Color(0, 122, 0));
        lblKehadiran.setText("0 %");
        kartuTotal5.add(lblKehadiran, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, -1, -1));

        jPanel6.setBackground(new java.awt.Color(255, 235, 235));
        jPanel6.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel5.setIcon(new javax.swing.ImageIcon("/home/arelssi/Downloads/check.png")); // NOI18N
        jLabel5.setPreferredSize(new java.awt.Dimension(60, 40));
        jPanel6.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(5, 0, 40, -1));

        kartuTotal5.add(jPanel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 10, 40, 40));

        jLabel12.setFont(new java.awt.Font("Inter", 0, 12)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(130, 130, 130));
        jLabel12.setText("Kehadiran Kelas");
        kartuTotal5.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, -1, -1));

        add(kartuTotal5, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 80, 217, 120));

        kartuTotal6.setBackground(new java.awt.Color(255, 255, 255));
        kartuTotal6.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(220, 220, 220)));
        kartuTotal6.setPreferredSize(new java.awt.Dimension(296, 56));
        kartuTotal6.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel13.setFont(new java.awt.Font("Inter", 0, 12)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(130, 130, 130));
        jLabel13.setText("Dari semua materi");
        kartuTotal6.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 90, -1, -1));

        lblRataNilai.setFont(new java.awt.Font("Inter", 1, 36)); // NOI18N
        lblRataNilai.setForeground(new java.awt.Color(0, 0, 122));
        lblRataNilai.setText("0");
        kartuTotal6.add(lblRataNilai, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, -1, -1));

        jPanel7.setBackground(new java.awt.Color(255, 235, 235));
        jPanel7.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel6.setIcon(new javax.swing.ImageIcon("/home/arelssi/Downloads/average.png")); // NOI18N
        jLabel6.setPreferredSize(new java.awt.Dimension(60, 40));
        jPanel7.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(5, 0, 40, -1));

        kartuTotal6.add(jPanel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 10, 40, 40));

        jLabel14.setFont(new java.awt.Font("Inter", 0, 12)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(130, 130, 130));
        jLabel14.setText("Rata-rata Nilai");
        kartuTotal6.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, -1, -1));

        add(kartuTotal6, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 80, 217, 120));

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(220, 220, 220)));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 260, -1, -1));
    }// </editor-fold>//GEN-END:initComponents

    private void cbPeriodeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbPeriodeActionPerformed
        generateLaporan();
    }//GEN-LAST:event_cbPeriodeActionPerformed

    private void cbMateriActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbMateriActionPerformed
        generateLaporan();
    }//GEN-LAST:event_cbMateriActionPerformed

    private void btnResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnResetActionPerformed
        cbMateri.setSelectedIndex(0);
        cbPeriode.setSelectedIndex(0);
        generateLaporan();
    }//GEN-LAST:event_btnResetActionPerformed

    private void btnEksporActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEksporActionPerformed
        javax.swing.JFileChooser fileChooser = new javax.swing.JFileChooser();
        fileChooser.setDialogTitle("Simpan Laporan Excel");
        
        javax.swing.filechooser.FileNameExtensionFilter filter = new javax.swing.filechooser.FileNameExtensionFilter("File Excel/CSV (*.csv)", "csv");
        fileChooser.setFileFilter(filter);

        int userSelection = fileChooser.showSaveDialog(this);
        if (userSelection == javax.swing.JFileChooser.APPROVE_OPTION) {
            
            java.io.File fileToSave = fileChooser.getSelectedFile();
            String pathFile = fileToSave.getAbsolutePath();

            // 2. Trik Cerdas: Tambahkan .csv otomatis kalau user lupa ngetik ekstensinya
            if (!pathFile.toLowerCase().endsWith(".csv")) {
                fileToSave = new java.io.File(pathFile + ".csv");
            }

            try {
                // 3. Siapkan alat tulis (FileWriter) ke dalam file tersebut
                java.io.FileWriter fw = new java.io.FileWriter(fileToSave);
                java.io.BufferedWriter bw = new java.io.BufferedWriter(fw);
                javax.swing.table.DefaultTableModel model = (javax.swing.table.DefaultTableModel) tabelLaporan.getModel();

                // 4. Tulis baris pertama: NAMA KOLOM (Header)
                for (int i = 0; i < model.getColumnCount(); i++) {
                    bw.write(model.getColumnName(i) + ","); // Pakai koma sebagai pemisah antar kolom (CSV)
                }
                bw.newLine(); // Pindah baris ke bawah (Enter)

                // 5. Tulis isinya: DATA TABEL PER BARIS
                for (int baris = 0; baris < model.getRowCount(); baris++) {
                    for (int kolom = 0; kolom < model.getColumnCount(); kolom++) {
                        Object data = model.getValueAt(baris, kolom);
                        
                        // Bersihkan data jika ada koma di dalam teks (misal: "Dian, S.Pd") agar tabel Excel tidak hancur
                        String teksData = (data == null) ? "" : data.toString().replace(",", " ");
                        
                        bw.write(teksData + ",");
                    }
                    bw.newLine(); // Pindah baris tiap selesai 1 murid
                }

                // 6. Tutup file-nya agar tersimpan permanen
                bw.close();
                fw.close();

                // 7. Berikan notifikasi sukses ke Instruktur
                javax.swing.JOptionPane.showMessageDialog(this, 
                    "Laporan berhasil diekspor!\nTersimpan di: " + fileToSave.getAbsolutePath(), 
                    "Ekspor Berhasil", 
                    javax.swing.JOptionPane.INFORMATION_MESSAGE);

            } catch (Exception ex) {
                // Jika error (misal disk penuh atau file sedang dibuka di aplikasi lain)
                javax.swing.JOptionPane.showMessageDialog(this, 
                    "Ups, gagal menyimpan file: " + ex.getMessage(), 
                    "Ekspor Gagal", 
                    javax.swing.JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_btnEksporActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnEkspor;
    private javax.swing.JButton btnReset;
    private javax.swing.JComboBox<String> cbMateri;
    private javax.swing.JComboBox<String> cbPeriode;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPanel kartuTotal3;
    private javax.swing.JPanel kartuTotal4;
    private javax.swing.JPanel kartuTotal5;
    private javax.swing.JPanel kartuTotal6;
    private javax.swing.JLabel lblKehadiran;
    private javax.swing.JLabel lblPerluPerhatian;
    private javax.swing.JLabel lblRataNilai;
    private javax.swing.JLabel lblTotalData;
    private javax.swing.JLabel lblTotalPeserta;
    private javax.swing.JPanel panelTabel;
    private javax.swing.JPanel pnlToolbar;
    private javax.swing.JTable tabelLaporan;
    // End of variables declaration//GEN-END:variables
}
