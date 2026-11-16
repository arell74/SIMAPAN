/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package Views.instruktur;

import DataStore.DataStore;
import Model.HasilKuis;
import Model.Instruktur;
import Model.Kuis;
import Model.Materi;
import Model.Pengguna;
import Model.Peserta;
import Model.Seleksi;
import Util.TabelUtil;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author arelssi
 */
public class PanelLaporanInstruktur extends javax.swing.JPanel {

    private String idInstrukturAktif;
    private boolean sedangMuatFilter = false;

    public PanelLaporanInstruktur(String idInstruktur) {
        initComponents();
        setPreferredSize(new java.awt.Dimension(980, 616));
        this.idInstrukturAktif = idInstruktur;

        setupTable();
        muatOpsiFilter();
        loadDataLaporan();
    }

    private void setupTable() {
        tabelLaporan.setRowHeight(40);

        javax.swing.table.JTableHeader header = tabelLaporan.getTableHeader();
        header.setBackground(new java.awt.Color(122, 0, 0));
        header.setForeground(java.awt.Color.WHITE);
        header.setFont(new java.awt.Font("Inter", java.awt.Font.BOLD, 13));
        header.setOpaque(true);
    }

    private String resolveIdInstruktur() {
        String idAsli = idInstrukturAktif;
        for (Pengguna pgn : DataStore.daftarPengguna) {
            if (pgn instanceof Instruktur) {
                Instruktur ins = (Instruktur) pgn;
                if ((ins.getIdInstruktur() != null && ins.getIdInstruktur().equals(idInstrukturAktif))
                        || (ins.getUsername() != null && ins.getUsername().equals(idInstrukturAktif))) {
                    idAsli = ins.getIdInstruktur();
                    this.idInstrukturAktif = idAsli;
                    break;
                }
            }
        }
        return idAsli;
    }

    private void muatOpsiFilter() {
        sedangMuatFilter = true;
        try {
            DataStore.muatUlang();
            String idAsli = resolveIdInstruktur();

            Object pilihanSebelumnya = cbMateri.getSelectedItem();
            cbMateri.removeAllItems();
            cbMateri.addItem("Semua Materi / Kuis");

            for (Kuis k : DataStore.daftarKuis) {
                if (idAsli != null && idAsli.equals(k.getIdInstruktur())) {
                    cbMateri.addItem("[Kuis] " + k.getJudulKuis());
                }
            }
            for (Materi m : DataStore.daftarMateri) {
                if (idAsli != null && idAsli.equals(m.getIdInstruktur())) {
                    cbMateri.addItem("[Materi] " + m.getJudul());
                }
            }

            if (pilihanSebelumnya != null) {
                cbMateri.setSelectedItem(pilihanSebelumnya);
            }
        } finally {
            sedangMuatFilter = false;
        }
    }

    private void generateLaporan() {
        if (!sedangMuatFilter) {
            loadDataLaporan();
        }
    }

    private void loadDataLaporan() {
        DataStore.muatUlang();

        DefaultTableModel model = (DefaultTableModel) tabelLaporan.getModel();
        model.setRowCount(0);

        String idAsli = resolveIdInstruktur();
        String filterMateri = (cbMateri.getSelectedItem() != null) ? cbMateri.getSelectedItem().toString() : "";
        String judulKuisFilter = filterMateri.startsWith("[Kuis] ")
                ? filterMateri.substring("[Kuis] ".length()).trim()
                : null;

        int totalSiswa = 0;
        int siswaPerluPerhatian = 0;
        int totalPertemuanHadir = 0;
        int totalPertemuanWajib = 0;
        int no = 1;

        for (Peserta p : DataStore.daftarPeserta) {
            if (p.getInstrukturDamping() != null
                    && p.getInstrukturDamping().getIdInstruktur().equals(idAsli)) {
                totalSiswa++;

                // Kumpulkan skor aktual dari Seleksi & HasilKuis milik peserta ini
                List<Integer> daftarSkor = new ArrayList<>();

                if (judulKuisFilter == null) {
                    for (Seleksi s : DataStore.daftarSeleksi) {
                        if (s.getPeserta() != null && s.getPeserta().getIdPeserta().equals(p.getIdPeserta())) {
                            daftarSkor.add(s.getNilai());
                        }
                    }
                }

                for (HasilKuis hk : DataStore.daftarHasilKuis) {
                    if (p.getIdPeserta().equals(hk.getIdPeserta())) {
                        if (judulKuisFilter == null || judulKuisFilter.equalsIgnoreCase(hk.getJudulKuis())) {
                            daftarSkor.add(hk.getNilaiAkhir());
                        }
                    }
                }

                int nilai1 = !daftarSkor.isEmpty() ? daftarSkor.get(0) : 0;
                int nilai2 = daftarSkor.size() > 1 ? daftarSkor.get(1) : nilai1;
                int nilai3 = daftarSkor.size() > 2 ? daftarSkor.get(2) : (daftarSkor.isEmpty() ? 0 : (nilai1 + nilai2) / 2);

                int rataRataIndividu;
                if (daftarSkor.isEmpty()) {
                    rataRataIndividu = 0;
                } else {
                    int sum = 0;
                    for (int skor : daftarSkor) {
                        sum += skor;
                    }
                    rataRataIndividu = Math.round((float) sum / daftarSkor.size());
                }

                // Data kehadiran berdasarkan presensi aktual dari Dashboard Instruktur
                int hadir = 0;
                int izin = 0;
                int alpha = 0;

                String statusHadir = p.getKehadiranHariIni();
                if (statusHadir != null) {
                    if (statusHadir.equalsIgnoreCase("Hadir")) {
                        hadir = 1;
                    } else if (statusHadir.equalsIgnoreCase("Izin") || statusHadir.equalsIgnoreCase("Sakit")) {
                        izin = 1;
                    } else if (statusHadir.equalsIgnoreCase("Alpa")) {
                        alpha = 1;
                    }
                }

                totalPertemuanHadir += hadir;
                totalPertemuanWajib += (hadir + izin + alpha);

                if (rataRataIndividu < 70) {
                    siswaPerluPerhatian++;
                }

                String status = (rataRataIndividu >= 70) ? "Aman" : "Beresiko";

                model.addRow(new Object[]{
                    no++,
                    p.getIdPeserta(),
                    p.getNamaLengkap(),
                    nilai1,
                    nilai2,
                    nilai3,
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
            lblPerluPerhatian.setText(String.valueOf(siswaPerluPerhatian));

            int persenHadir = (totalPertemuanWajib > 0)
                    ? (totalPertemuanHadir * 100) / totalPertemuanWajib
                    : 0;
            lblKehadiran.setText(persenHadir + " %");
        } else {
            lblTotalPeserta.setText("0");
            lblPerluPerhatian.setText("0");
            lblKehadiran.setText("0 %");
        }

        TabelUtil.autoResizeKolom(tabelLaporan);
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
        sedangMuatFilter = true;
        try {
            if (cbMateri.getItemCount() > 0) {
                cbMateri.setSelectedIndex(0);
            }
            if (cbPeriode.getItemCount() > 0) {
                cbPeriode.setSelectedIndex(0);
            }
        } finally {
            sedangMuatFilter = false;
        }
        muatOpsiFilter();
        loadDataLaporan();
    }//GEN-LAST:event_btnResetActionPerformed

    private void btnEksporActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEksporActionPerformed
        DefaultTableModel model = (DefaultTableModel) tabelLaporan.getModel();
        if (model.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this,
                    "Tidak ada data laporan untuk diekspor!",
                    "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Simpan Laporan Excel / CSV");
        fileChooser.setFileFilter(new FileNameExtensionFilter("File Excel/CSV (*.csv)", "csv"));

        int userSelection = fileChooser.showSaveDialog(this);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            String pathFile = fileToSave.getAbsolutePath();

            if (!pathFile.toLowerCase().endsWith(".csv")) {
                fileToSave = new File(pathFile + ".csv");
            }

            try (BufferedWriter bw = new BufferedWriter(
                    new OutputStreamWriter(new FileOutputStream(fileToSave), StandardCharsets.UTF_8))) {

                int colCount = model.getColumnCount();
                for (int i = 0; i < colCount; i++) {
                    bw.write(model.getColumnName(i));
                    if (i < colCount - 1) {
                        bw.write(",");
                    }
                }
                bw.newLine();

                for (int baris = 0; baris < model.getRowCount(); baris++) {
                    for (int kolom = 0; kolom < colCount; kolom++) {
                        Object data = model.getValueAt(baris, kolom);
                        String teksData = (data == null) ? "" : data.toString().replace(",", " ");
                        bw.write(teksData);
                        if (kolom < colCount - 1) {
                            bw.write(",");
                        }
                    }
                    bw.newLine();
                }

                JOptionPane.showMessageDialog(this,
                        "Laporan berhasil diekspor!\nTersimpan di: " + fileToSave.getAbsolutePath(),
                        "Ekspor Berhasil", JOptionPane.INFORMATION_MESSAGE);

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                        "Gagal menyimpan file: " + ex.getMessage(),
                        "Ekspor Gagal", JOptionPane.ERROR_MESSAGE);
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
